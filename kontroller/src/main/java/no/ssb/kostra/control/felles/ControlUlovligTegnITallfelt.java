package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

@SuppressWarnings("SpellCheckingInspection")
public class ControlUlovligTegnITallfelt {

    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry, final String fieldValue) {

        return (fieldValue.split("\t").length != 1
                || !fieldValue.matches("^\\s*?-?\\d+$")) && errorReport.addEntry(errorReportEntry);
    }
}
