package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.List;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste {

    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String fieldValue1, final List<String> codeList1,
            final String fieldValue2, final List<String> codeList2) {

        return Comparator.isCodeInCodeList(fieldValue1, codeList1)
                && !Comparator.isCodeInCodeList(fieldValue2, codeList2)
                && errorReport.addEntry(errorReportEntry);
    }
}
