package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControlFodselsnummerDUFnummerTest {
    private ErrorReport errorReport;
    private ErrorReportEntry errorReportEntry;

    @Before
    public void beforeTest() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        errorReport = new ErrorReport(arguments);
        errorReportEntry = new ErrorReportEntry(" ", " ", " ", " ",
                "TEST av FNR", "Feil i FNR", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlFodselsnummerDUFnummer.doControl(errorReport, errorReportEntry, "01010150589", ""));
        assertFalse(ControlFodselsnummerDUFnummer.doControl(errorReport, errorReportEntry, "41010150572", ""));
        assertFalse(ControlFodselsnummerDUFnummer.doControl(errorReport, errorReportEntry, "01010150590", "201212345603"));
        assertFalse(ControlFodselsnummerDUFnummer.doControl(errorReport, errorReportEntry, "01011299999", "            "));
        assertFalse(ControlFodselsnummerDUFnummer.doControl(errorReport, errorReportEntry, "010101     ", "            "));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFodselsnummerDUFnummer.doControl(errorReport, errorReportEntry, "010101     ", " 6            "));
        assertTrue(ControlFodselsnummerDUFnummer.doControl(errorReport, errorReportEntry, "01011299999", "201234567890"));
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }
}
