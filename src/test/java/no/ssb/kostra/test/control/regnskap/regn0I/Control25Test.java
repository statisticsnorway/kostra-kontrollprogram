package no.ssb.kostra.test.control.regnskap.regn0I;

import no.ssb.kostra.control.regnskap.regn0I.Control25;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 11.03.2019.
 */
public class Control25Test {
    Control25 t;
    List<String> okList;
    List<String> failList;

    @Before
    public void beforeTest() {
        t = new Control25();

// @formatter:off
//---------------000000000000000000000000000000000000000000000000
//---------------000000000111111111122222222223333333333444444444
//---------------123456789012345678901234567890123456789012345678
        okList = Arrays.asList(
                "0A2015 160100                  0800 075        5",
                "0A2015 160100                  0899 580        5",
                "0A2015 160100                  0899 589        5",
                "0A2015 160100                  0899 980        5",
                "0A2015 160100                  0899 989        5",
                "0A2015 160100                  1800 075        5",
                "0A2015 160100                  1899 580        5",
                "0A2015 160100                  1899 589        5",
                "0A2015 160100                  1899 980        5",
                "0A2015 160100                  1899 989        5");

        failList = Arrays.asList(
                "0A2015 160100                  0899 075        5",
                "0A2015 160100                  0100 580        5",
                "0A2015 160100                  0100 589        5",
                "0A2015 160100                  0100 980        5",
                "0A2015 160100                  0100 989        5",
                "0A2015 160100                  1899 075        5",
                "0A2015 160100                  1100 580        5",
                "0A2015 160100                  1100 589        5",
                "0A2015 160100                  1100 980        5",
                "0A2015 160100                  1100 989        5");
// @formatter:on
    }

    @Test
    public void testValidFunksjonOK() {
        okList.forEach((String item) -> assertFalse(t.doControl(item, 1, "      ", "")));
    }

    @Test
    public void testValidFunksjonFail() {
        failList.forEach((String item) -> assertTrue(t.doControl(item, 1, "      ", "")));
    }
}
