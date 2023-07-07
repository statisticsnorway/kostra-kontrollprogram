package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.KostraRecord;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpellCheckingInspection")
public final class ControlAlleFeltIListeHarLikSum {

    private ControlAlleFeltIListeHarLikSum() {
    }

    public static boolean doControl(
            final KostraRecord record, final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry, final List<String> fieldList) {

        final var sum = record.getFieldAsIntegerDefaultEquals0(fieldList.get(0));
        final var allSumsEqual = fieldList.stream()
                .map(record::getFieldAsIntegerDefaultEquals0)
                .allMatch(item -> item.equals(sum));

        if (allSumsEqual) {
            return false;
        }

        final var errorText = "Én eller flere i følgende liste har ulik verdi i forhold til de andre: "
                + fieldList.stream()
                .map(f -> "("
                        .concat(f)
                        .concat(" = ")
                        .concat(record.getFieldAsIntegerDefaultEquals0(f).toString())
                        .concat(")")
                )
                .collect(Collectors.joining(", "));

        errorReport.addEntry(new ErrorReportEntry(
                errorReportEntry.getSaksbehandler()
                , errorReportEntry.getJournalnummer()
                , errorReportEntry.getIndividId()
                , String.valueOf(record.getLine())
                , errorReportEntry.getKontrollNr()
                , errorText
                , errorReportEntry.getErrorType()
        ));
        return true;
    }
}