package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class ControlFelt1InneholderKodeFraKodeliste {
    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String fieldValue1, final List<String> codeList1) {

        return !Comparator.isCodeInCodeList(fieldValue1, codeList1)
                && errorReport.addEntry(errorReportEntry);
    }
}
