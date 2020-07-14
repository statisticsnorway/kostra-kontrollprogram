package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

public class ControlFelt1BoolskSaaFelt2Boolsk {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, String operator1, Integer value1, String field2, String operator2, Integer value2) {
        boolean hasErrors = compareIntegers(r.getFieldAsInteger(field1), operator1, value1);

        if (!hasErrors) {
            hasErrors = compareIntegers(r.getFieldAsInteger(field2), operator2, value2);

            if (hasErrors) {
                ere.setRefNr(String.valueOf(r.getLine()));
                er.addEntry(ere);
            }
        }

        return r;
    }

    private static boolean compareIntegers(Integer i1, String operator, Integer i2) {
        boolean hasErrors = false;

        if (i1 != null && !operator.isEmpty() && i2 != null) {
            if (operator.equalsIgnoreCase("<")) {
                hasErrors = !(i1 < i2);

            } else if (operator.equalsIgnoreCase("<=")) {
                hasErrors = !(i1 <= i2);

            } else if (operator.equalsIgnoreCase(">")) {
                hasErrors = !(i1 > i2);

            } else if (operator.equalsIgnoreCase(">=")) {
                hasErrors = !(i1 >= i2);

            } else if (operator.equalsIgnoreCase("==")) {
                hasErrors = !(i1.intValue() == i2.intValue());

            } else if (operator.equalsIgnoreCase("!=")) {
                hasErrors = (i1.intValue() == i2.intValue());
            }
        }

        return hasErrors;
    }
}
