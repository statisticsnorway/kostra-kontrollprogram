package no.ssb.kostra.test.control.regnskap.regn0L;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class MemoriakontiTest {
    @Test
    public void testListContains() {
        assertTrue(Arrays.asList(9100, 9110, 9200, 9999).contains(9110));
    }

    @Test
    public void testListNotContains() {
        assertFalse(Arrays.asList(9100, 9110, 9200, 9999).contains(8888));
    }

}
