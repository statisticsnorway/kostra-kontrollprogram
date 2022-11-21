package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

public class ControlFelt1Lengde {

    public static boolean doControl(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final String trimmedFieldValue1) {

        return trimmedFieldValue1.isEmpty() && errorReport.addEntry(errorReportEntry);
    }
}
