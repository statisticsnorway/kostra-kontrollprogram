package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

public class ControlHeltall {
    public static Record doControl(Record p, ErrorReport er, ErrorReportEntry ere, String field) {
        Integer i = p.getFieldAsInteger(field);

        if (i == null) {
            ere.setRefNr(String.valueOf(p.getLine()));
            er.addEntry(ere);
        }

        return p;
    }
}
