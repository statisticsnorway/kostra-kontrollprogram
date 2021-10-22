package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

public class ControlUlovligTegnITallfelt {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, String fieldvalue) {
        return ((fieldvalue.split("\t").length != 1 || !fieldvalue.matches("^\\s*?-?\\d+$")) && er.addEntry(ere));
    }
}
