package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.List;

public class ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste {

    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final Integer fieldvalue1, final String operator1, final Integer checkvalue1,
            final String fieldvalue2, final List<String> codeList2) {

        return Comparator.compareIntegerOperatorInteger(fieldvalue1, operator1, checkvalue1)
                && !Comparator.isCodeInCodeList(fieldvalue2, codeList2)
                && errorReport.addEntry(errorReportEntry);
    }
}
