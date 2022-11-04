package no.ssb.kostra.control.sosial.s11_sosialhjelp;

import no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad.FieldDefinitions;
import no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad.Main;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

class SosialBlankFileTest {
    private Arguments arguments;

    @BeforeEach
    public void beforeTest() {
        // Mocking a blank file
        var inputFileContent = "";

        FieldDefinitions.getFieldDefinitions();
        var byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        arguments = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400"}, byteArrayInputStream);
    }

    @Test
    void testDoControl() {
        var errorReport = Main.doControls(arguments);

        if (Constants.DEBUG) {
            System.out.print(errorReport.generateReport());
        }

        Assertions.assertNotNull(errorReport, "Has content ErrorReport");
        Assertions.assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }
}
