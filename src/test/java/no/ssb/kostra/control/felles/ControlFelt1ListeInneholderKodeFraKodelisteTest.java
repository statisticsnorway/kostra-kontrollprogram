package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.List.of;
import static org.junit.Assert.*;

public class ControlFelt1ListeInneholderKodeFraKodelisteTest {
    private final String controlCategoriTitle = "Kontrolkategori";
    private final String title = "navn på felt";
    private ErrorReport errorReport;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        errorReport = new ErrorReport(args);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport, controlCategoriTitle, title, "Fant ugyldig kode (%s) i liste", of("1", "2"), of("0", "1", "2", "3"), Constants.NO_ERROR));
        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport, controlCategoriTitle, title, "Fant ugyldig kode (%s) i liste", of("0", "2", "4", "4"), of("1", "2", "3"), Constants.CRITICAL_ERROR));
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
        System.out.println(errorReport.generateReport());
    }

    @Test
    public void testFail2() {
        List<String> felt1Liste = of("0", "2", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4"
                , "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4"
                , "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4"
                , "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4"
        );
        assertTrue(ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport, controlCategoriTitle, title, "Fant ugyldig kode (%s) i liste", felt1Liste, of("1", "2", "3"), Constants.CRITICAL_ERROR));
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
        System.out.println(errorReport.generateReport());
    }
}
