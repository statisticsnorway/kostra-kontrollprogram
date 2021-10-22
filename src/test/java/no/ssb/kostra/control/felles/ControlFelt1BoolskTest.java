package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ControlFelt1BoolskTest {
    private ErrorReport er;
    private ErrorReportEntry ere;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av boolsk", "Feil: fant boolsk", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        ControlFelt1Boolsk.doControl(er, ere, 1, ">", 0);
    }

    @Test
    public void testFail1() {
        ControlFelt1Boolsk.doControl(er, ere, 1, "<", 0);
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
