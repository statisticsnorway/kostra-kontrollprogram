package no.ssb.kostra.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ControlFelt1InneholderKodeFraKodelisteSaaFelt2BoolskTest {
    InputStream sysInBackup;
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private List<FieldDefinition> fieldDefinitions;
    private String inputFileContent;
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
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(r, er, ere, "felt1", List.of("NOT IN LIST"), "heltall", "<", 0);
        assertTrue(er.getErrorType() == Constants.NO_ERROR);
    }

    @Test
    public void testOK2() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(r, er, ere, "felt1", List.of("NOT IN LIST"), "heltall", "<", 99999);
        assertTrue(er.getErrorType() == Constants.NO_ERROR);

    }

    @Test
    public void testOK3() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(r, er, ere, "felt1", List.of("NOT IN LIST"), "heltall", "==", 99999);
        assertTrue(er.getErrorType() == Constants.NO_ERROR);

    }

    @Test
    public void testOK4() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(r, er, ere, "felt1", List.of("F1"), "heltall", "<", 99999);
        assertTrue(er.getErrorType() == Constants.NO_ERROR);
    }

    @Test
    public void testOK5() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(r, er, ere, "felt1", List.of("F1"), "heltall", ">", 0);
        assertTrue(er.getErrorType() == Constants.NO_ERROR);

    }

    @Test
    public void testOK6() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(r, er, ere, "felt1", List.of("F1"), "heltall", "==", 12345);
        assertTrue(er.getErrorType() == Constants.NO_ERROR);

    }

    @Test
    public void testFail1() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(r, er, ere, "felt1", List.of("F1"), "heltall", "<", 0);
        assertTrue(er.getErrorType() == Constants.CRITICAL_ERROR);
    }

    @Test
    public void testFail2() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(r, er, ere, "felt1", List.of("F1"), "heltall", "==", 0);
        assertTrue(er.getErrorType() == Constants.CRITICAL_ERROR);
    }

    @Test
    public void testFail3() {
        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(r, er, ere, "felt1", List.of("F1"), "heltall", ">", 99999);
        assertTrue(er.getErrorType() == Constants.CRITICAL_ERROR);
    }

}
