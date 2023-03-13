package no.ssb.kostra.control.regnskap.kirkekostra;

import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.control.regnskap.felles.ControlRegnskap;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.KostraRecord;
import no.ssb.kostra.utils.TestRecordListInputAndResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static no.ssb.kostra.control.felles.ControlIntegritet.controlSektor;
import static org.junit.jupiter.api.Assertions.assertEquals;


class KirkeKostraITCase {
    private final static Arguments args0F = new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"});
    private final static Arguments args0G = new Arguments(new String[]{"-s", "0G", "-y", "2020", "-r", "300500", "-u", "976989732"});

    static Stream<TestRecordListInputAndResult> controlSektorInputs() {
        return Stream.of(
                new TestRecordListInputAndResult(args0G, List.of(new KostraRecord("0G2020 300500976989732         510  123      263", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(args0G, List.of(new KostraRecord("0G2020 300500976989732         510           263", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> controlKombinasjonKontoklasseArtInvesteringsregnskapInputs() {
        return Stream.of(
                new TestRecordListInputAndResult(args0G, List.of(new KostraRecord("0G2020 300500976989732         510  123    -3000", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)//,
//                new TestRecordListInputAndResult(args0F, List.of(new KostraRecord("0F2020 300500976989732         4041 110    -3000", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
//                new TestRecordListInputAndResult(args0F, List.of(new KostraRecord("0F2020 300500976989732         4041 990    -3000", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR)
        );
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("controlSektorInputs")
    void controlSektorTest(TestRecordListInputAndResult inputAndResult) {
        ErrorReport errorReport = inputAndResult.getErrorReport();
        Arguments arguments = errorReport.getArgs();
        boolean result = controlSektor(inputAndResult.getErrorReport(), inputAndResult.getRecordList(), Main.getArtSektorAsList(arguments.getSkjema()));

        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), errorReport.getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("controlKombinasjonKontoklasseArtInvesteringsregnskapInputs")
    void controlKombinasjonKontoklasseArtInvesteringsregnskapTest(TestRecordListInputAndResult inputAndResult) {
        ErrorReport errorReport = inputAndResult.getErrorReport();
        List<String> bevilgningRegnskapList = Main.getBevilgningRegnskapList();
        String kontoklasse = Main.getKontoklasseAsMap(inputAndResult.getErrorReport().getArgs().getSkjema()).get("I");
        List<String> artSektorList = Main.getArtSektorAsList(inputAndResult.getErrorReport().getArgs().getSkjema());
        List<String> invalidArtSektorList = Main.getArterUgyldigInvestering();
        List<String> validArtSektorList = Comparator.removeCodesFromCodeList(artSektorList, invalidArtSektorList);

        boolean result = ControlRegnskap.controlKombinasjonKontoklasseArt(inputAndResult.getErrorReport(),
                inputAndResult.getRecordList(),
                bevilgningRegnskapList,
                kontoklasse,
                validArtSektorList,
                "Korrigér art (%s) til gyldig art i investeringsregnskapet, eller overfør posteringen til driftsregnskapet",
                Constants.CRITICAL_ERROR
        );

        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport());

        assertEquals(inputAndResult.isResult(), result);
        assertEquals(inputAndResult.getExpectedErrorType(), errorReport.getErrorType());

/*
        assertEquals(inputAndResult.isResult(), ControlRegnskap.controlKombinasjonKontoklasseArt(inputAndResult.getErrorReport(),
                inputAndResult.getRecordList(),
                Main.getBevilgningRegnskapList(),
                Main.getKontoklasseAsMap(inputAndResult.getErrorReport().getArgs().getSkjema()).get("I"),
                Comparator.removeCodesFromCodeList(Main.getArtSektorAsList(inputAndResult.getErrorReport().getArgs().getSkjema()), Main.getArterUgyldigInvestering()),
                "Korrigér art (%s) til gyldig art i investeringsregnskapet, eller overfør posteringen til driftsregnskapet",
                Constants.CRITICAL_ERROR
        ));
        assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
        System.out.println(inputAndResult.getErrorReport().generateReport());

 */
    }

    @Test
    void testDoControl0F() {
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

        Assertions.assertNotNull(errorReport, "Has content ErrorReport");
        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());

    }
//
//    @Test
//    public void testDoControl0G() {
//        //@formatter:off
//        inputFileContent =
//            //00000000111111111122222222223333333333444444444
//            //23456789012345678901234567890123456789012345678
//            "0G2020 300500976989732         510           263\n" +
//            "0G2020 300500976989732         511         38896\n" +
//            "0G2020 300500976989732         512         80297\n" +
//            "0G2020 300500976989732         513         36431\n" +
//            "0G2020 300500976989732         518           202\n" +
//            "0G2020 300500976989732         521         95733\n" +
//            "0G2020 300500976989732         522         35500\n" +
//            "0G2020 300500976989732         524         32842\n" +
//            "0G2020 300500976989732         527         95113\n" +
//            "0G2020 300500976989732         531        -84392\n" +
//            "0G2020 300500976989732         532        -99758\n" +
//            "0G2020 300500976989732         541        -18392\n" +
//            "0G2020 300500976989732         545        -57043\n" +
//            "0G2020 300500976989732         551        -55373\n" +
//            "0G2020 300500976989732         553        -13875\n" +
//            "0G2020 300500976989732         555         -9935\n" +
//            "0G2020 300500976989732         556        -99116\n" +
//            "0G2020 300500976989732         5581        32670\n" +
//            "0G2020 300500976989732         55990      -10063\n" +
//            "0G2020 300500976989732         59100        9897\n" +
//            "0G2020 300500976989732         59200       12171\n" +
//            "0G2020 300500976989732         59999      -22068\n";
//        //@formatter:on
//
//        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
//        System.setIn(in);
//
//        args = new Arguments(new String[]{"-s", "0G", "-y", "2020", "-r", "300500", "-u", "976989732"});
//
//        er = Main.doControls(args);
//
//        if (Constants.DEBUG) {
//            System.out.print(er.generateReport());
//        }
//
//        assertNotNull("Has content ErrorReport", er);
//        assertEquals(Constants.NO_ERROR, er.getErrorType());
//    }
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

