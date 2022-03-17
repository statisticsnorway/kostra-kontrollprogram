package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.KostraRecord;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("SpellCheckingInspection")
public class ControlDubletter {

    public static boolean doControl(
            final List<KostraRecord> recordList, final ErrorReport errorReport,
            final List<String> fieldList, final List<String> titleList) {

        // Dublett kontroll
        final var dubletter = recordList.stream()
                .map(p -> fieldList.stream().map(p::getFieldAsTrimmedString).collect(Collectors.joining(" * ")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(p -> p.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        return !dubletter.isEmpty() && errorReport.addEntry(
                new ErrorReportEntry("4. Dublettkontroller", "Kontroll Dubletter", " ", " "
                        , "Det er oppgitt flere beløp på samme kombinasjon av (" + String.join(" * ", titleList) + ")."
                        + " Hvis dette er riktig, kan du sende inn filen, og beløpene summeres hos SSB. Dersom dette er feil må recordene korrigeres før innsending til SSB."
                        , ""
                        , Constants.NORMAL_ERROR
                ));
    }
}
