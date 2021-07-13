package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.utils.Fnr;

public class ControlFodselsnummerDUFnummer {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, String fnr, String duf) {
        if (!Fnr.isValidNorwId(fnr) && !Fnr.isValidDUFnr(duf)){
            er.addEntry(ere);

            return true;
        }

        return false;
    }
}
