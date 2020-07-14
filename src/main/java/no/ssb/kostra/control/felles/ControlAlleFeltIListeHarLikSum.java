package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;

public class ControlAlleFeltIListeHarLikSum {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, List<String> fieldList) {
        int sum = r.getFieldAsInteger(fieldList.get(0));
        boolean allSumsEqual = fieldList.stream()
                .map(r::getFieldAsInteger)
                .allMatch(item -> item == sum);

        if (!allSumsEqual) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
        }

        return r;
    }

}