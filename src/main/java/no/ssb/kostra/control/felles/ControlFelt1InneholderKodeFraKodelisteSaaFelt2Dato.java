package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato {
    public static Record doControl(Record p, ErrorReport er, ErrorReportEntry ere, String field1, List<String> codeList1, String field2) {
        if (codeList1.contains(p.getFieldAsString(field1))) {
            try {
                p.getFieldAsLocalDate(field2);
            } catch (Exception e) {
                ere.setRefNr(String.valueOf(p.getLine()));
                er.addEntry(ere);

            }
        }

        return p;
    }
}
