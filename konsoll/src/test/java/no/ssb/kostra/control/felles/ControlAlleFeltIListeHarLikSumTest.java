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

public class ControlAlleFeltIListeHarLikSumTest {
    private ErrorReport er;
    private ErrorReportEntry ere;
    private List<FieldDefinition> fieldDefinitions;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
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
    }

    @Test
    public void testOK1() {
        var sumList = List.of("sum_1", "sum_2", "sum_3");
        var r1 = new KostraRecord("F1F2 1 1 1", fieldDefinitions);
        ControlAlleFeltIListeHarLikSum.doControl(r1, er, ere, sumList);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testFAIL1() {
        var sumList = List.of("sum_1", "sum_2", "sum_3");
        var r2 = new KostraRecord("F1F2 1 2 3", fieldDefinitions);
        ControlAlleFeltIListeHarLikSum.doControl(r2, er, ere, sumList);
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
