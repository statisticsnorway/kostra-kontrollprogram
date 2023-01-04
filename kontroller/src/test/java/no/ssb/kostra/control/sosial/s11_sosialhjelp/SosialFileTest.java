package no.ssb.kostra.control.sosial.s11_sosialhjelp;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class SosialFileTest {

    private Arguments arguments;

    @BeforeEach
    public void beforeTest() {
        // Mocking a test file
        var inputFileContent =
// to make position easier to see
// hundreds      0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111112222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222223333333333333333333333333333
// tens          0000000001111111111222222222233333333334444444444555555555566666666667777777777888888888899999999990000000001111111111222222222233333333334444444444555555555566666666667777777777888888888899999999990000000001111111111222222222233333333334444444444555555555566666666667777777777888888888899999999990000000001111111111222222222
// ones          1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
                "420422  040000186202011088123            11101512040102030405060708091011120073964       0073964                                                                                                                                                                 11per0000102122                                                    ";

        FieldDefinitions.getFieldDefinitions();
        var byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        arguments = new Arguments(new String[]{"-s", "11F", "-y", "2022", "-r", "420400"}, byteArrayInputStream);
    }

    @Test
    void testDoControl() {
        var errorReport = Main.doControls(arguments);

        if (Constants.DEBUG) {
            System.out.print(errorReport.generateReport());
        }

        Assertions.assertNotNull(errorReport, "Has content ErrorReport");
        Assertions.assertEquals(Constants.NO_ERROR, errorReport.getErrorType());
    }
}
