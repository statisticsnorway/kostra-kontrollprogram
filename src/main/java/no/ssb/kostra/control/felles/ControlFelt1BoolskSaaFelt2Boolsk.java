package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

public class ControlFelt1BoolskSaaFelt2Boolsk {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, String operator1, Integer value1, String field2, String operator2, Integer value2) {
        boolean hasErrors = false;

        if (operator1.equalsIgnoreCase("<")) {
            hasErrors = !(r.getFieldAsInteger(field1) < value1);

        } else if (operator1.equalsIgnoreCase("<=")) {
            hasErrors = !(r.getFieldAsInteger(field1) <= value1);

        } else if (operator1.equalsIgnoreCase(">")) {
            hasErrors = !(r.getFieldAsInteger(field1) > value1);

        } else if (operator1.equalsIgnoreCase(">=")) {
            hasErrors = !(r.getFieldAsInteger(field1) >= value1);

        } else if (operator1.equalsIgnoreCase("==")) {
            hasErrors = !(r.getFieldAsInteger(field1).intValue() == value1);
        }

        if (!hasErrors){
            if (operator2.equalsIgnoreCase("<")) {
                hasErrors = !(r.getFieldAsInteger(field2) < value2);

            } else if (operator2.equalsIgnoreCase("<=")) {
                hasErrors = !(r.getFieldAsInteger(field2) <= value2);

            } else if (operator2.equalsIgnoreCase(">")) {
                hasErrors = !(r.getFieldAsInteger(field2) > value2);

            } else if (operator2.equalsIgnoreCase(">=")) {
                hasErrors = !(r.getFieldAsInteger(field2) >= value2);

            } else if (operator2.equalsIgnoreCase("==")) {
                hasErrors = !(r.getFieldAsInteger(field2).intValue() == value2);
            }
        }

        if (hasErrors) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
        }

        return r;
    }
}
