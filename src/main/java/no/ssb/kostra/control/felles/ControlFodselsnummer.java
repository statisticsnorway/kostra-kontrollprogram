package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.utils.Fnr;

public class ControlFodselsnummer {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, String fieldValueSSN, int isFieldValueSSNOK) {
        return isFieldValueSSNOK == 1 && !Fnr.isValidNorwId(fieldValueSSN) && er.addEntry(ere);
    }
}
