package no.ssb.kostra.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFelt1LikSumAvListe;
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

public class ControlFelt1LikSumAvListeTest {
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
                , "TEST av Sumfelt er like summen av felt i liste", "Feil: Sumfeltet er forskjellig fra summen til feltene i liste", Constants.CRITICAL_ERROR);
        fieldDefinitions = List.of(
                new FieldDefinition(1, "felt1", "String", "", 1, 2, new ArrayList<>(), "", false),
                new FieldDefinition(2, "felt2", "String", "", 3, 4, new ArrayList<>(), "", false),
                new FieldDefinition(3, "heltall_1_1", "Integer", "", 5, 6, new ArrayList<>(), "", false),
                new FieldDefinition(4, "heltall_1_2", "Integer", "", 7, 8, new ArrayList<>(), "", false),
                new FieldDefinition(5, "heltall_1_S", "Integer", "", 9, 10, new ArrayList<>(), "", false),
                new FieldDefinition(6, "heltall_2_1", "Integer", "", 11, 12, new ArrayList<>(), "", false),
                new FieldDefinition(7, "heltall_2_2", "Integer", "", 13, 14, new ArrayList<>(), "", false),
                new FieldDefinition(8, "heltall_2_S", "Integer", "", 15, 16, new ArrayList<>(), "", false),
                new FieldDefinition(9, "heltall_S_1", "Integer", "", 17, 18, new ArrayList<>(), "", false),
                new FieldDefinition(10, "heltall_S_2", "Integer", "", 19, 20, new ArrayList<>(), "", false),
                new FieldDefinition(11, "heltall_S_S", "Integer", "", 21, 22, new ArrayList<>(), "", false)
        );
        inputFileContent = "F1F2 1 2 3 2 4 6 3 6 9";
        r1 = new Record("F1F2 1 2 3 2 4 6 3 6 9", fieldDefinitions);
        r2 = new Record("F1F2 1 2 3 2 4 6 3 619", fieldDefinitions);
        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);
    }


    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    public void createFieldListTest1() {
        List<List<String>> expected = List.of(
                List.of("heltall_S_S", "heltall_S_1", "heltall_S_2"),
                List.of("heltall_1_S", "heltall_1_1", "heltall_1_2"),
                List.of("heltall_2_S", "heltall_2_1", "heltall_2_2"),
                List.of("heltall_S_S", "heltall_1_S", "heltall_2_S"),
                List.of("heltall_S_1", "heltall_1_1", "heltall_2_1"),
                List.of("heltall_S_2", "heltall_1_2", "heltall_2_2"));
        List<List<String>> matrix = ControlFelt1LikSumAvListe.createFieldList("heltall", List.of("S", "1", "2"), List.of("S", "1", "2"));

        assertEquals(expected, matrix);
    }

    @Test
    public void createFieldListTest2() {
        List<List<String>> expected = List.of(
                List.of("S_S", "S_1", "S_2"),
                List.of("1_S", "1_1", "1_2"),
                List.of("2_S", "2_1", "2_2"),
                List.of("S_S", "1_S", "2_S"),
                List.of("S_1", "1_1", "2_1"),
                List.of("S_2", "1_2", "2_2"));
        List<List<String>> matrix = ControlFelt1LikSumAvListe.createFieldList(List.of("S", "1", "2"), List.of("S", "1", "2"));

        assertEquals(expected, matrix);
    }

    @Test
    public void testOK1() {
        List<List<String>> matrix = ControlFelt1LikSumAvListe.createFieldList("heltall", List.of("S", "1", "2"), List.of("S", "1", "2"));
        ControlFelt1LikSumAvListe.doControl(r1, er, ere, matrix);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testFAIL1() {
        List<List<String>> matrix = ControlFelt1LikSumAvListe.createFieldList("heltall", List.of("S", "1", "2"), List.of("S", "1", "2"));
        ControlFelt1LikSumAvListe.doControl(r2, er, ere, matrix);
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
