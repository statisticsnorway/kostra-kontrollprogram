package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

public class ControlFelt1Boolsk {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, String operator1, Integer value1) {
        if (!Comparator.compareIntegerOperatorInteger(r.getFieldAsInteger(field1), operator1, value1)) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
        }

        return r;
    }
}
