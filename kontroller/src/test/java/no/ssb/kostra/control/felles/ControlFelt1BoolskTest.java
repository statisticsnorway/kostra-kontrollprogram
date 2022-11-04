package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ControlFelt1BoolskTest {
    private ErrorReport errorReport;
    private ErrorReportEntry errorReportEntry;

    @Before
    public void beforeTest() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        errorReport = new ErrorReport(arguments);
        errorReportEntry = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av boolsk", "Feil: fant boolsk", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        ControlFelt1Boolsk.doControl(errorReport, errorReportEntry, 1, ">", 0);
    }

    @Test
    public void testFail1() {
        ControlFelt1Boolsk.doControl(errorReport, errorReportEntry, 1, "<", 0);
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }
}
