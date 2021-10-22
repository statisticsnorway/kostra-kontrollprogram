package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import static no.ssb.kostra.control.felles.Comparator.isValidDate;

public class ControlFelt1Dato {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, String date1, String datePattern1) {
        return (!isValidDate(date1, datePattern1) && er.addEntry(ere));
    }
}
