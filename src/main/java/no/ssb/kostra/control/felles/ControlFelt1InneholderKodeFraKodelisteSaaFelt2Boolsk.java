package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.List;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, String fieldvalue1, List<String> codeList1, Integer fieldvalue2, String operator2, Integer checkvalue2) {
        return (Comparator.isCodeInCodelist(fieldvalue1, codeList1)
                && !Comparator.compareIntegerOperatorInteger(fieldvalue2, operator2, checkvalue2)
                && er.addEntry(ere));
    }
}
