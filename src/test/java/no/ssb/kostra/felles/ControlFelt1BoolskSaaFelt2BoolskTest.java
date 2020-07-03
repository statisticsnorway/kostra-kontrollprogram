package no.ssb.kostra.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFelt1BoolskSaaFelt2Boolsk;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ControlFelt1BoolskSaaFelt2BoolskTest {
    InputStream sysInBackup;
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private List<FieldDefinition> fieldDefinitions;
    private String inputFileContent;
    private Record r1;
    private Record r2;

    @Before
    public void beforeTest() {
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Kode i kodeliste", "Feil: fant ikke kode i kodeliste", Constants.CRITICAL_ERROR);
        fieldDefinitions = List.of(
                new FieldDefinition(1, "felt1", "String", "", 1, 2, new ArrayList<>(), "", false),
                new FieldDefinition(2, "felt2", "String", "", 3, 4, new ArrayList<>(), "", false),
                new FieldDefinition(3, "heltall1", "Integer", "", 5, 10, new ArrayList<>(), "", false),
                new FieldDefinition(4, "heltall2", "Integer", "", 11, 16, new ArrayList<>(), "", false)
        );
        inputFileContent = "F1F2 12345";
        r1 = new Record("F1F2 12345 12345", fieldDefinitions);
        r2 = new Record("F1F2 12345 99999", fieldDefinitions);
        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);
    }

    @After
    public void afterTest(){
        System.setIn(sysInBackup);
    }

    @Test
    public void testOK1() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", ">", 0, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK2() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", ">=", 0, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK3() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", "<=", 12345, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK4() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", "==", 12345, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK5() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", ">=", 12345, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK6() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", "<", 99999, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK7() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", "<=", 99999, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK8() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", "!=", 99999, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK9() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", "!=", null, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK10() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", "<", 0, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK11() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", "==", 0, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK12() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", ">", 99999, "heltall2", ">", 0);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testFail1() {
        ControlFelt1BoolskSaaFelt2Boolsk.doControl(r1, er, ere, "heltall1", "==", 12345, "heltall2", "<", 0);
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
