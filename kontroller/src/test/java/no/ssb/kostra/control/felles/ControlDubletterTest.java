package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.FieldDefinition;
import no.ssb.kostra.felles.KostraRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ControlDubletterTest {
    private ErrorReport er;
    private List<FieldDefinition> fieldDefinitions;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        fieldDefinitions = List.of(
                new FieldDefinition(1, "KeyA", "String", "", 1, 5, new ArrayList<>(), "", true),
                new FieldDefinition(2, "ValueA", "String", "", 6, 6, new ArrayList<>(), "", true),
                new FieldDefinition(3, "KeyB", "String", "", 8, 12, new ArrayList<>(), "", true),
                new FieldDefinition(4, "ValueB", "String", "", 13, 13, new ArrayList<>(), "", true)
        );
    }

    @Test
    public void hasDuplicates() {
        var recordList = Stream.of("KeyA:1,KeyB:2", "KeyA:1,KeyB:1", "KeyA:1,KeyB:2")
                .map(p -> new KostraRecord(p, fieldDefinitions))
                .toList();

        var hasDuplicates = ControlDubletter.doControl(recordList, er, List.of("ValueA", "ValueB"), List.of("A", "B"));
        assertTrue(hasDuplicates);
        assertEquals(Constants.NORMAL_ERROR, er.getErrorType());
    }

    @Test
    public void hasNoDuplicates() {
        var recordList = Stream.of("KeyA:1,KeyB:2", "KeyA:1,KeyB:1", "KeyA:2,KeyB:2")
                .map(p -> new KostraRecord(p, fieldDefinitions))
                .toList();

        assertFalse(ControlDubletter.doControl(recordList, er, List.of("ValueA", "ValueB"), List.of("A", "B")));
    }
}
