package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControlFelt1LengdeTest {
    private ErrorReport errorReport;
    private ErrorReportEntry errorReportEntry;

    @Before
    public void beforeTest() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        errorReport = new ErrorReport(arguments);
        errorReportEntry = new ErrorReportEntry(" ", " ", " ", " ",
                "TEST av blankt innhold", "Feil: fant ikke noe innhold", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlFelt1Lengde.doControl(errorReport, errorReportEntry, "har innhold"));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFelt1Lengde.doControl(errorReport, errorReportEntry, ""));
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }
}
