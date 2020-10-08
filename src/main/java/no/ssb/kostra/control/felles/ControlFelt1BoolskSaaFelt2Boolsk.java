package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

public class ControlFelt1BoolskSaaFelt2Boolsk {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, String operator1, Integer value1, String field2, String operator2, Integer value2) {
        if (Comparator.compareIntegerOperatorInteger(r.getFieldAsInteger(field1), operator1, value1)) {
            if (!Comparator.compareIntegerOperatorInteger(r.getFieldAsInteger(field2), operator2, value2)) {
                ere.setRefNr(String.valueOf(r.getLine()));
                er.addEntry(ere);
            }
        }

        return r;
    }
}
