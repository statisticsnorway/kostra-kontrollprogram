package no.ssb.kostra.test.control.sensitiv.famvern_53;

import no.ssb.kostra.control.sensitiv.famvern_53.ControlTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 18.06.2018.
 */
public class ControlTimerTest {
    private ControlTimer t;
    private String ok1;
    private String ok2;
    private String f_1;

    @Before
    public void beforeTest() {
        t = new ControlTimer("K6", "Grupper for publikum", 51, 52);
        // ----00000000000000000000000000000000000000000000000
        // ----00000000011111111112222222222333333333344444444
        // ----12345678901234567890123456789012345678901234567
        ok1 = "04041050070020030080090030040040050030040010020";
        ok2 = "04041      020030080090030040040050030040010020";
        f_1 = "04041050   020030080090030040040050030040010020";
    }

    @Test
    public void testControlTimerOK1() {
        assertFalse(t.doControl(ok1, 1, "      ", ""));
    }

    @Test
    public void testControlTimerOK2() {
        assertFalse(t.doControl(ok2, 1, "      ", ""));
    }

    @Test
    public void testControlTimerFail() {
        assertTrue(t.doControl(f_1, 1, "      ", ""));
    }

    @Test
    public void titleTest() {
        assertTrue(t.getErrorText().equalsIgnoreCase("K6: Grupper for publikum, timer"));
    }

    @Test
    public void errorReportTest() {
        assertTrue(0 < t.getErrorReport(1).length());
    }

    @Test
    public void errorTypeTest() {
        assertTrue(1 == t.getErrorType());
    }

    @Test
    public void foundErrorTest() {
        assertFalse(t.foundError());
    }
}
