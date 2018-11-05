package no.ssb.kostra.test.control.regnskap.regn0A;

import no.ssb.kostra.control.regnskap.regn0A.Control24;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 05.11.2018.
 */
public class Control24Test {
    private Control24 t;
    private String ok1;
    private String ok2;
    private String ok3;
    private String f_1;
    private String f_2;
    private String f_3;

    @Before
    public void beforeTest() {
        t = new Control24();
        // ----000000000000000000000000000000000000000000000000
        // ----000000000111111111122222222223333333333444444444
        // ----123456789012345678901234567890123456789012345678
        ok1 = "0A2018 030100                  1899 580        5";
        ok2 = "0A2018 160100                  1899 980        5";
        ok3 = "0A2018 160100                  1400 075        5";
        f_1 = "0A2018 094100                  1899 075        5";
        f_2 = "0A2018 010100                  1400 580        5";
        f_3 = "0A2018 060200                  1302 980        5";
    }


    @Test
    public void testFunksjonArtOK1() {
        assertFalse(t.doControl(ok1, 1, "030100", ""));
    }

    @Test
    public void testFunksjonArtOK2() {
        assertFalse(t.doControl(ok2, 1, "030100", ""));
    }

    @Test
    public void testFunksjonArtOK3() {
        assertFalse(t.doControl(ok3, 1, "030100", ""));
    }

    @Test
    public void testFunksjonArtFail1() {
        assertTrue(t.doControl(f_1, 1, "030100", ""));
    }

    @Test
    public void testFunksjonArtFail2() {
        assertTrue(t.doControl(f_2, 1, "030100", ""));
    }

    @Test
    public void testFunksjonArtFail3() {
        assertTrue(t.doControl(f_3, 1, "030100", ""));
    }
}
