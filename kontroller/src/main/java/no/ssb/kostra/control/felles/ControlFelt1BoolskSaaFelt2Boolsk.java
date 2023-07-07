package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

public final class ControlFelt1BoolskSaaFelt2Boolsk {

    private ControlFelt1BoolskSaaFelt2Boolsk() {
    }

    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final Integer fieldvalue1, final String operator1, final Integer checkvalue1,
            final Integer fieldvalue2, final String operator2, final Integer checkvalue2) {

        return Comparator.compareIntegerOperatorInteger(fieldvalue1, operator1, checkvalue1)
                && !Comparator.compareIntegerOperatorInteger(fieldvalue2, operator2, checkvalue2)
                && errorReport.addEntry(errorReportEntry);
    }
}
