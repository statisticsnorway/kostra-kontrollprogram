package no.ssb.kostra.control.felles;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void createLinenumberTest() {
        // TODO
    }

    @Test
    public void getValidRecordsTest() {
        // TODO
    }

    @Test
    public void mergeFieldDefinitionsAndArgumentsTest() {
        // TODO
    }

    @Test
    public void rpadListTest() {
        assertEquals(List.of("1   ", "12  ", "123 ", "1234"), Utils.rpadList(List.of("1", "12", "123", "1234"), 4));
    }

    @Test
    public void replaceSpaceWithNoBreakingSpaceTest() {
        assertEquals("&nbsp;", Utils.replaceSpaceWithNoBreakingSpace(" "));
        assertEquals("&nbsp;&nbsp;", Utils.replaceSpaceWithNoBreakingSpace("  "));
        assertEquals("A", Utils.replaceSpaceWithNoBreakingSpace("A"));
        assertEquals("\t", Utils.replaceSpaceWithNoBreakingSpace("\t"));
    }
}
