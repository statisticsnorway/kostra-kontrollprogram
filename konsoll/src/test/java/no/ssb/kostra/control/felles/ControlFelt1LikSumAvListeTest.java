package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.FieldDefinition;
import no.ssb.kostra.felles.KostraRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ControlFelt1LikSumAvListeTest {
    private ErrorReport er;
    private ErrorReportEntry ere;
    private KostraRecord r1;
    private KostraRecord r2;

    @Before
    public void beforeTest() {
        var args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Sumfelt er like summen av felt i liste", "Feil: Sumfeltet er forskjellig fra summen til feltene i liste", Constants.CRITICAL_ERROR);
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "felt1", "String", "", 1, 2, new ArrayList<>(), "", false),
                new FieldDefinition(2, "felt2", "String", "", 3, 4, new ArrayList<>(), "", false),
                new FieldDefinition(3, "heltall_R1_C1", "Integer", "", 5, 6, new ArrayList<>(), "", false),
                new FieldDefinition(4, "heltall_R1_C2", "Integer", "", 7, 8, new ArrayList<>(), "", false),
                new FieldDefinition(5, "heltall_R1_CS", "Integer", "", 9, 10, new ArrayList<>(), "", false),
                new FieldDefinition(6, "heltall_R2_C1", "Integer", "", 11, 12, new ArrayList<>(), "", false),
                new FieldDefinition(7, "heltall_R2_C2", "Integer", "", 13, 14, new ArrayList<>(), "", false),
                new FieldDefinition(8, "heltall_R2_CS", "Integer", "", 15, 16, new ArrayList<>(), "", false),
                new FieldDefinition(9, "heltall_RS_C1", "Integer", "", 17, 18, new ArrayList<>(), "", false),
                new FieldDefinition(10, "heltall_RS_C2", "Integer", "", 19, 20, new ArrayList<>(), "", false),
                new FieldDefinition(11, "heltall_RS_CS", "Integer", "", 21, 22, new ArrayList<>(), "", false)
        );
        r1 = new KostraRecord("F1F2 1 2 3 2 4 6 3 6 9", fieldDefinitions);
        r2 = new KostraRecord("F1F2 1 2 3 2 4 6 3 619", fieldDefinitions);
    }

    @Test
    public void createFieldListTest1() {
        var expected = List.of(
                List.of("heltall_RS_CS", "heltall_RS_C1", "heltall_RS_C2"),
                List.of("heltall_R1_CS", "heltall_R1_C1", "heltall_R1_C2"),
                List.of("heltall_R2_CS", "heltall_R2_C1", "heltall_R2_C2"),

                List.of("heltall_RS_CS", "heltall_R1_CS", "heltall_R2_CS"),
                List.of("heltall_RS_C1", "heltall_R1_C1", "heltall_R2_C1"),
                List.of("heltall_RS_C2", "heltall_R1_C2", "heltall_R2_C2")
        );

        var matrix = ControlFelt1LikSumAvListe.createFieldList("heltall", List.of("RS", "R1", "R2"), List.of("CS", "C1", "C2"));

        assertEquals(expected, matrix);
    }

    @Test
    public void createFieldListTest2() {
        var expected = List.of(
                List.of("RS_CS", "RS_C1", "RS_C2"),
                List.of("R1_CS", "R1_C1", "R1_C2"),
                List.of("R2_CS", "R2_C1", "R2_C2"),
                List.of("RS_CS", "R1_CS", "R2_CS"),
                List.of("RS_C1", "R1_C1", "R2_C1"),
                List.of("RS_C2", "R1_C2", "R2_C2")
        );

        var matrix = ControlFelt1LikSumAvListe.createFieldList(List.of("RS", "R1", "R2"), List.of("CS", "C1", "C2"));

        assertEquals(expected, matrix);
    }

    @Test
    public void testOK1() {
        var matrix = ControlFelt1LikSumAvListe.createFieldList("heltall", List.of("RS", "R1", "R2"), List.of("CS", "C1", "C2"));
        assertFalse(ControlFelt1LikSumAvListe.doControl(r1, er, ere, matrix));
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testFAIL1() {
        var matrix = ControlFelt1LikSumAvListe.createFieldList("heltall", List.of("RS", "R1", "R2"), List.of("CS", "C1", "C2"));
        assertTrue(ControlFelt1LikSumAvListe.doControl(r2, er, ere, matrix));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
