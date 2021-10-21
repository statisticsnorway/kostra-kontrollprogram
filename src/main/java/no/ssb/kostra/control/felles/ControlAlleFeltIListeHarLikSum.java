package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

import java.util.List;
import java.util.stream.Collectors;

public class ControlAlleFeltIListeHarLikSum {
    public static boolean doControl(Record r, ErrorReport er, ErrorReportEntry ere, List<String> fieldList) {
        int sum = r.getFieldAsIntegerDefaultEquals0(fieldList.get(0));
        boolean allSumsEqual = fieldList.stream()
                .map(r::getFieldAsIntegerDefaultEquals0)
                .allMatch(item -> item == sum);

        if (!allSumsEqual) {
            String errorText = "Én eller flere i følgende liste har ulik verdi i forhold til de andre: "
                    + fieldList.stream()
                    .map(f -> "("
                            .concat(f)
                            .concat(" = ")
                            .concat(r.getFieldAsIntegerDefaultEquals0(f).toString())
                            .concat(")")
                    )
                    .collect(Collectors.joining(", "));

            ErrorReportEntry e = new ErrorReportEntry(
                    ere.getSaksbehandler()
                    , ere.getJournalnummer()
                    , ere.getIndividId()
                    , String.valueOf(r.getLine())
                    , ere.getKontrollNr()
                    , errorText
                    , ere.getErrorType()
            );

            er.addEntry(e);
            return true;
        }

        return false;
    }

}