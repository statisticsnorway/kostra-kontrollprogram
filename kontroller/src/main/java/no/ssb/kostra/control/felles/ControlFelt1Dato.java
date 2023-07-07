package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import static no.ssb.kostra.control.felles.Comparator.isValidDate;

public final class ControlFelt1Dato {

    private ControlFelt1Dato() {
    }

    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String date1, final String datePattern1) {
        return !isValidDate(date1, datePattern1) && errorReport.addEntry(errorReportEntry);
    }
}
