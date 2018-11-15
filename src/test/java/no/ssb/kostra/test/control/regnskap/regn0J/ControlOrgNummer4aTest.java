package no.ssb.kostra.test.control.regnskap.regn0J;

import no.ssb.kostra.control.regnskap.regn0J.ControlOrgNummer4a;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 07.11.2018.
 */
public class ControlOrgNummer4aTest {


    private ControlOrgNummer4a t;
    private String ok1;
    private String ok2;
    private String ok3;
    private String f_1;
    private String f_2;
    private String f_3;

    @Before
    public void beforeTest() {
        t = new ControlOrgNummer4a();
        // ----000000000000000000000000000000000000000000000000
        // ----000000000111111111122222222223333333333444444444
        // ----123456789012345678901234567890123456789012345678
        ok1 = "0J2018 090600970541012         515  000      264";
        ok2 = "0J2018 090600970541012         510  320      264";
        ok3 = "0J2018 090600970541012         510  080       65";
        f_1 = "0J2018 0906000                 510  650      232";
        f_2 = "0J2018 09060000000000          513  070      475";
        f_3 = "0J2018 090600                  511  650       38";
    }


    @Test
    public void testOrgNummerOK() {
        assertFalse(t.doControl(ok1, 1, "030100", "970541012"));
        assertFalse(t.doControl(ok2, 1, "030100", "970541012"));
        assertFalse(t.doControl(ok3, 1, "030100", "970541012"));
        assertTrue(t.doControl(f_1, 1, "030100", "970541012"));
        assertTrue(t.doControl(f_2, 1, "030100", "970541012"));
        assertTrue(t.doControl(f_3, 1, "030100", "970541012"));
    }

    @Test
    public void testOrgNummerFail() {
        assertFalse(t.doControl(f_1, 1, "030100", "970541012"));
        assertTrue(t.doControl(f_2, 1, "030100", "970541012"));
        assertTrue(t.doControl(f_3, 1, "030100", "970541012"));
        assertTrue(t.doControl(ok1, 1, "030100", "970541012"));
        assertTrue(t.doControl(ok2, 1, "030100", "970541012"));
        assertTrue(t.doControl(ok3, 1, "030100", "970541012"));
    }

}
