package no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad;

import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.FieldDefinition;
import no.ssb.kostra.felles.KostraRecord;
import no.ssb.kostra.utils.TestRecordInputAndResult;
import no.ssb.kostra.utils.TestRecordListInputAndResult;
import no.ssb.kostra.utils.TestStringInputAndResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static no.ssb.kostra.control.sosial.felles.ControlSosial.*;

class KvalifiseringstonadTest {
    private static final Arguments arguments = new Arguments(new String[]{"-s", "11CF", "-y", "2021", "-r", "420400"});
    private static final List<FieldDefinition> definitions = FieldDefinitions.getFieldDefinitions();

    static Stream<TestStringInputAndResult> control01RecordLengdeProvider() {
        return Stream.of(
                new TestStringInputAndResult(arguments, "1".repeat(138), false, Constants.NO_ERROR),
                new TestStringInputAndResult(arguments, "123456789", true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control03KommunenummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "KOMMUNE_NR", "4204"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "KOMMUNE_NR", "3400"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control03BydelsnummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "  "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "00"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "04"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11CF", "-y", "2021", "-r", "030101"}), new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "01"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11CF", "-y", "2021", "-r", "030100"}), new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "00"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control04OppgaveAarProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "VERSION", "21"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "VERSION", "19"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control05FodselsnummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096600100", "FNR_OK", "1"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "FNR_OK", "1"), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> control05AFodselsnummerDubletterProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "789", "PERSON_FODSELSNR", "19096633133", "STATUS", "1"), definitions)
                )
                        , false
                        , Constants.NO_ERROR
                )

                , new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "STATUS", "1"), definitions)
                )
                        , true
                        , Constants.CRITICAL_ERROR
                )

                , new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "STATUS", "2"), definitions)
                )
                        , false
                        , Constants.NO_ERROR
                )

                , new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "STATUS", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "STATUS", "1"), definitions)
                )
                        , false
                        , Constants.NO_ERROR
                )
        );
    }

    static Stream<TestRecordListInputAndResult> control05BJournalnummerDubletterProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096633133", "FNR_OK", "1"), definitions)
                )
                        , true
                        , Constants.CRITICAL_ERROR
                )

                , new TestRecordListInputAndResult(arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345"), definitions)
                )
                        , false
                        , Constants.NO_ERROR
                )
        );
    }

    static Stream<TestRecordInputAndResult> control06AlderUnder18AarProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "ALDER", "19", "FNR_OK", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "18", "FNR_OK", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "17", "FNR_OK", "1"), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control07AlderEr68AarEllerOverProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "ALDER", "69", "FNR_OK", "1"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "68", "FNR_OK", "1"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "67", "FNR_OK", "1"), definitions), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control08KjonnProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KJONN", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KJONN", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KJONN", "0"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control09SivilstandProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "EKTSTAT", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "EKTSTAT", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "EKTSTAT", "0"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    @AfterEach
    public void resetStaticRecordCounter() {
        KostraRecord.resetLineCount();
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control01RecordLengdeProvider")
    void control01RecordLengdeTest(TestStringInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), ControlRecordLengde.doControl(List.of(inputAndResult.getString()), inputAndResult.getErrorReport(), FieldDefinitions.getFieldLength()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control03KommunenummerProvider")
    void control03KommunenummerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control03Kommunenummer(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control03BydelsnummerProvider")
    void control03BydelsnummerTest(TestRecordInputAndResult inputAndResult) {
        boolean result = control03Bydelsnummer(inputAndResult.getErrorReport(), inputAndResult.getRecord());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control04OppgaveAarProvider")
    void control04OppgaveAarTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control04OppgaveAar(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05FodselsnummerProvider")
    void control05FodselsnummerTest(TestRecordInputAndResult inputAndResult) {
        var result = control05Fodselsnummer(inputAndResult.getErrorReport(), inputAndResult.getRecord());

        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05AFodselsnummerDubletterProvider")
    void control05AFodselsnummerDubletterTest(TestRecordListInputAndResult inputAndResult) {
        var result = control05AFodselsnummerDubletter(inputAndResult.getErrorReport(), inputAndResult.getRecordList());

        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05BJournalnummerDubletterProvider")
    void control05BJournalnummerDubletterTest(TestRecordListInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control05BJournalnummerDubletter(inputAndResult.getErrorReport(), inputAndResult.getRecordList()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control06AlderUnder18AarProvider")
    void control06AlderUnder18AarTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control06AlderUnder18Aar(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control07AlderEr68AarEllerOverProvider")
    void control07AlderEr68AarEllerOverTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control07AlderEr68AarEllerOver(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control08KjonnProvider")
    void control08KjonnTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control08Kjonn(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control09SivilstandProvider")
    void control09SivilstandTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control09Sivilstand(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }
}
