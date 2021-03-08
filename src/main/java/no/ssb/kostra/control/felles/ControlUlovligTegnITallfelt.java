package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

public class ControlUlovligTegnITallfelt {
    public static boolean doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field) {
        if (r.getFieldAsString(field).split("\t").length != 1 || !r.getFieldAsString(field).matches("^\\s*?-?\\d+$")) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
            return true;
        }

        return false;
    }
}