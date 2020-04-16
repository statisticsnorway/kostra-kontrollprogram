package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

public class ControlRecordLengde {
    public static Record doControl(Record p, ErrorReport er, ErrorReportEntry ere, int length) {
        if (p.getRecord().length() != length) {
            ere.setRefNr(String.valueOf(p.getLine()));
            er.addEntry(ere);
        }

        return p;
    }
}
