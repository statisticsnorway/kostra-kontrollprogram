package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

public class ControlFelt1Boolsk {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, Integer value1, String operator1, Integer checkvalue) {
        return !Comparator.compareIntegerOperatorInteger(value1, operator1, checkvalue) && er.addEntry(ere);
    }
}