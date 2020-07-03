package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, List<String> codeList1, String field2, String operator2, Integer value2) {
        if (codeList1.contains(r.getFieldAsString(field1))) {
            boolean hasErrors = false;

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

            } else if (operator2.equalsIgnoreCase("!=")) {
                hasErrors = r.getFieldAsInteger(field2).intValue() == value2;
            }

            if (hasErrors) {
                ere.setRefNr(String.valueOf(r.getLine()));
                er.addEntry(ere);
            }
        }

        return r;
    }
}
