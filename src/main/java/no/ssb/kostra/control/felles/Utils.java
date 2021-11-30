package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Code;
import no.ssb.kostra.felles.FieldDefinition;
import no.ssb.kostra.felles.Record;
import no.ssb.kostra.utils.Format;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static String createLinenumber(int l, Record r) {
        return "Linje " + Format.sprintf("%0" + l + "d", r.getLine()) + " : " + r.getRecord().trim();
    }

    public static List<Record> addLineNumbering(List<Record> records) {
        AtomicInteger lineNumber = new AtomicInteger(0);

        return records.stream()
                .peek(r -> r.setFieldAsInteger("linjenummer", lineNumber.incrementAndGet()))
                .peek(r -> r.setFieldAsInteger("LINJENUMMER", lineNumber.get()))
                .collect(Collectors.toList());
    }

    public static Record addLineNumbering(Record record) {
        AtomicInteger lineNumber = new AtomicInteger(0);

        record.setFieldAsInteger("linjenummer", lineNumber.incrementAndGet());
        record.setFieldAsInteger("LINJENUMMER", lineNumber.get());

        return record;
    }

    public static List<Record> getValidRecords(List<String> list1, List<FieldDefinition> fieldDefinitions) {
        return list1.stream()
                // fjernet blanke linjer
                .filter(l -> l.trim().length() != 0)
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());
    }

    public static List<FieldDefinition> mergeFieldDefinitionsAndArguments(List<FieldDefinition> fieldDefinitions, Arguments args) {
        return fieldDefinitions.stream()
                .map(d -> {
                    if (d.getName().equalsIgnoreCase("skjema")) {
                        d.setCodeList(List.of(new Code(args.getSkjema(), "Skjematype")));

                    } else if (d.getName().equalsIgnoreCase("aargang")) {
                        d.setCodeList(List.of(new Code(args.getAargang(), "Årgang")));

                    } else if (d.getName().equalsIgnoreCase("region")) {
                        d.setCodeList(List.of(new Code(args.getRegion(), "Region")));

                    } else if (d.getName().equalsIgnoreCase("orgnr")) {
                        List<Code> list = Stream.of(args.getOrgnr().split(","))
                                .collect(Collectors.toList())
                                .stream()
                                .map(n -> new Code(n, "Organisasjonsnummer"))
                                .collect(Collectors.toList());

                        d.setCodeList(list);

                    } else if (d.getName().equalsIgnoreCase("foretaksnr")) {
                        List<Code> list = Stream.of(args.getForetaknr().concat(",").concat(args.getOrgnr()).split(","))
                                .collect(Collectors.toList())
                                .stream()
                                .map(n -> new Code(n, "Foretaksnummer"))
                                .collect(Collectors.toList());

                        d.setCodeList(list);
                    }

                    return d;
                })
                .collect(Collectors.toList());
    }

    public static List<String> rpadList(List<String> list, int width) {
        return list.stream()
                // rightPad / legger til mellomrom på slutten av tekstene slik at alle blir (width) tegn lange
                .map(c -> String.format("%1$-" + width + "s", c))
                .collect(Collectors.toList());
    }

    public static String replaceSpaceWithNoBreakingSpace(String s) {
        return s.replace(" ", "&nbsp;");
    }

    public static List<Record> removeBelopEquals0(List<Record> regnskap1) {
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
                .collect(Collectors.toList());
    }
}
