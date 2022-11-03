package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ControlFelt1BoolskSaaFelt2BoolskTest {
    private ErrorReport er;
    private ErrorReportEntry ere;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Boolsk så boolsk", "Feil: fant ikke Boolsk så boolsk", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, ">", 0, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, ">=", 0, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, "<=", 12345, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, "==", 12345, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, ">=", 12345, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, "<", 99999, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, "<=", 99999, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, "!=", 99999, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, "!=", null, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, "<", 0, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, "==", 0, 12345, ">", 0));
        assertFalse(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, ">", 99999, 12345, ">", 0));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFelt1BoolskSaaFelt2Boolsk.doControl(er, ere, 12345, "==", 12345, 12345, "<", 0));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
