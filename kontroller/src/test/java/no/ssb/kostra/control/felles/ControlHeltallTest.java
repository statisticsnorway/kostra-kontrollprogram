package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControlHeltallTest {
    private ErrorReport errorReport;
    private ErrorReportEntry errorReportEntry;

    @Before
    public void beforeTest() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        errorReport = new ErrorReport(arguments);
        errorReportEntry = new ErrorReportEntry(" ", " ", " ", " ",
                "TEST av Heltall", "Feil i Heltall", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlHeltall.doControl(errorReport, errorReportEntry, 12345));
    }

    @Test
    public void testHeltallFail1() {
        assertTrue(ControlHeltall.doControl(errorReport, errorReportEntry, null));
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }
}
