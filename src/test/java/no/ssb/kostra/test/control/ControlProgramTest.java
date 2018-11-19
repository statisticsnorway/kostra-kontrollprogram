package no.ssb.kostra.test.control;

import no.ssb.kostra.control.ControlProgram;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 16.11.2018.
 */
public class ControlProgramTest {

    ControlProgram cp;
    String region;
    String kildefil;
    String rapportfil;
    String type_filuttrekk;
    String regnskapstype;
    String kontornummer;
    String orgnr;

    @Before
    public void beforeTest() {
        cp = new ControlProgram();
    }

    @Test
    public void testValidArguments() {
//-----------------------------------------------------------------------------------------------------------------
//    ----- Argument |    0      |     1       |       2       |     3     |        4                  |    5      |
//    ----- Skjema   |  region   |  kildefil   |  rapportfil   |  1 - 10   |  regnskapstype / Kontornr.|  org.nr.  |
//    ----- 0A - 0D  |    X      |    -        |       -       |     4     |        X                  |    0      |
//    ----- 0AK-0DK  |    X      |    -        |       -       |     4     |        X                  |    0      |
//    ----- 0F-0G	 |    X      |    -        |       -       |     4     |        X                  |    X      |
//    ----- 0I-0L    |    X      |    -        |       -       |     4     |        X                  |    X      |
//    ----- 0X-0Y    |    X      |    -        |       -       |     4     |        X                  |    X      |
//    ----- 52A      |    X      |    -        |       -       |     3     |                    X      |    X      |
//    ----- 52B      |    X      |    -        |       -       |     6     |                    X      |    X      |
//    ----- 53       |    X      |    -        |       -       |     8     |                    X      |    X      |
//    ----- 55       |    X      |    -        |       -       |     9     |                    X      |    X      |
//-----------------------------------------------------------------------------------------------------------------


        region = "050100";
        type_filuttrekk = "4";
        regnskapstype = "0J";
        orgnr = "987654321";

        String[] arguments = {region, type_filuttrekk, regnskapstype, orgnr};

        try {
            cp.parseArguments(arguments);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(region.equalsIgnoreCase(cp.getRegion_nr()));
    }
}
