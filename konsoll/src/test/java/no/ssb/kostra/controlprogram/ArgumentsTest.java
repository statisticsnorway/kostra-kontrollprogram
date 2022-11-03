package no.ssb.kostra.controlprogram;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ArgumentsTest {
    InputStream sysInBackup;
    private Arguments args;
    private String inputFileContent;

    @Before
    public void beforeTest() {
        inputFileContent = """
                0A20194040200                  1120 010    36328
                0A20194040200                  1121 010     4306
                0A20194040200                  1130 010      864""";

        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);
    }

    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }


    @Test
    public void ReadFileFromStdin() {
        try {
            args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"});

            String result = String.join("\n", args.getInputContentAsStringList());
            assertEquals(result.length(), inputFileContent.length());
            assertTrue(result.equalsIgnoreCase(inputFileContent));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testArguments() {
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"});

        assertTrue(args.getSkjema().equalsIgnoreCase("Test"));
        assertTrue(args.getAargang().equalsIgnoreCase("9999"));
        assertTrue(args.getRegion().equalsIgnoreCase("000000"));

        // test default values
        assertTrue(args.getKvartal().equalsIgnoreCase(" "));
        assertTrue(args.getNavn().equalsIgnoreCase("Uoppgitt"));
        assertTrue(args.getOrgnr().equalsIgnoreCase("         "));
        assertTrue(args.getForetaknr().equalsIgnoreCase("         "));
        assertFalse(args.isRunAsExternalProcess());
        assertTrue(args.getNewline().equalsIgnoreCase("\n"));
    }
    @Test
    public void testAllArgumentsAreSet() {
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-q", "1", "-r", "000000", "-n", "Name", "-u", "123456789", "-c", "987654321", "-a", "0", "-e", "1"});

        assertTrue(args.getSkjema().equalsIgnoreCase("Test"));
        assertTrue(args.getAargang().equalsIgnoreCase("9999"));
        assertTrue(args.getRegion().equalsIgnoreCase("000000"));

        // test set values
        assertTrue(args.getKvartal().equalsIgnoreCase("1"));
        assertTrue(args.getNavn().equalsIgnoreCase("Name"));
        assertTrue(args.getOrgnr().equalsIgnoreCase("123456789"));
        assertTrue(args.getForetaknr().equalsIgnoreCase("987654321"));
        assertFalse(args.harVedlegg());
        assertTrue(args.isRunAsExternalProcess());
        assertTrue(args.getNewline().equalsIgnoreCase(""));
    }

    @Test
    public void testAllArgumentsAreSetToDefaultValues() {
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-q", " ", "-r", "000000", "-n", "Uoppgitt", "-u", "         ", "-c", "         ", "-a", "1", "-e", "0"});

        assertTrue(args.getSkjema().equalsIgnoreCase("Test"));
        assertTrue(args.getAargang().equalsIgnoreCase("9999"));
        assertTrue(args.getRegion().equalsIgnoreCase("000000"));

        // test set values
        assertTrue(args.getKvartal().equalsIgnoreCase(" "));
        assertTrue(args.getNavn().equalsIgnoreCase("uoppgitt"));
        assertTrue(args.getOrgnr().equalsIgnoreCase("         "));
        assertTrue(args.getForetaknr().equalsIgnoreCase("         "));
        assertTrue(args.harVedlegg());
        assertFalse(args.isRunAsExternalProcess());
        assertTrue(args.getNewline().equalsIgnoreCase("\n"));
    }

    @Test
    public void testMissingArguments() {
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> args = new Arguments(new String[]{"-y", "9999", "-r", "000000"}));

        String expectedMessage1 = "parameter for skjema er ikke definert. Bruk -s SS. F.eks. -s 0A";
        String actualMessage1 = e1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception e2 = assertThrows(IllegalArgumentException.class, () -> args = new Arguments(new String[]{"-s", "Test", "-r", "000000"}));

        String expectedMessage2 = "parameter for årgang er ikke definert. Bruk -y YYYY. F.eks. -y 2020";
        String actualMessage2 = e2.getMessage();

        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception e3 = assertThrows(IllegalArgumentException.class, () -> args = new Arguments(new String[]{"-s", "Test", "-y", "9999"}));

        String expectedMessage3 = "parameter for region er ikke definert. Bruk -r RRRRRR. F.eks. -r 030100";
        String actualMessage3 = e3.getMessage();

        assertTrue(actualMessage3.contains(expectedMessage3));

    }

}
