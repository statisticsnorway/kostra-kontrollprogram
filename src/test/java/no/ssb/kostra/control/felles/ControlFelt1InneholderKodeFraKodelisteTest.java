package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ControlFelt1InneholderKodeFraKodelisteTest {
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;

    @Before
    public void beforeTest() {
        args = new Arguments(new String[]{"-s", "0A", "-y", "2020", "-r", "030100"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Kode i kodeliste", "Feil: fant ikke kode i kodeliste", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK() {
        assertFalse(ControlFelt1InneholderKodeFraKodeliste.doControl(er, ere, "1", List.of("0", "1")));
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testFail() {
        assertTrue(ControlFelt1InneholderKodeFraKodeliste.doControl(er, ere, "0", List.of("1", "2")));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }


    @Test
    public void testBlank1() {
        assertFalse(ControlFelt1InneholderKodeFraKodeliste.doControl(er, ere, "", List.of("")));
    }
}
