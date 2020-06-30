package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

public class ControlFelt1Boolsk {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, String operator1, Integer value1) {
        boolean hasErrors = false;
        Integer recordValue = r.getFieldAsInteger(field1);

        if (recordValue != null) {
            int rv = recordValue;
            int v = value1;

            if (operator1.equalsIgnoreCase("<")) {
                hasErrors = !(rv < v);

            } else if (operator1.equalsIgnoreCase("<=")) {
                hasErrors = !(rv <= v);

            } else if (operator1.equalsIgnoreCase(">")) {
                hasErrors = !(rv > v);

            } else if (operator1.equalsIgnoreCase(">=")) {
                hasErrors = !(rv >= v);

            } else if (operator1.equalsIgnoreCase("==")) {
                hasErrors = !(rv == v);
            }

            if (hasErrors) {
                ere.setRefNr(String.valueOf(r.getLine()));
                er.addEntry(ere);
            }
        }

        return r;
    }
}
