package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

public class ControlFelt1Boolsk {
    public static Record doControl(Record p, ErrorReport er, ErrorReportEntry ere, String field1, String operator1, Integer value1) {
        boolean hasErrors = false;

        if (operator1.equalsIgnoreCase("<")) {
            hasErrors = !(p.getFieldAsInteger(field1) < value1);

        } else if (operator1.equalsIgnoreCase(">")) {
            hasErrors = !(p.getFieldAsInteger(field1) > value1);

        } else if (operator1.equalsIgnoreCase("==")) {
            hasErrors = !(p.getFieldAsInteger(field1).intValue() == value1);
        }

        if (hasErrors) {
            ere.setRefNr(String.valueOf(p.getLine()));
            er.addEntry(ere);
        }

        return p;
    }
}
