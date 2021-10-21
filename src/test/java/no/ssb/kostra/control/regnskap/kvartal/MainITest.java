package no.ssb.kostra.control.regnskap.kvartal;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Ignore
public class MainITest {
    InputStream sysInBackup;

    @Before
    public void beforeTest() {
        sysInBackup = System.in; // backup System.in to restore it later
    }

    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    public void testDoControl0AK3NoErrors() {
        //@formatter:off
        String inputFileContent =
                //00000000111111111122222222223333333333444444444
                //23456789012345678901234567890123456789012345678
                "0A20203303600                  0261 910        0\n" +
                        "0A20203303600                  1841 050      616\n" +
                        "0A20203303600                  1265 050        0\n" +
                        "0A20203303600                  1265 090       83\n" +
                        "0A20203303600                  1265 099       93\n" +
                        "0A20203303600                  1265 100        3\n" +
                        "0A20203303600                  1265 115        0\n" +
                        "0A20203303600                  1265 120        0\n" +
                        "0A20203303600                  1265 512        3\n" +
                        "0A20203303600                  1265 522        0\n" +
                        "0A20203303600                  1265 589        0\n" +
                        "0A20203303600                  1265 912        3\n" +
                        "0A20203303600                  1265 922        0\n" +
                        "0A20203303600                  1265 989        0\n" +
                        "0A20203303600                  1265 090       83\n" +
                        "0A20203303600                  1z   099       93\n" +
                        "0A20203303600                  1265 z          3";


        //@formatter:on

        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        Arguments args = new Arguments(new String[]{"-s", "0AK3", "-y", "2020", "-q", "3", "-r", "303600", "-n", "Nannestad Kommune"});

        ErrorReport er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        assertNotNull("Has content ErrorReport", er);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testDoControl0CK3NoErrors() {
        //@formatter:off
        String inputFileContent =
                //00000000111111111122222222223333333333444444444
                //23456789012345678901234567890123456789012345678
        "0C20203340000                  1561 010     1391\n" +
        "0C20203340000                  1561 040        2\n" +
        "0C20203340000                  0473 090  5823163\n" +
        "0C20203340000                  0472 529       63\n" +
        "0C20203340000                  0472 929       63\n" +
        "0C20203340000                  1534 010     1391\n" +
        "0C20203340000                  1535 040        2\n" +
        "0C20203340000                  0536 090  5823163\n" +
        "0C20203340000                  0537 529       63\n" +
        "0C20203340000                  0735 929       63\n" +
        "0C20203340000                  1561 512     1391\n" +
        "0C20203340000                  1561 522        2\n" +
        "0C20203340000                  0473 589  5823163\n" +
        "0C20203340000                  0472 912       63\n" +
        "0C20203340000                  0472 922       63\n" +
        "0C20203340000                  0472 989       63";

        //@formatter:on

        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        Arguments args = new Arguments(new String[]{"-s", "0AK3", "-y", "2020", "-q", "3", "-r", "340000", "-n", "Innlandet Fylkeskommune"});

        ErrorReport er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        assertNotNull("Has content ErrorReport", er);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }
}
