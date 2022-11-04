package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControlFodselsnummerTest {
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
        assertFalse(ControlFodselsnummer.doControl(errorReport, errorReportEntry, "08011688153"));
        assertFalse(ControlFodselsnummer.doControl(errorReport, errorReportEntry, "41010150572"));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFodselsnummer.doControl(errorReport, errorReportEntry, "01010150590"));
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }

    @Test
    public void testFail2() {
        assertTrue(ControlFodselsnummer.doControl(errorReport, errorReportEntry, "010101     "));
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }
}
