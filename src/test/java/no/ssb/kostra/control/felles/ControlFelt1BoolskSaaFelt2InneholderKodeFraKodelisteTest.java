package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ControlFelt1BoolskSaaFelt2InneholderKodeFraKodelisteTest {
    private ErrorReport er;
    private ErrorReportEntry ere;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av boolsk så Kode i kodeliste"
                , "Feil: fant ikke kode i kodeliste"
                , Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, ">", 0, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, ">=", 0, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, "<=", 12345, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, "==", 12345, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, ">=", 12345, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, "<", 99999, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, "<=", 99999, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, "!=", 99999, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, "!=", null, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, "<", 0, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, "==", 0, "code1", List.of("code1", "code2")));
        assertFalse(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, ">", 99999, "code1", List.of("code1", "code2")));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(er, ere, 12345, "==", 12345, "codeNotInCodelist", List.of("code1", "code2")));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
