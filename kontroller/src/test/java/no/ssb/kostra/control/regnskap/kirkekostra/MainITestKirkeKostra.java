package no.ssb.kostra.control.regnskap.kirkekostra;

import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.KostraRecord;
import no.ssb.kostra.utils.TestRecordListInputAndResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static no.ssb.kostra.control.felles.ControlIntegritet.controlSektor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class MainITestKirkeKostra {
    private final static Arguments args0F = new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"});
    private final static Arguments args0G = new Arguments(new String[]{"-s", "0G", "-y", "2020", "-r", "300500", "-u", "976989732"});

    static Stream<TestRecordListInputAndResult> controlSektorInputs() {
        return Stream.of(
                new TestRecordListInputAndResult(args0G, List.of(new KostraRecord("0G2020 300500976989732         510  123      263", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(args0G, List.of(new KostraRecord("0G2020 300500976989732         510           263", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(args0F, List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(args0F, List.of(new KostraRecord("0F2020 303030976989732         9041 abc    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(args0F, List.of(new KostraRecord("0F2020 300500976989732         3041 123    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(args0F, List.of(new KostraRecord("                                    123         ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    @Disabled("TODO fix me")
    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("controlSektorInputs")
    public void controlSektorTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(inputAndResult.isResult(), controlSektor(inputAndResult.getErrorReport(), inputAndResult.getRecordList(), Main.getBalanseRegnskapList()));
        assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }


    static Stream<TestRecordListInputAndResult> controlKombinasjonKontoklasseArtInvesteringsregnskapInputs() {
        return Stream.of(
                new TestRecordListInputAndResult(args0G, List.of(new KostraRecord("0G2020 300500976989732         510  123    -3000", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(args0F, List.of(new KostraRecord("0F2020 300500976989732         4041 110    -3000", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(args0F, List.of(new KostraRecord("0F2020 300500976989732         4041 990    -3000", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR)
        );
    }

//    @ParameterizedTest(name = "#{index} - Run test with {0}")
//    @MethodSource("controlKombinasjonKontoklasseArtInvesteringsregnskapInputs")
//    public void controlKombinasjonKontoklasseArtInvesteringsregnskapTest(TestRecordListInputAndResult inputAndResult) {
//        assertEquals(inputAndResult.isResult(), ControlRegnskap.controlKombinasjonKontoklasseArt(inputAndResult.getErrorReport(),
//                inputAndResult.getRecordList(),
//                Main.getBevilgningRegnskapList(),
//                Main.getKontoklasseAsMap(inputAndResult.getErrorReport().getArgs().getSkjema()).get("I"),
//                removeCodesFromCodelist(Main.getArtSektorAsList(inputAndResult.getErrorReport().getArgs().getSkjema()), Main.getArterUgyldigInvestering()),
//                "Korrigér art (%s) til gyldig art i investeringsregnskapet, eller overfør posteringen til driftsregnskapet",
//                Constants.CRITICAL_ERROR
//        ));
//        assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
//        System.out.println(inputAndResult.getErrorReport().generateReport());
//    }

    @Test
    public void testDoControl0F() {
        //@formatter:off
        String inputFileContent =
                //00000000111111111122222222223333333333444444444
                //23456789012345678901234567890123456789012345678

                """
                        0F2020 300500976989732         3041 650    -8695
                        0F2020 300500976989732         3041 305     4000
                        0F2020 300500976989732         3042 405     4695
                        0F2020 300500976989732         3089 900    -1000
                        0F2020 300500976989732         3043 805    -1000
                        0F2020 300500976989732         4045 305     4680
                        0F2020 300500976989732         4044 805    -4695
                        0F2020 300500976989732         4044 405    -1040
                        0F2020 300500976989732         4041 905    -3000
                        0F2020 300500976989732         4041 010     2000
                        0F2020 300500976989732         3042 650    -4695
                        0F2020 300500976989732         4089 900    -1000
                        0F2020 300500976989732         3045 010     4680
                        0F2020 300500976989732         3041 010     4920
                        0F2020 300500976989732         3041 905     -920
                        0F2020 300500976989732         3041 270      920
                        0F2020 300500976989732         3041 390      920
                        0F2020 300500976989732         3041 790     -920
                        0F2020 300500976989732         3041 090      525
                        0F2020 300500976989732         3041 095      565
                        0F2020 300500976989732         3041 570     2000
                        0F2020 300500976989732         3041 610     -525
                        0F2020 300500976989732         3041 800    -4920
                        0F2020 300500976989732         3042 010     5882
                        0F2020 300500976989732         3042 830    -9577
                        0F2020 300500976989732         3044 090      535
                        0F2020 300500976989732         3042 095      565
                        0F2020 300500976989732         3043 610     -565
                        0F2020 300500976989732         3043 800    -4920
                        0F2020 300500976989732         3043 700     -565
                        0F2020 300500976989732         3044 010     4920
                        0F2020 300500976989732         3044 020      565
                        0F2020 300500976989732         3044 030      565
                        0F2020 300500976989732         4042 220    12405
                        0F2020 300500976989732         4042 285     1425
                        0F2020 300500976989732         4042 830   -10920
                        0F2020 300500976989732         4042 970    -4000
                        0F2020 300500976989732         3042 729        0
                        0F2020 300500976989732         4042 429        0
                        0F2020 300500976989732         3042 570     2000
                        0F2020 300500976989732         3041 590        0
                        0F2020 300500976989732         3044 429       20
                        0F2020 300500976989732         4042 910        0
                        0F2020 300500976989732         4042 670      -10
                        0F2020 300500976989732         4041 280     4015
                        0F2020 300500976989732         4043 429       80
                        0F2020 300500976989732         4044 429       60
                        0F2020 300500976989732         3044 380       40
                        0F2020 300500976989732         3044 780      -20
                        0F2020 300500976989732         3044 390       15
                        0F2020 300500976989732         3043 790      -20
                        0F2020 300500976989732         3044 465       70
                        0F2020 300500976989732         3044 865      -60
                        """;
        var byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        var arguments = new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"},
                byteArrayInputStream);

        var errorReport = Main.doControls(arguments);

        if (Constants.DEBUG) {
            System.out.print(errorReport.generateReport());
        }

        assertNotNull(errorReport, "Has content ErrorReport");
        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());

    }

    @Test
    void testDoControl0G() {
        //@formatter:off
        String inputFileContent =
            //00000000111111111122222222223333333333444444444
            //23456789012345678901234567890123456789012345678
            "0G2021 777700987654321         510  000        0\n" +
            "0G2021 777700987654321         510  070     1139\n" +
            "0G2021 777700987654321         510  320    24872\n" +
            "0G2021 777700987654321         513  080      355\n" +
            "0G2021 777700987654321         513  640     1699\n" +
            "0G2021 777700987654321         513  890      600\n" +
            "0G2021 777700987654321         521  200     1998\n" +
            "0G2021 777700987654321         524  080    44297\n" +
            "0G2021 777700987654321         527  080   116753\n" +
            "0G2021 777700987654321         532  080    -2349\n" +
            "0G2021 777700987654321         532  610    -2527\n" +
            "0G2021 777700987654321         532  640    -2188\n" +
            "0G2021 777700987654321         532  890    -6780\n" +
            "0G2021 777700987654321         551  080     -287\n" +
            "0G2021 777700987654321         553  080     -281\n" +
            "0G2021 777700987654321         555  080   -10222\n" +
            "0G2021 777700987654321         556  080    -3365\n" +
            "0G2021 777700987654321         55950080      -89\n" +
            "0G2021 777700987654321         55960080     -577\n" +
            "0G2021 777700987654321         55990080  -163048\n";
        //@formatter:on

        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        var args = new Arguments(new String[]{"-s", "0G", "-y", "2021", "-r", "777700", "-u", "987654321"});
        args.setInputFileContent(Arrays.stream(inputFileContent.split("\n")).toList());

        var er = Main.doControls(args);

        if (Constants.DEBUG) {
            System.out.print(er.generateReport());
        }

        //assertNotNull("Has content ErrorReport", er);
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
//
//
//    @Test
//    public void testDoControl0GFail() {
//        //@formatter:off
//        inputFileContent =
//            //00000000111111111122222222223333333333444444444
//            //23456789012345678901234567890123456789012345678
//            "0G2020 341600976987942         5*** 000    -6263\n" +
//            "0G2020 341600976987942         510  320    10176\n" +
//            "0G2020 341600976987942         513  200        1\n" +
//            "0G2020 341600976987942         513  640      293\n" +
//            "0G2020 341600976987942         521  550       96\n" +
//            "0G2020 341600976987942         524  080       13\n" +
//            "0G2020 341600976987942         527  080     6080\n" +
//            "0G2020 341600976987942         532  080    -2196\n" +
//            "0G2020 341600976987942         532  640     -188\n" +
//            "0G2020 341600976987942         532  890     -300\n" +
//            "0G2020 341600976987942         535  000      -56\n" +
//            "0G2020 341600976987942         551  080     -189\n" +
//            "0G2020 341600976987942         553  080      -40\n" +
//            "0G2020 341600976987942         555  080     -662\n" +
//            "0G2020 341600976987942         556  080    -6841\n" +
//            "";
//
//        //@formatter:on
//
//        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
//        System.setIn(in);
//
//        args = new Arguments(new String[]{"-s", "0G", "-y", "2020", "-r", "341600", "-u", "976987942"});
//
//        er = Main.doControls(args);
//
//        if (Constants.DEBUG) {
//            System.out.print(er.generateReport());
//        }
//
//        assertNotNull("Has content ErrorReport", er);
//        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
//    }
}

