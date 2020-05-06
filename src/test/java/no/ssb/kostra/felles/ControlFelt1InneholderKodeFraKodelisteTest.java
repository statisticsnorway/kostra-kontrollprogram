package no.ssb.kostra.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodeliste;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ControlFelt1InneholderKodeFraKodelisteTest {
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private List<FieldDefinition> fieldDefinitions;
    private Record r;

    @Before
    public void beforeTest() {
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888", "-i", "src/test/resources/15F_V2019_R040200_OK.xml"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Kode i kodeliste", "Feil: fant ikke kode i kodeliste", Constants.CRITICAL_ERROR);
        fieldDefinitions = List.of(
                new FieldDefinition(1, "felt1", "String", "", 1, 2, new ArrayList<>(), "", false),
                new FieldDefinition(2, "felt2", "String", "", 3, 4, new ArrayList<>(), "", false),
                new FieldDefinition(3, "felt3", "String", "", 5, 5, new ArrayList<>(), "", false),
                new FieldDefinition(4, "heltall", "Integer", "", 6, 10, new ArrayList<>(), "", false)
        );
        r = new Record("F1F2 12345", fieldDefinitions);
    }

    @Test
    public void testOK() {
        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "felt1", List.of("F1"));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

    }

    @Test
    public void testFail() {
        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "felt1", List.of("FA"));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());

    }

    @Test
    public void testBlank1() {
        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "felt3", List.of(""));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());

    }

    @Test
    public void testBlank2() {
        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "felt3", List.of(" "));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

    }

}
