package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;

public class ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste {
    public static Record doControl(Record p, ErrorReport er, ErrorReportEntry ere, String field1, String operator1, Integer value1, String field2, List<String> codeList2) {
        boolean doCheck = false;

        if (p.getFieldAsInteger(field1) != null) {
            if (operator1.equalsIgnoreCase("<")) {
                doCheck = (p.getFieldAsInteger(field1) < value1);

            } else if (operator1.equalsIgnoreCase(">")) {
                doCheck = (p.getFieldAsInteger(field1) > value1);

            } else if (operator1.equalsIgnoreCase("==")) {
                doCheck = (p.getFieldAsInteger(field1).intValue() == value1);
            }
        }
        if (doCheck) {
            if (codeList2.stream().noneMatch(n -> n.equalsIgnoreCase(p.getFieldAsString(field2)))) {
                ere.setRefNr(String.valueOf(p.getLine()));
                er.addEntry(ere);
            }
        }

        return p;
    }
}
