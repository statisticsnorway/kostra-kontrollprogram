package no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainITest11CKvalifiseringBlankFile {
    InputStream sysInBackup;
    private Arguments args;


    @BeforeEach
    public void beforeTest() {
        sysInBackup = System.in; // backup System.in to restore it later
    }

    @AfterEach
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    public void testDoControl() {
        // Mocking a blank file
        String inputFileContent = "File with invalid content";
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        args = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400"});
        ErrorReport er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        assertNotNull("Has content ErrorReport", er);
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testDoControlWithNoFile() {
        // Mocking a blank file
        String inputFileContent = "";
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        args = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400", "-a", "0"});
        ErrorReport er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        assertNotNull("Has content ErrorReport", er);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testDoControlWithBlankFile() {
        // Mocking a blank file
        String inputFileContent = " ";
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        args = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400", "-a", "0"});
        args.getInputContentAsStringList();
        ErrorReport er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        assertNotNull("Has content ErrorReport", er);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testDoControlWithInvalidFile() {
        // Mocking a blank file
        String inputFileContent = "File with invalid content";
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        args = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400", "-a", "0"});
        ErrorReport er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        assertNotNull("Has content ErrorReport", er);
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
