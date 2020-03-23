package no.ssb.kostra.controlprogram;

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

import static org.junit.Assert.assertTrue;

public class ArgumentsTest {
    private Arguments args;
    private String inputFileContent;

    @Before
    public void beforeTest() {
        inputFileContent = "0A20194040200                  1120 010    36328\n" +
                "0A20194040200                  1121 010     4306\n" +
                "0A20194040200                  1130 010      864";
    }

    @Test
    public void testReadFileFromStdin() {
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"});
        byte[] bytes = inputFileContent.getBytes(StandardCharsets.ISO_8859_1);
        InputStream inputStream = new ByteArrayInputStream(bytes);

        try {
            args.readFileFromStdin(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = String.join("\n", args.getInputFileContent());
        assertTrue(result.length() == inputFileContent.length());
        assertTrue(result.equalsIgnoreCase(inputFileContent));
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
}
