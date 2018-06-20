package no.ssb.kostra.test.control.sensitiv.famvern_53;

import no.ssb.kostra.control.sensitiv.famvern_53.ControlTiltak;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 18.06.2018.
 */
public class ControlTiltakTest {
    private ControlTiltak t;
    private String ok;
    private String f1;

    @Before
    public void beforeTest() {
        t = new ControlTiltak("K6", "Grupper for publikum", 51);
        // ---00000000000000000000000000000000000000000000000
        // ---00000000011111111112222222222333333333344444444
        // ---12345678901234567890123456789012345678901234567
        ok = "04041050070020030080090030040040050030040010020";
        f1 = "04041   070020030080090030040040050030040010020";
    }

    @Test
    public void titleTest() {
        assertTrue(t.getErrorText().equalsIgnoreCase("K6: Grupper for publikum, tiltak"));
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

    @Test
    public void okTest() {
        assertFalse(t.doControl(ok, 1, "      ", ""));
    }

    @Test
    public void failTest() {
        assertTrue(t.doControl(f1, 1, "      ", ""));
    }

}
