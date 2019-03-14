package no.ssb.helseforetak.control.regnskap.regn0X;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 14.03.2019.
 */
public class ControlRegionTest {


    private ControlRegion t;
    private String ok1;

    @Before
    public void beforeTest() {
        t = new ControlRegion();
        // ----000000000000000000000000000000000000000000000000
        // ----000000000111111111122222222223333333333444444444
        // ----123456789012345678901234567890123456789012345678
        ok1 = "0Y2018 990000914637651              135        7";
    }


    @Test
    public void testRegionOK1() {
        assertFalse(t.doControl(ok1, 1, "990000", "914637651"));
    }

    @Test
    public void testRegionOK2() {
        assertFalse(t.doControl(ok1, 1, "99", "914637651"));
    }

    @Test
    public void testRegionFail1() {
        assertTrue(t.doControl(ok1, 1, "030000", "914637651"));
    }

}
