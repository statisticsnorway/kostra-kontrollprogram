package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControlFodselsnummerTest {
    private ErrorReport er;
    private ErrorReportEntry ere;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av FNR", "Feil i FNR", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlFodselsnummer.doControl(er, ere, "08011688153", 1));
        assertFalse(ControlFodselsnummer.doControl(er, ere, "41010150572", 1));
        assertFalse(ControlFodselsnummer.doControl(er, ere, "18011688153", 0));
        assertFalse(ControlFodselsnummer.doControl(er, ere, "11010150572", 0));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFodselsnummer.doControl(er, ere, "01010150590", 1));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testFail2() {
        assertTrue(ControlFodselsnummer.doControl(er, ere, "010101     ", 1));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
