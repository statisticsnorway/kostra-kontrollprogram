package no.ssb.kostra.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlAlleFeltIListeHarLikSum;
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

public class ControlAlleFeltIListeHarLikSumTest {
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
                , "TEST av alle felt i liste har like sum", "Feil: Alle summene er ikke identiske", Constants.CRITICAL_ERROR);
        fieldDefinitions = List.of(
                new FieldDefinition(1, "felt1", "String", "", 1, 2, new ArrayList<>(), "", false),
                new FieldDefinition(2, "felt2", "String", "", 3, 4, new ArrayList<>(), "", false),
                new FieldDefinition(3, "sum_1", "Integer", "", 5, 6, new ArrayList<>(), "", false),
                new FieldDefinition(4, "sum_2", "Integer", "", 7, 8, new ArrayList<>(), "", false),
                new FieldDefinition(5, "sum_3", "Integer", "", 9, 10, new ArrayList<>(), "", false)
        );
        inputFileContent = "F1F2 1 1 1";
        r1 = new Record("F1F2 1 1 1", fieldDefinitions);
        r2 = new Record("F1F2 1 2 3", fieldDefinitions);
        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);
    }

    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    public void testOK1() {
        List<String> sumList = List.of("sum_1", "sum_2", "sum_3");
        ControlAlleFeltIListeHarLikSum.doControl(r1, er, ere, sumList);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testFAIL1() {
        List<String> sumList = List.of("sum_1", "sum_2", "sum_3");
        ControlAlleFeltIListeHarLikSum.doControl(r2, er, ere, sumList);
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
