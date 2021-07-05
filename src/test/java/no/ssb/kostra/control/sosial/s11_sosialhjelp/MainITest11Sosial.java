package no.ssb.kostra.control.sosial.s11_sosialhjelp;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.Record;
import no.ssb.kostra.utils.TestRecordInputAndResult;
import no.ssb.kostra.utils.TestRecordListInputAndResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static no.ssb.kostra.control.sosial.felles.ControlSosial.*;

public class MainITest {
    static Stream<TestRecordInputAndResult> control03KommunenummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "KOMMUNE_NR", "4204"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "KOMMUNE_NR", "3400"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control03BydelsnummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "00"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "04"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "030101"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "01"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "030100"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "00"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control04OppgaveAarProvider() {
        return Stream.of(
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "VERSION", "20"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "VERSION", "19"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control05FodselsnummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345"), FieldDefinitions.getFieldDefinitions()), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> control05AFodselsnummerDubletterProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"})
                        , List.of(
                        new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                        , new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                        , new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "789", "PERSON_FODSELSNR", "19096633133", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                )
                        , true, Constants.CRITICAL_ERROR)

                , new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"})
                        , List.of(
                        new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345"), FieldDefinitions.getFieldDefinitions())
                )
                        , false, Constants.NO_ERROR)


        );
    }

    static Stream<TestRecordListInputAndResult> control05BJournalnummerDubletterProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"})
                        , List.of(
                        new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                        , new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                        , new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096633133", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                )
                        , true, Constants.CRITICAL_ERROR)

                , new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2020", "-r", "420400"})
                        , List.of(
                        new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345"), FieldDefinitions.getFieldDefinitions())
                )
                        , false, Constants.NO_ERROR)


        );
    }


    @AfterEach
    public void resetStaticRecordCounter() {
        Record.resetLineCount();
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control03KommunenummerProvider")
    public void control03KommunenummerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control03Kommunenummer(inputAndResult.getErrorReport(), inputAndResult.getRecord()));

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }


    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control03BydelsnummerProvider")
    public void control03BydelsnummerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control03Bydelsnummer(inputAndResult.getErrorReport(), inputAndResult.getRecord()));

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control04OppgaveAarProvider")
    public void control04OppgaveAarTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control04OppgaveAar(inputAndResult.getErrorReport(), inputAndResult.getRecord()));

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05FodselsnummerProvider")
    public void control05FodselsnummerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control05Fodselsnummer(inputAndResult.getErrorReport(), inputAndResult.getRecord()));

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05AFodselsnummerDubletterProvider")
    public void control05AFodselsnummerDubletterTest(TestRecordListInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control05AFodselsnummerDubletter(inputAndResult.getErrorReport(), inputAndResult.getRecordList()));

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05BJournalnummerDubletterProvider")
    public void control05BJournalnummerDubletterTest(TestRecordListInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control05BJournalnummerDubletter(inputAndResult.getErrorReport(), inputAndResult.getRecordList()));

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }


}
