package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;
import no.ssb.kostra.utils.Fnr;

public class ControlAlderFraFodselsnummer {
    public static Record doControl(Record p, ErrorReport er, ErrorReportEntry ere, String fieldSSN1, String operator, int age1, int reportYear) {
        boolean hasErrors;

        try {
            int age = Fnr.getAlderFromFnr(p.getFieldAsString(fieldSSN1), reportYear);
            hasErrors = !Comparator.compareIntegerOperatorInteger(age, operator, age1);

        } catch (Exception e) {
            hasErrors = true;
        }

        if (hasErrors) {
            ere.setRefNr(String.valueOf(p.getLine()));
            er.addEntry(ere);
        }

        return p;
    }
}
