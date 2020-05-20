package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

public class ControlFelt1Lengde {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1) {
        if (r.getFieldAsTrimmedString(field1).isEmpty()) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
        }

        return r;
    }
}
