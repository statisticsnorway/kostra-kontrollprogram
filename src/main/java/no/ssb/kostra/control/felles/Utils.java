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
                .peek(fieldDefinition -> {
                    if (fieldDefinition.getName().equalsIgnoreCase("skjema")) {
                        fieldDefinition.setCodeList(List.of(new Code(args.getSkjema(), "Skjematype")));
                    } else if (fieldDefinition.getName().equalsIgnoreCase("aargang")) {
                        fieldDefinition.setCodeList(List.of(new Code(args.getAargang(), "??rgang")));
                    } else if (fieldDefinition.getName().equalsIgnoreCase("region")) {
                        fieldDefinition.setCodeList(List.of(new Code(args.getRegion(), "Region")));
                    } else if (fieldDefinition.getName().equalsIgnoreCase("orgnr")) {
                        fieldDefinition.setCodeList(Stream.of(args.getOrgnr().split(","))
                                .map(n -> new Code(n, "Organisasjonsnummer"))
                                .toList());
                    } else if (fieldDefinition.getName().equalsIgnoreCase("foretaksnr")) {
                        fieldDefinition.setCodeList(Stream.of(args.getForetaknr().concat(",").concat(args.getOrgnr()).split(","))
                                .map(n -> new Code(n, "Foretaksnummer"))
                                .toList());
                    }
                })
                .toList();
    }

    public static List<String> rpadList(final List<String> list, final int width) {
        return list.stream()
                // rightPad / legger til mellomrom p?? slutten av tekstene slik at alle blir (width) tegn lange
                .map(c -> String.format("%1$-" + width + "s", c))
                .toList();
    }

    public static String replaceSpaceWithNoBreakingSpace(final String s) {
        return s.replace(" ", "&nbsp;");
    }

    public static List<KostraRecord> removeBelopEquals0(final List<KostraRecord> regnskap1) {
        // Fjerner posteringer der bel??p = 0
        return regnskap1.stream()
                // fjerner record der bel??pet er 0, brukes ifm. med alle regnskap
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
