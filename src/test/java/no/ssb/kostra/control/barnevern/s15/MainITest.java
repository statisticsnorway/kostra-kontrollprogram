package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainITest {
    InputStream sysInBackup;
    private Arguments args;

    @Before
    public void beforeTest() {
        sysInBackup = System.in; // backup System.in to restore it later
    }

    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    public void testDoControlErrors340100() {
        controlFile("/testfiler/15F/Testfile_errors_3401_2020.xml"
                , new String[]{"-s", "15F", "-y", "2020", "-r", "340100"}
                , Constants.CRITICAL_ERROR);
    }

    @Test
    public void testDoControlErrors030102() {
        controlFile("/testfiler/15F/Testfile_errors_030102_2020.xml"
                , new String[]{"-s", "15F", "-y", "2020", "-r", "030102"}
                , Constants.CRITICAL_ERROR);
    }

    private void controlFile(String classpathToFile, String[] cliArgs, int expectedError){
        InputStream inputStream = null;
        String inputFileContent = "";
        Class clazz = MainITest.class;

        try {
            inputStream = clazz.getResourceAsStream(classpathToFile);
            inputFileContent = readFromInputStream(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        args = new Arguments(cliArgs);

        ErrorReport er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        assertNotNull("Has content ErrorReport", er);
        assertEquals(expectedError, er.getErrorType());
    }

    private String readFromInputStream(InputStream inputStream) {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            return "";
        }
        return resultStringBuilder.toString();
    }
}
