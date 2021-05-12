package no.ssb.kostra.control.famvern.s52a;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;

import no.ssb.kostra.felles.FieldDefinition;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Ignore
public class MainITest {

    InputStream sysInBackup;
    private Arguments args;
    private ErrorReport er;
    private String inputFileContent;
    private List<FieldDefinition> fieldDefinitions;


    @Before
    public void beforeTest() {
        //@formatter:off
        //       0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111111111111111111111111111111111111111111111111111
        //       0000000001111111111222222222233333333334444444444555555555566666666667777777777888888888899999999990000000000111111111122222222223333333333444444444455
        //       1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901
        inputFileContent =
                "6676000170201001000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201002000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201003000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "667600 160201004000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000730201005000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201006000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201006000101202041119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201007000000000011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201008000101202001119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201009000101202016119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201010000101202011319761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "66760001702010110001012020111    1 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201012000101202011119767 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201013000101202011119764 1133  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201014000101202011119761  113  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201015000101202011119761 1 13  1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201016000101202011119761 111   1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201017000101202011119761 112  11       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201018000101202011119761 112 1 1       0302202021                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201020100101202011119761 1113  1               21                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201021000101202011119761 1113  1       0312201621                 12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201022000101202011119761 1113  1       030220202                  12122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201023000101202011119761 1113  1       0302202021                  2122222220302              003000003005      00300521          10120052020\n" +
                "6676000170201024000101202011119761 1113  1       0302202021                 12 22222220302              003000003005      00300521          10120052020\n" +
                "6676000170201025000101202011119761 1113  1       0302202021                 1222222222                  003000003005      00300521          10120052020\n" +
                "6676000170201026000101202011119761 1113  1       0302202021                 121222222203                003000003005      00300521          10120052020\n" +
                "6676000170201027000101202011119761 1113  1       0302202021                 12122222220302              000000003005      00300521          10120052020\n" +
                "6676000170201028000101202011119761 1113  1       0302202021                 12122222220302              003000000005      00300521          10120052020\n" +
                "6676000170201029000101202011119761 1113  1       0302202021                 12122222220302              003000003000      00300521          10120052020\n" +
                "6676000170201030000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00000521          10120052020\n" +
                "6676000170201031000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300021          10120052020\n" +
                "6676000170201032000101202011119761 1113  1       0302202021                 12122222220302              003000003005      0030052           10120052020\n" +
                "6676000170201034000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521           0120052020\n" +
                "6676000170201033000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          1  20052020\n" +
                "6676000170201035000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          101        \n" +
                "6676000170201036000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052018\n" +
                "6676000170201037000101202011119761 1113  1       0302202021                 1 122222220302              003000003005      00300521          10120052020\n";

        //@formatter:on

        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        fieldDefinitions = FieldDefinitions.getFieldDefinitions();

        args = new Arguments(new String[]{"-s", "52AF", "-y", "2020", "-r", "667600"});
    }

    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    public void testDoControl() {
        er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        assertNotNull("Has content ErrorReport", er);
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }


}
