package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

import java.util.List;

public class ControlFelt1InneholderKodeFraKodeliste {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, List<String> codeList1) {
        if (!Comparator.isCodeInCodelist(r.getFieldAsString(field1), codeList1)) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
        }

        return r;
    }
}
