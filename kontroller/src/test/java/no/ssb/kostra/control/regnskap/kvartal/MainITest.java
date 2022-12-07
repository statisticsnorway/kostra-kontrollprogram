package no.ssb.kostra.control.regnskap.kvartal;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainITest {

    @Test
    public void testDoControl0AK3NoErrors() {
        //@formatter:off
        String inputFileContent =
                //00000000111111111122222222223333333333444444444
                //23456789012345678901234567890123456789012345678
                """
                        0A20203303600                  0261 910        0
                        0A20203303600                  1841 050      616
                        0A20203303600                  1265 050        0
                        0A20203303600                  1265 090       83
                        0A20203303600                  1265 099       93
                        0A20203303600                  1265 100        3
                        0A20203303600                  1265 115        0
                        0A20203303600                  1265 120        0
                        0A20203303600                  1265 512        3
                        0A20203303600                  1265 522        0
                        0A20203303600                  1265 589        0
                        0A20203303600                  1265 912        3
                        0A20203303600                  1265 922        0
                        0A20203303600                  1265 989        0
                        0A20203303600                  1265 090       83
                        0A20203303600                  1z   099       93
                        0A20203303600                  1265 z          3""";


        var byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        var arguments = new Arguments(
                new String[]{"-s", "0AK3", "-y", "2020", "-q", "3", "-r", "303600", "-n", "Nannestad Kommune"},
                byteArrayInputStream);

        var errorReport = Main.doControls(arguments);

        if (Constants.DEBUG) {
            System.out.print(errorReport.generateReport());
        }

        assertNotNull("Has content ErrorReport", errorReport);
        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());
    }

    @Test
    public void testDoControl0CK3NoErrors() {
        //@formatter:off
        String inputFileContent =
                //00000000111111111122222222223333333333444444444
                //23456789012345678901234567890123456789012345678
                """
                        0C20203340000                  1561 010     1391
                        0C20203340000                  1561 040        2
                        0C20203340000                  0473 090  5823163
                        0C20203340000                  0472 529       63
                        0C20203340000                  0472 929       63
                        0C20203340000                  1534 010     1391
                        0C20203340000                  1535 040        2
                        0C20203340000                  0536 090  5823163
                        0C20203340000                  0537 529       63
                        0C20203340000                  0735 929       63
                        0C20203340000                  1561 512     1391
                        0C20203340000                  1561 522        2
                        0C20203340000                  0473 589  5823163
                        0C20203340000                  0472 912       63
                        0C20203340000                  0472 922       63
                        0C20203340000                  0472 989       63""";

        //@formatter:on

        var byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));

        var arguments = new Arguments(new
                String[]{"-s", "0AK3", "-y", "2020", "-q", "3", "-r", "340000", "-n", "Innlandet Fylkeskommune"},
                byteArrayInputStream);

        var errorReport = Main.doControls(arguments);

        if (Constants.DEBUG) {
            System.out.print(errorReport.generateReport());
        }

        assertNotNull("Has content ErrorReport", errorReport);
        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());
    }

    @Test
    public void testDoControl0DK4WithErrors() {
        //@formatter:off
        String inputFileContent =
                //00000000111111111122222222223333333333444444444
                //23456789012345678901234567890123456789012345678
                """
                        0D20224340001                  215  000     2899
                        0D20224340000                  215  070   302276
                        0D20224340000                  215  080    40000
                        0D20224340000                  216  110    28888
                        0D20224340000                  235  161      438
                        0D20224340000                  234  152     2881
                        0D20224340000                  234  153    -7583
                        0D20224340000                  234  320      304
                        0D20224340000                  234  395       32
                        0D20224340000                  234  430       32
                        0D20224340000                  234  450       32
                        0D20224340000                  234  499       32
                        0D20224340000                  234  550       32
                        0D20224340000                  234  570       32
                        0D20224340000                  234  610       32
                        0D20224340000                  234  640       32""";

        //@formatter:on

        var byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));

        var arguments = new Arguments(new
                String[]{"-s", "0DK4", "-y", "2022", "-q", "4", "-r", "340000", "-n", "Innlandet Fylkeskommune"},
                byteArrayInputStream);

        var errorReport = Main.doControls(arguments);

        if (Constants.DEBUG) {
            System.out.print(errorReport.generateReport());
        }

        assertNotNull("Has content ErrorReport", errorReport);
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }
}
