package no.ssb.kostra.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodelisteTest {
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private List<FieldDefinition> fieldDefinitions;
    private String inputFileContent;
    private Record r;
    InputStream sysInBackup;


    @Before
    public void beforeTest() {
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888", "-i", "src/test/resources/15F_V2019_R040200_OK.xml"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Kode i kodeliste", "Feil: fant ikke kode i kodeliste", Constants.CRITICAL_ERROR);
        fieldDefinitions = List.of(
                new FieldDefinition(1, "felt1", "String", "", 1, 2, new ArrayList<>(), "", false),
                new FieldDefinition(2, "felt2", "String", "", 3, 4, new ArrayList<>(), "", false),
                new FieldDefinition(3, "heltall", "Integer", "", 5, 10, new ArrayList<>(), "", false)
        );
        inputFileContent = "F1F2 12345";
        r = new Record("F1F2 12345", fieldDefinitions);
        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

    }

    @Test
    public void testOK1() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(r, er, ere, "felt1", List.of("F1","C2"), "felt2", List.of("F2"));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

    }

    @Test
    public void testOK2() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(r, er, ere, "felt1", List.of("NOT IN LIST"), "felt2", List.of("F2"));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

    }

    @Test
    public void testFail1() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(r, er, ere, "felt1", List.of("F1"), "felt2", List.of("FAIL"));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());

    }

    @Test
    public void testFail2() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(r, er, ere, "felt1", List.of("NOT IN LIST"), "felt2", List.of("FAIL"));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

    }
}
