package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.time.LocalDate;
import java.util.List;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, String fieldvalue1, List<String> codeList1, LocalDate date2) {
        return (Comparator.isCodeInCodelist(fieldvalue1, codeList1)
                && date2 == null
                && er.addEntry(ere));
    }
}
