package no.ssb.kostra.control.famvern.s52b;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.FieldDefinition;
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
        inputFileContent =
        //@formatter:off
                //0000000011111111112222222222333333333344444444445555555555666666666677777777778888888
                //2345678901234567890123456789012345678901234567890123456789012345678901234567890123456
                "667600017000011aaabbb                        01022020203003005012015010013022209022020\n" +
                "667600017000012aaabbb                        01022020203003005012015010013022209022020\n" +
                "667600017000013aaabbb                        01022020203003005012015010013022209022020\n" +
                "667600017000014aaabbb                        01022020203003005012015010013022209022020\n" +
                "667600117000015aaabbb                        01022020203003005012015010013022209022020\n" +
                "667600017000016aaabbb                        01022020203003005012015010013022209022020\n" +
                "667600017000016aaabbb                        01022020203003005012015010013022209022020\n" +
                "667600017000017                              01022020203003005012015010013022209022020\n" +
                "667600017000018aaabbb                                203003005012015010013022209022020\n" +
                "667600017000019aaabbb                        01022020 03003005012015010013022209022020\n" +
                "667600017000020aaabbb                        010220202  003005012015010013022209022020\n" +
                "667600017000021aaabbb                        01022020203   005012015010013022209022020\n" +
                "667600017000022aaabbb                        01022020203003   012015010013022209022020\n" +
                "667600017000023aaabbb                        01022020203003005   015010013022209022020\n" +
                "667600017000024aaabbb                        01022020203003005012   010013022209022020\n" +
                "667600017000025aaabbb                        01022020203003005012015   013022209022020\n" +
                "667600017000026aaabbb                        01022020203003005012015010   022209022020\n" +
                "667600017000027aaabbb                        01022020203003005012015010013  2209022020\n" +
                "667600017000028aaabbb                        0102202020300300501201501001302 209022020\n" +
                "667600017000029aaabbb                        01022020203003005012015010013022 09022020\n" +
                "667600017000030aaabbb                        010220202030030050120150100130222        \n" +
                "667600017000031aaabbb                        01022020203003005012015010013022201012020\n" +
                "667600017000032aaabbb                        01022020203003005012015010013022209022020\n" +
                "667600017000033aaabbbæøå                     01022020203003005012015010013022209022020\n";

        //@formatter:on

        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        fieldDefinitions = FieldDefinitions.getFieldDefinitions();

        args = new Arguments(new String[]{"-s", "52BF", "-y", "2020", "-r", "667600"});
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
