package no.ssb.kostra.test.control.sensitiv.famvern_53;

import no.ssb.kostra.control.sensitiv.famvern_53.ControlKontornummer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 07.11.2018.
 */
public class ControlKontorNummerTest {
    private ControlKontornummer t;
    private List<String> kontorNumberList;

    private String ok1;
    private String ok2;
    private String ok3;
    private String ok4;
    private String ok5;
    private String f_1;

    @Before
    public void beforeTest() {
        t = new ControlKontornummer();
        // ----000000000000000000000000000000000000000000000000
        // ----000000000111111111122222222223333333333444444444
        // ----123456789012345678901234567890123456789012345678
        ok1 = "01016050070020030080090030040040050030040010020";
        ok2 = "03030050070020030080090030040040050030040010020";
        ok3 = "50162050070020030080090030040040050030040010020";
        ok4 = "50171050070020030080090030040040050030040010020";
        ok5 = "50172050070020030080090030040040050030040010020";
        f_1 = "50999050070020030080090030040040050030040010020";
    }

    @Test
    public void testOrgNummerOK1() {
        assertFalse(t.doControl(ok1, 1, "667600", ""));
    }

    @Test
    public void testOrgNummerOK2() {
        assertFalse(t.doControl(ok2, 1, "667600", ""));
    }

    @Test
    public void testOrgNummerOK3() {
        assertFalse(t.doControl(ok3, 1, "667600", ""));
    }

    @Test
    public void testOrgNummerOK4() {
        assertFalse(t.doControl(ok4, 1, "667600", ""));
    }

    @Test
    public void testOrgNummerOK5() {
        assertFalse(t.doControl(ok5, 1, "667600", ""));
    }

    @Test
    public void testOrgNummerFail1() {
        assertTrue(t.doControl(f_1, 1, "667600", ""));
    }



}