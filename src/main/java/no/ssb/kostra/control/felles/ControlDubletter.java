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
    public static boolean doControl(List<Record> recordList, ErrorReport er, List<String> fieldList, List<String> titleList) {
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
                    new ErrorReportEntry("9. Dublettkontroller", "Dubletter", " ", " "
                            , "Kontroll Dubletter"
                            , "Det er oppgitt flere beløp på samme kombinasjon av (" + String.join(" * ", titleList) + ")."
                            + " Hvis dette er riktig, kan du sende inn filen, og beløpene summeres hos SSB. Dersom dette er feil må recordene korrigeres før innsending til SSB."
                            , Constants.NORMAL_ERROR
                    ));
        }

        return false;
    }
}
