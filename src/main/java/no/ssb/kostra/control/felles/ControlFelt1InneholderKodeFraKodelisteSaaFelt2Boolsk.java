package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk {
    public static Record doControl(Record p, ErrorReport er, ErrorReportEntry ere, String field1, List<String> codeList1, String field2, String operator, Integer value) {
        if (codeList1.contains(p.getFieldAsString(field1))) {
            boolean hasErrors = false;

            if (operator.equalsIgnoreCase("<")){
                hasErrors = !(p.getFieldAsInteger(field2) < value);

            } else if (operator.equalsIgnoreCase(">")){
                hasErrors = !(p.getFieldAsInteger(field2) > value);

            } else if (operator.equalsIgnoreCase("==")){
                hasErrors = !(p.getFieldAsInteger(field2).intValue() == value);
            }

            if (hasErrors) {
                ere.setRefNr(String.valueOf(p.getLine()));
                er.addEntry(ere);
            }
        }

        return p;
    }
}
