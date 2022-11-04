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
    private ErrorReport errorReport;
    private ErrorReportEntry errorReportEntry;

    @Before
    public void beforeTest() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        errorReport = new ErrorReport(arguments);
        errorReportEntry = new ErrorReportEntry(" ", " ", " ", " ",
                "TEST av Kode i kodeliste", "Feil: fant ikke kode i kodeliste",
                Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(errorReport, errorReportEntry, "felt1", List.of("NOT IN LIST"), 1, "<", 0));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(errorReport, errorReportEntry, "felt1", List.of("NOT IN LIST"), 0, "<", 1));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(errorReport, errorReportEntry, "felt1", List.of("NOT IN LIST"), 0, "==", 1));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(errorReport, errorReportEntry, "F1", List.of("F1"), 0, "<", 1));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(errorReport, errorReportEntry, "F1", List.of("F1"), 1, ">", 0));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(errorReport, errorReportEntry, "F1", List.of("F1"), 1, "==", 1));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(errorReport, errorReportEntry, "F1", List.of("F1"), null, "<", 0));
        assertEquals(errorReport.getErrorType(), Constants.CRITICAL_ERROR);
    }

    @Test
    public void testFail2() {
        assertTrue(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(errorReport, errorReportEntry, "F1", List.of("F1"), 1, "==", 0));
        assertEquals(errorReport.getErrorType(), Constants.CRITICAL_ERROR);
    }

    @Test
    public void testFail3() {
        assertTrue(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(errorReport, errorReportEntry, "F1", List.of("F1"), 0, ">", 1));
        assertEquals(errorReport.getErrorType(), Constants.CRITICAL_ERROR);
    }

}
