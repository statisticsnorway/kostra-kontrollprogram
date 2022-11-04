package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2BoolskTest {
    private ErrorReport er;
    private ErrorReportEntry ere;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Kode i kodeliste", "Feil: fant ikke kode i kodeliste", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(er, ere, "felt1", List.of("NOT IN LIST"), 1, "<", 0));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(er, ere, "felt1", List.of("NOT IN LIST"), 0, "<", 1));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(er, ere, "felt1", List.of("NOT IN LIST"), 0, "==", 1));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(er, ere, "F1", List.of("F1"), 0, "<", 1));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(er, ere, "F1", List.of("F1"), 1, ">", 0));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(er, ere, "F1", List.of("F1"), 1, "==", 1));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(er, ere, "F1", List.of("F1"), null, "<", 0));
        assertEquals(er.getErrorType(), Constants.CRITICAL_ERROR);
    }

    @Test
    public void testFail2() {
        assertTrue(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(er, ere, "F1", List.of("F1"), 1, "==", 0));
        assertEquals(er.getErrorType(), Constants.CRITICAL_ERROR);
    }

    @Test
    public void testFail3() {
        assertTrue(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(er, ere, "F1", List.of("F1"), 0, ">", 1));
        assertEquals(er.getErrorType(), Constants.CRITICAL_ERROR);
    }

}
