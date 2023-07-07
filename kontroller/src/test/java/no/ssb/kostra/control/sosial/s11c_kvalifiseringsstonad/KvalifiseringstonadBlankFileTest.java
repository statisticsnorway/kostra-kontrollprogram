package no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KvalifiseringstonadBlankFileTest {
    private Arguments arguments;

    @Test
    void testDoControl() {
        // Mocking a blank file
        var inputFileContent = "File with invalid content";
        var byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        arguments = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400"}, byteArrayInputStream);

        var errorReport = Main.doControls(arguments);

        if (Constants.DEBUG) {
            System.out.print(errorReport.generateReport());
        }

        assertNotNull(errorReport, "Has content ErrorReport");
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }

    //@Test
    void testDoControlWithNoFile() {
        // Mocking a blank file
        var inputFileContent = "";
        var byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));

        arguments = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400", "-a", "0"},
                byteArrayInputStream);
        var errorReport = Main.doControls(arguments);

        if (Constants.DEBUG) {
            System.out.print(errorReport.generateReport());
        }

        assertNotNull(errorReport, "Has content ErrorReport");
        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());
    }

    //@Test
    void testDoControlWithBlankFile() {
        // Mocking a blank file
        var inputFileContent = " ";
        var byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));

        arguments = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400", "-a", "0"},
                byteArrayInputStream);

        arguments.getInputContentAsStringList();
        var errorReport = Main.doControls(arguments);

        if (Constants.DEBUG) {
            System.out.print(errorReport.generateReport());
        }

        assertNotNull(errorReport, "Has content ErrorReport");
        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());
    }

    @Test
    void testDoControlWithInvalidFile() {
        // Mocking a blank file
        var inputFileContent = "File with invalid content";
        var byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));

        arguments = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400", "-a", "0"},
                byteArrayInputStream);
        var errorReport = Main.doControls(arguments);

        if (Constants.DEBUG) {
            System.out.print(errorReport.generateReport());
        }

        assertNotNull(errorReport, "Has content ErrorReport");
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }
}
