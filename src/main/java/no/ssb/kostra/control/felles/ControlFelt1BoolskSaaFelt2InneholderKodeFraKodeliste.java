package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.List;

public class ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, Integer fieldvalue1, String operator1, Integer checkvalue1, String fieldvalue2, List<String> codeList2) {
        return (Comparator.compareIntegerOperatorInteger(fieldvalue1, operator1, checkvalue1)
                && !Comparator.isCodeInCodelist(fieldvalue2, codeList2)
                && er.addEntry(ere));
    }
}
