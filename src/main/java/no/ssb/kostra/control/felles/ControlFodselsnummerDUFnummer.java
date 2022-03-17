package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.utils.Fnr;

import static no.ssb.kostra.control.felles.Comparator.isValidDate;

@SuppressWarnings("SpellCheckingInspection")
public class ControlFodselsnummerDUFnummer {
    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String fnr, final String duf) {

        if (!Fnr.isValidNorwId(fnr)
                && !Fnr.isValidDUFnr(duf)
                && !(isValidDate(fnr.substring(0, 6), "ddMMyy")
                && duf.trim().length() == 0)) {
            errorReport.addEntry(errorReportEntry);
            return true;
        }
        return false;
    }
}
