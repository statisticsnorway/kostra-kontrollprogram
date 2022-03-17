package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.time.LocalDate;
import java.util.List;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato {

    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String fieldValue1, final List<String> codeList1, final LocalDate date2) {

        return Comparator.isCodeInCodeList(fieldValue1, codeList1)
                && date2 == null
                && errorReport.addEntry(errorReportEntry);
    }
}
