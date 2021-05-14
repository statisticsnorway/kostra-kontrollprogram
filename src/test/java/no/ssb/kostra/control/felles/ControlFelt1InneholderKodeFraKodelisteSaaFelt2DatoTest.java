package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2DatoTest {
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
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(er, ere, "code1", List.of("NOT IN LIST"), null));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(er, ere, "code1", List.of("NOT IN LIST"), LocalDate.of(2020, 1, 1)));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(er, ere, "code1", List.of("code1", "code2"), LocalDate.of(2020, 1, 1)));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(er, ere, "code1", List.of("code1", "code2"), null));
        assertEquals(er.getErrorType(), Constants.CRITICAL_ERROR);
    }

}
