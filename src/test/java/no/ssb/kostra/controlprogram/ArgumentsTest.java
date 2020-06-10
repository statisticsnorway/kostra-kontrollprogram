package no.ssb.kostra.controlprogram;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ArgumentsTest {
    private Arguments args;
    private String inputFileContent;
    InputStream sysInBackup;

    @Before
    public void beforeTest() {
        inputFileContent = "0A20194040200                  1120 010    36328\n" +
                "0A20194040200                  1121 010     4306\n" +
                "0A20194040200                  1130 010      864";

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
            assertTrue(result.length() == inputFileContent.length());
            assertTrue(result.equalsIgnoreCase(inputFileContent));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readFileFromClassPath1() {
        Path resourceFile = Paths.get("src", "test", "resources", "0A_V2019_K4_R040200_OK.dat");
        String absolutePath = resourceFile.toFile().getAbsolutePath();

        try {
            List<String> lines = Files.readAllLines(Paths.get(absolutePath), StandardCharsets.ISO_8859_1);
            String fileContent = String.join("\n", lines);
            assertTrue(!fileContent.isEmpty());

        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(absolutePath.endsWith("0A_V2019_K4_R040200_OK.dat"));
    }

    @Test
    public void readFileFromClassPath2() {
        Path resourceFile = Paths.get("src", "test", "resources", "15F_V2019_R040200_OK.xml");
        String absolutePath = resourceFile.toFile().getAbsolutePath();
        try {
            List<String> lines = Files.readAllLines(Paths.get(absolutePath), StandardCharsets.ISO_8859_1);
            String fileContent = String.join("\n", lines);
            assertTrue(!fileContent.isEmpty());

        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(absolutePath.endsWith("15F_V2019_R040200_OK.xml"));
    }

    @Test
    public void testArguments() {
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000", "-i", "src/test/resources/15F_V2019_R040200_OK.xml"});

        assertTrue(args.getSkjema().equalsIgnoreCase("Test"));
        assertTrue(args.getAargang().equalsIgnoreCase("9999"));
        assertTrue(args.getRegion().equalsIgnoreCase("000000"));

        // test default values
        assertTrue(args.getKvartal().equalsIgnoreCase(" "));
        assertTrue(args.getNavn().equalsIgnoreCase("Uoppgitt"));
        assertTrue(args.getOrgnr().equalsIgnoreCase("         "));
        assertTrue(args.getForetaknr().equalsIgnoreCase("         "));
    }

    @Test
    public void testMissingArguments() {
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> {
            args = new Arguments(new String[]{"-y", "9999", "-r", "000000"});
        });

        String expectedMessage1 = "parameter for skjema er ikke definert. Bruk -s SS. F.eks. -s 0A";
        String actualMessage1 = e1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception e2 = assertThrows(IllegalArgumentException.class, () -> {
            args = new Arguments(new String[]{"-s", "Test", "-r", "000000"});
        });

        String expectedMessage2 = "parameter for årgang er ikke definert. Bruk -y YYYY. F.eks. -y 2020";
        String actualMessage2 = e2.getMessage();

        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception e3 = assertThrows(IllegalArgumentException.class, () -> {
            args = new Arguments(new String[]{"-s", "Test", "-y", "9999"});
        });

        String expectedMessage3 = "parameter for region er ikke definert. Bruk -r RRRRRR. F.eks. -r 030100";
        String actualMessage3 = e3.getMessage();

        assertTrue(actualMessage3.contains(expectedMessage3));

    }

}
