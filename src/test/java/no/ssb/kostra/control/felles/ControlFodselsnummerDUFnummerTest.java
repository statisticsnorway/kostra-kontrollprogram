package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControlFodselsnummerDUFnummerTest {
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
        assertFalse(ControlFodselsnummerDUFnummer.doControl(er, ere, "01010150589", ""));
        assertFalse(ControlFodselsnummerDUFnummer.doControl(er, ere, "41010150572", ""));
        assertFalse(ControlFodselsnummerDUFnummer.doControl(er, ere, "01010150590", "201212345603"));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFodselsnummerDUFnummer.doControl(er, ere, "01011299999", "201234567890"));
        assertTrue(ControlFodselsnummerDUFnummer.doControl(er, ere, "01011299999", "            "));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testFail2() {
        assertTrue(ControlFodselsnummerDUFnummer.doControl(er, ere, "010101     ", ""));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
