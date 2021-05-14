package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

public class ControlFelt1BoolskSaaFelt2Boolsk {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, Integer fieldvalue1, String operator1, Integer checkvalue1, Integer fieldvalue2, String operator2, Integer checkvalue2) {
        return (Comparator.compareIntegerOperatorInteger(fieldvalue1, operator1, checkvalue1)
                && !Comparator.compareIntegerOperatorInteger(fieldvalue2, operator2, checkvalue2)
                && er.addEntry(ere));
    }
}
