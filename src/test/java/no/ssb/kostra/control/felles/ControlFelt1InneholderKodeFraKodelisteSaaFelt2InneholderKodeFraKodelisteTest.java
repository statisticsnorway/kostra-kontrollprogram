package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodelisteTest {
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
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, "Code1 in list", List.of("Code1 NOT in list"), "Code2 NOT in list", List.of("Code2 in list")));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, "Code1 in list", List.of("Code1 NOT in list"), "Code2 in list", List.of("Code2 in list")));
        assertFalse(ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, "Code1 in list", List.of("Code1 in list"), "Code2 in list", List.of("Code2 in list")));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, "Code1 in list", List.of("Code1 in list"), "Code2 NOT in list", List.of("Code2 in list")));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
