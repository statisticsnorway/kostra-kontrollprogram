package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Code;
import no.ssb.kostra.felles.FieldDefinition;
import no.ssb.kostra.felles.KostraRecord;
import no.ssb.kostra.utils.Format;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@SuppressWarnings("SpellCheckingInspection")
public class Utils {

    public static String createLinenumber(final int l, final KostraRecord record) {
        return "Linje " + Format.sprintf("%0" + l + "d", record.getLine()) + " : " + record.getRecord().trim();
    }

    public static List<KostraRecord> addLineNumbering(final List<KostraRecord> records) {
        final var lineNumber = new AtomicInteger(0);

        return records.stream()
                .peek(r -> r.setFieldAsInteger("linjenummer", lineNumber.incrementAndGet()))
                .peek(r -> r.setFieldAsInteger("LINJENUMMER", lineNumber.get()))
                .toList();
    }

    public static KostraRecord addLineNumbering(final KostraRecord record) {
        final var lineNumber = new AtomicInteger(0);

        record.setFieldAsInteger("linjenummer", lineNumber.incrementAndGet());
        record.setFieldAsInteger("LINJENUMMER", lineNumber.get());

        return record;
    }

    public static List<KostraRecord> getValidRecords(
            final List<String> list1, final List<FieldDefinition> fieldDefinitions) {

        return list1.stream()
                // fjernet blanke linjer
                .filter(l -> l.trim().length() != 0)
                .map(p -> new KostraRecord(p, fieldDefinitions))
                .toList();
    }

    public static List<FieldDefinition> mergeFieldDefinitionsAndArguments(
            final List<FieldDefinition> fieldDefinitions, final Arguments args) {

        return fieldDefinitions.stream()
                .peek(d -> {
                    if (d.getName().equalsIgnoreCase("skjema")) {
                        d.setCodeList(List.of(new Code(args.getSkjema(), "Skjematype")));
                    } else if (d.getName().equalsIgnoreCase("aargang")) {
                        d.setCodeList(List.of(new Code(args.getAargang(), "Årgang")));
                    } else if (d.getName().equalsIgnoreCase("region")) {
                        d.setCodeList(List.of(new Code(args.getRegion(), "Region")));
                    } else if (d.getName().equalsIgnoreCase("orgnr")) {
                        List<Code> list = Stream.of(args.getOrgnr().split(","))
                                .toList()
                                .stream()
                                .map(n -> new Code(n, "Organisasjonsnummer"))
                                .toList();

                        d.setCodeList(list);
                    } else if (d.getName().equalsIgnoreCase("foretaksnr")) {
                        List<Code> list = Stream.of(args.getForetaknr().concat(",").concat(args.getOrgnr()).split(","))
                                .toList()
                                .stream()
                                .map(n -> new Code(n, "Foretaksnummer"))
                                .toList();

                        d.setCodeList(list);
                    }

                })
                .toList();
    }

    public static List<String> rpadList(final List<String> list, final int width) {
        return list.stream()
                // rightPad / legger til mellomrom på slutten av tekstene slik at alle blir (width) tegn lange
                .map(c -> String.format("%1$-" + width + "s", c))
                .toList();
    }

    public static String replaceSpaceWithNoBreakingSpace(final String s) {
        return s.replace(" ", "&nbsp;");
    }

    public static List<KostraRecord> removeBelopEquals0(final List<KostraRecord> regnskap1) {
        // Fjerner posteringer der beløp = 0
        return regnskap1.stream()
                // fjerner record der beløpet er 0, brukes ifm. med alle regnskap
                .filter(p -> {
                    try {
                        return p.getFieldAsInteger("belop") != 0;
                    } catch (NullPointerException e) {
                        return true;
                    }
                })
                .toList();
    }
}
