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

    @Before
    public void beforeTest() {
        sysInBackup = System.in; // backup System.in to restore it later
    }

    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    public void testFail01() {
        controlFile("/testfiler/15F/Testfil_01_FEIL_2020_15F_ugyldig_xml.xml"
                , new String[]{"-s", "15F", "-y", "2020", "-r", "340100", "-n", "Test av ugyldig testfil :-("}
                , Constants.CRITICAL_ERROR);
    }

    @Test
    public void testFail02() {
        controlFile("/testfiler/15F/Testfil_02_FEIL_2020_15F_for_3401_feil_i_avgiver.xml"
                , new String[]{"-s", "15F", "-y", "2020", "-r", "340100", "-n", "Test av ugyldig Avgiver informasjon :-("}
                , Constants.CRITICAL_ERROR);
    }

    @Test
    public void testFail03() {
        controlFile("/testfiler/15F/Testfil_03_FEIL_2020_15F_for_030102.xml"
                , new String[]{"-s", "15F", "-y", "2020", "-r", "030102", "-n", "Test av bydel uten bydelsinformasjon :-("}
                , Constants.CRITICAL_ERROR);
    }

    @Test
    public void testFail04() {
        controlFile("/testfiler/15F/Testfil_04_FEIL_2020_15F_for_3401.xml"
                , new String[]{"-s", "15F", "-y", "2020", "-r", "340100", "-n", "Test av alle kontroller :-("}
                , Constants.CRITICAL_ERROR);
    }

    @Test
    public void testWarning11() {
        controlFile("/testfiler/15F/Testfil_11_ADVARSEL_2020_15F_for_3401.xml"
                , new String[]{"-s", "15F", "-y", "2020", "-r", "340100", "-n", "Test av advarselskontroller :-|"}
                , Constants.NORMAL_ERROR);
    }

    @Test
    public void testWarning12() {
        controlFile("/testfiler/15F/Testfil_12_ADVARSEL_2020_15F_for_3035_Visma.xml"
                , new String[]{"-s", "15F", "-y", "2020", "-r", "303500", "-n", "Test av advarselskontroller, Visma :-|"}
                , Constants.NORMAL_ERROR);
    }

    @Test
    public void testOK21() {
        controlFile("/testfiler/15F/Testfil_21_OK_2020_15F_for_3401.xml"
                , new String[]{"-s", "15F", "-y", "2020", "-r", "340100", "-n", "Test av data OK, kommune :-D"}
                , Constants.NO_ERROR);
    }

    @Test
    public void testOK22() {
        controlFile("/testfiler/15F/Testfil_22_OK_2020_15F_for_030102.xml"
                , new String[]{"-s", "15F", "-y", "2020", "-r", "030102", "-n", "Test av data OK, bydel :-D"}
                , Constants.NO_ERROR);
    }

    private void controlFile(String classpathToFile, String[] cliArgs, int expectedError){
        InputStream inputStream = null;
        String inputFileContent;

        try {
            inputStream = MainITest.class.getResourceAsStream(classpathToFile);
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

        Arguments args = new Arguments(cliArgs);

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
