package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

public final class ControlFelt1Boolsk {

    private ControlFelt1Boolsk() {
    }

    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final Integer value1, final String operator1, final Integer checkValue) {
        return !Comparator.compareIntegerOperatorInteger(
                value1, operator1, checkValue) && errorReport.addEntry(errorReportEntry);
    }
}