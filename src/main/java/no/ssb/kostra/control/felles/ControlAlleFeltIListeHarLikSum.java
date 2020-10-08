package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;
import java.util.stream.Collectors;

public class ControlAlleFeltIListeHarLikSum {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, List<String> fieldList) {
        int sum = r.getFieldAsInteger(fieldList.get(0));
        boolean allSumsEqual = fieldList.stream()
                .map(r::getFieldAsInteger)
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
        }

        return r;
    }

}