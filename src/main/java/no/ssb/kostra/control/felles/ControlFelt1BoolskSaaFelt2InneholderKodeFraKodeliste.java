package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;

public class ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, String operator1, Integer value1, String field2, List<String> codeList2) {
        boolean hasErrors = false;
        Integer recordValue = r.getFieldAsInteger(field1);

        if (recordValue != null) {
            int rv = recordValue;
            int v = value1;

            if (operator1.equalsIgnoreCase("<")) {
                hasErrors = !(v < rv);

            } else if (operator1.equalsIgnoreCase("<=")) {
                hasErrors = !(v <= rv);

            } else if (operator1.equalsIgnoreCase(">")) {
                hasErrors = !(v > rv);

            } else if (operator1.equalsIgnoreCase(">=")) {
                hasErrors = !(v >= rv);

            } else if (operator1.equalsIgnoreCase("==")) {
                hasErrors = !(v == rv);
            }

            if (!hasErrors) {
                if (codeList2.stream().noneMatch(n -> n.equalsIgnoreCase(r.getFieldAsString(field2)))) {
                    ere.setRefNr(String.valueOf(r.getLine()));
                    er.addEntry(ere);
                }
            }
        }
        return r;
    }
}
