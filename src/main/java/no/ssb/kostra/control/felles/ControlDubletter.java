package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ControlDubletter {
    public static boolean doControl(List<Record> recordList, ErrorReport er, List<String> fieldList) {
        // Dublett kontroll
        List<String> dubletter = recordList.stream()
                .map(p -> fieldList.stream().map(p::getFieldAsTrimmedString).collect(Collectors.joining(" * ")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(p -> p.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        if (!dubletter.isEmpty()) {
            er.addEntry(
                    new ErrorReportEntry("Filuttrekk", "Dubletter", " ", " "
                            , "Kontroll Dubletter"
                            , "Dubletter (lik " + String.join(" * ", fieldList) + ") summeres sammen. (Gjelder for:<br/>\n" + String.join(",<br/>\n", dubletter) + ")"
                            , Constants.NORMAL_ERROR
                    ));
        }

        return false;
    }
}
