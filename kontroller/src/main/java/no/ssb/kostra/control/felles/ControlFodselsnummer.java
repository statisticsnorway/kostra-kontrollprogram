package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.utils.Fnr;

public final class ControlFodselsnummer {
    private ControlFodselsnummer() {
    }

    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry, final String fieldValueSSN) {
        return !Fnr.isValidNorwId(fieldValueSSN) && errorReport.addEntry(errorReportEntry);
    }
}
