package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

import java.util.List;

public class ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, String operator1, Integer value1, String field2, List<String> codeList2) {
        if (Comparator.compareIntegerOperatorInteger(r.getFieldAsInteger(field1), operator1, value1)) {
            if (!Comparator.isCodeInCodelist(r.getFieldAsString(field2), codeList2)) {
                ere.setRefNr(String.valueOf(r.getLine()));
                er.addEntry(ere);
            }
        }

        return r;
    }
}
