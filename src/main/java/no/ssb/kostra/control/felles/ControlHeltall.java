package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

public class ControlHeltall {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, Integer value1) {
        return (value1 == null && er.addEntry(ere));
    }
}
