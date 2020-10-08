package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, List<String> codeList1, String field2, String operator2, Integer value2) {
        if (Comparator.isCodeInCodelist(r.getFieldAsString(field1), codeList1)) {
            if (!Comparator.compareIntegerOperatorInteger(r.getFieldAsInteger(field2), operator2, value2)) {
                ere.setRefNr(String.valueOf(r.getLine()));
                er.addEntry(ere);
            }
        }

        return r;
    }
}
