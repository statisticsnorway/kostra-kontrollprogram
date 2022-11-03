package no.ssb.kostra.control.sosial.s11_sosialhjelp;

import no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad.FieldDefinitions;
import no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad.Main;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class SosialBlankFileTest {
    InputStream sysInBackup;
    private Arguments args;

    @BeforeEach
    public void beforeTest() {
        // Mocking a blank file
        String inputFileContent = "";

        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        FieldDefinitions.getFieldDefinitions();

        args = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400"});
    }

    @AfterEach
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    void testDoControl() {
        ErrorReport er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        Assertions.assertNotNull(er, "Has content ErrorReport");
        Assertions.assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
