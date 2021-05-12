package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;
import no.ssb.kostra.utils.Fnr;

public class ControlFodselsnummer {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String fieldSSN) {
        boolean hasErrors;

        try {
            hasErrors = (Fnr.validNorwId(r.getFieldAsString(fieldSSN)) != 1);
        } catch (Exception e) {
            hasErrors = true;
        }

        if (hasErrors) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
        }

        return r;
    }
}
