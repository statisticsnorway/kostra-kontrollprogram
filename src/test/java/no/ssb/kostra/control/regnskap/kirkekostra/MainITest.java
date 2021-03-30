package no.ssb.kostra.control.regnskap.kirkekostra;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.FieldDefinition;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainITest {

    InputStream sysInBackup;
    private Arguments args;
    private ErrorReport er;
    private String inputFileContent;
    private List<FieldDefinition> fieldDefinitions;


    @Before
    public void beforeTest() {
        fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        sysInBackup = System.in; // backup System.in to restore it later

    }

    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }

//        @Test
//        public void testDoControl0F() {
//            er = Main.doControls(args);
//
//            if (Constants.DEBUG) {
//                System.out.print(er.generateReport());
//            }
//
//            assertNotNull("Has content ErrorReport", er);
//            assertEquals(Constants.NORMAL_ERROR, er.getErrorType());
//        }

    @Test
    public void testDoControl0G() {
        //@formatter:off
        inputFileContent =
            //00000000111111111122222222223333333333444444444
            //23456789012345678901234567890123456789012345678
            "0G2020 300500976989732         510           263\n" +
            "0G2020 300500976989732         511         38896\n" +
            "0G2020 300500976989732         512         80297\n" +
            "0G2020 300500976989732         513         36431\n" +
            "0G2020 300500976989732         518           202\n" +
            "0G2020 300500976989732         521         95733\n" +
            "0G2020 300500976989732         522         35500\n" +
            "0G2020 300500976989732         524         32842\n" +
            "0G2020 300500976989732         527         95113\n" +
            "0G2020 300500976989732         531        -84392\n" +
            "0G2020 300500976989732         532        -99758\n" +
            "0G2020 300500976989732         541        -18392\n" +
            "0G2020 300500976989732         545        -57043\n" +
            "0G2020 300500976989732         551        -55373\n" +
            "0G2020 300500976989732         553        -13875\n" +
            "0G2020 300500976989732         555         -9935\n" +
            "0G2020 300500976989732         556        -99116\n" +
            "0G2020 300500976989732         5581        32670\n" +
            "0G2020 300500976989732         55990      -10063\n" +
            "0G2020 300500976989732         59100        9897\n" +
            "0G2020 300500976989732         59200       12171\n" +
            "0G2020 300500976989732         59999      -22068\n";
        //@formatter:on

        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        args = new Arguments(new String[]{"-s", "0G", "-y", "2020", "-r", "300500", "-u", "976989732"});

        er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        assertNotNull("Has content ErrorReport", er);
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }


    @Test
    public void testDoControl0GFail() {
        //@formatter:off
        inputFileContent =
            //00000000111111111122222222223333333333444444444
            //23456789012345678901234567890123456789012345678
            "0G2020 341600976987942         5*** 000    -6263\n" +
            "0G2020 341600976987942         510  320    10176\n" +
            "0G2020 341600976987942         513  200        1\n" +
            "0G2020 341600976987942         513  640      293\n" +
            "0G2020 341600976987942         521  550       96\n" +
            "0G2020 341600976987942         524  080       13\n" +
            "0G2020 341600976987942         527  080     6080\n" +
            "0G2020 341600976987942         532  080    -2196\n" +
            "0G2020 341600976987942         532  640     -188\n" +
            "0G2020 341600976987942         532  890     -300\n" +
            "0G2020 341600976987942         535  000      -56\n" +
            "0G2020 341600976987942         551  080     -189\n" +
            "0G2020 341600976987942         553  080      -40\n" +
            "0G2020 341600976987942         555  080     -662\n" +
            "0G2020 341600976987942         556  080    -6841\n" +
            "";

        //@formatter:on

        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        args = new Arguments(new String[]{"-s", "0G", "-y", "2020", "-r", "341600", "-u", "976987942"});

        er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        assertNotNull("Has content ErrorReport", er);
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}

