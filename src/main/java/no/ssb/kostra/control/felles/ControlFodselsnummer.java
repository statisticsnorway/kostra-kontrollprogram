package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;
import no.ssb.kostra.utils.DatoFnr;

public class ControlFodselsnummer {
    public static Record doControl(Record p, ErrorReport er, ErrorReportEntry ere, String fieldSSN) {
        boolean hasErrors;

        try {
            hasErrors = (DatoFnr.validNorwId(p.getFieldAsString(fieldSSN)) != 1);
        } catch (Exception e) {
            hasErrors = true;
        }

        if (hasErrors) {
            ere.setRefNr(String.valueOf(p.getLine()));
            er.addEntry(ere);
        }

        return p;
    }
}
