package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

public class ControlHeltall {
    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry, final Integer value1) {
        return value1 == null && errorReport.addEntry(errorReportEntry);
    }
}
