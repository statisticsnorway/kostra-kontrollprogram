package no.ssb.kostra.controlprogram;

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
    InputStream inputStream;
    private Arguments arguments;
    private String inputFileContent;

    @Before
    public void beforeTest() {
        inputFileContent = """
                0A20194040200                  1120 010    36328
                0A20194040200                  1121 010     4306
                0A20194040200                  1130 010      864""";

        inputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
    }

    @Test
    public void ReadFileFromStdin() {
        try {
            arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"}, inputStream);

            var result = String.join("\n", arguments.getInputContentAsStringList());
            assertEquals(result.length(), inputFileContent.length());
            assertTrue(result.equalsIgnoreCase(inputFileContent));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testArguments() {
        arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"}, inputStream);

        assertTrue(arguments.getSkjema().equalsIgnoreCase("Test"));
        assertTrue(arguments.getAargang().equalsIgnoreCase("9999"));
        assertTrue(arguments.getRegion().equalsIgnoreCase("000000"));

        // test default values
        assertTrue(arguments.getKvartal().equalsIgnoreCase(" "));
        assertTrue(arguments.getNavn().equalsIgnoreCase("Uoppgitt"));
        assertTrue(arguments.getOrgnr().equalsIgnoreCase("         "));
        assertTrue(arguments.getForetaknr().equalsIgnoreCase("         "));
        assertFalse(arguments.isRunAsExternalProcess());
        assertTrue(arguments.getNewline().equalsIgnoreCase("\n"));
    }
    @Test
    public void testAllArgumentsAreSet() {
        arguments = new Arguments(
                new String[]{"-s", "Test", "-y", "9999", "-q", "1", "-r", "000000", "-n", "Name", "-u", "123456789", "-c", "987654321", "-a", "0", "-e", "1"},
                inputStream);

        assertTrue(arguments.getSkjema().equalsIgnoreCase("Test"));
        assertTrue(arguments.getAargang().equalsIgnoreCase("9999"));
        assertTrue(arguments.getRegion().equalsIgnoreCase("000000"));

        // test set values
        assertTrue(arguments.getKvartal().equalsIgnoreCase("1"));
        assertTrue(arguments.getNavn().equalsIgnoreCase("Name"));
        assertTrue(arguments.getOrgnr().equalsIgnoreCase("123456789"));
        assertTrue(arguments.getForetaknr().equalsIgnoreCase("987654321"));
        assertFalse(arguments.harVedlegg());
        assertTrue(arguments.isRunAsExternalProcess());
        assertTrue(arguments.getNewline().equalsIgnoreCase(""));
    }

    @Test
    public void testAllArgumentsAreSetToDefaultValues() {
        arguments = new Arguments(
                new String[]{"-s", "Test", "-y", "9999", "-q", " ", "-r", "000000", "-n", "Uoppgitt", "-u", "         ", "-c", "         ", "-a", "1", "-e", "0"},
                inputStream);

        assertTrue(arguments.getSkjema().equalsIgnoreCase("Test"));
        assertTrue(arguments.getAargang().equalsIgnoreCase("9999"));
        assertTrue(arguments.getRegion().equalsIgnoreCase("000000"));

        // test set values
        assertTrue(arguments.getKvartal().equalsIgnoreCase(" "));
        assertTrue(arguments.getNavn().equalsIgnoreCase("uoppgitt"));
        assertTrue(arguments.getOrgnr().equalsIgnoreCase("         "));
        assertTrue(arguments.getForetaknr().equalsIgnoreCase("         "));
        assertTrue(arguments.harVedlegg());
        assertFalse(arguments.isRunAsExternalProcess());
        assertTrue(arguments.getNewline().equalsIgnoreCase("\n"));
    }

    @Test
    public void testMissingArguments() {
        Exception e1 = assertThrows(
                IllegalArgumentException.class,
                () -> arguments = new Arguments(new String[]{"-y", "9999", "-r", "000000"}, inputStream));

        var expectedMessage1 = "parameter for skjema er ikke definert. Bruk -s SS. F.eks. -s 0A";
        var actualMessage1 = e1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception e2 = assertThrows(IllegalArgumentException.class,
                () -> arguments = new Arguments(new String[]{"-s", "Test", "-r", "000000"}, inputStream));

        var expectedMessage2 = "parameter for årgang er ikke definert. Bruk -y YYYY. F.eks. -y 2020";
        var actualMessage2 = e2.getMessage();

        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception e3 = assertThrows(IllegalArgumentException.class,
                () -> arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999"}, inputStream));

        var expectedMessage3 = "parameter for region er ikke definert. Bruk -r RRRRRR. F.eks. -r 030100";
        var actualMessage3 = e3.getMessage();

        assertTrue(actualMessage3.contains(expectedMessage3));
    }
}
