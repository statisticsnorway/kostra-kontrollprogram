package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.List;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk {

    public static boolean doControl(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final String fieldValue1,
            final List<String> codeList1,
            final Integer fieldValue2,
            final String operator2,
            final Integer checkValue2) {

        return Comparator.isCodeInCodeList(fieldValue1, codeList1)
                && !Comparator.compareIntegerOperatorInteger(fieldValue2, operator2, checkValue2)
                && errorReport.addEntry(errorReportEntry);
    }
}
