package no.ssb.kostra.control.regnskap.kostra;

import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.FieldDefinition;
import no.ssb.kostra.felles.Record;
import no.ssb.kostra.utils.TestRecordListInputAndResult;
import no.ssb.kostra.utils.TestStringListInputAndResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodelist;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainITestKostraRegnskap {
    private static final Arguments arguments0A = new Arguments(new String[]{"-s", "0A", "-y", "2021", "-r", "420400"});
    private static final Arguments arguments0B = new Arguments(new String[]{"-s", "0B", "-y", "2021", "-r", "420400"});
    private static final Arguments arguments0C = new Arguments(new String[]{"-s", "0C", "-y", "2021", "-r", "420400"});
    private static final Arguments arguments0D = new Arguments(new String[]{"-s", "0D", "-y", "2021", "-r", "420400"});

    private static final Arguments arguments0I_1 = new Arguments(new String[]{"-s", "0I", "-y", "2021", "-r", "420400", "-u", "817920632"});
    private static final Arguments arguments0I_2 = new Arguments(new String[]{"-s", "0I", "-y", "2021", "-r", "420400", "-u", "999999999"});
    private static final Arguments arguments0J = new Arguments(new String[]{"-s", "0J", "-y", "2021", "-r", "420400"});
    private static final Arguments arguments0K = new Arguments(new String[]{"-s", "0K", "-y", "2021", "-r", "420400"});
    private static final Arguments arguments0L = new Arguments(new String[]{"-s", "0L", "-y", "2021", "-r", "420400"});

    private static final Arguments arguments0M = new Arguments(new String[]{"-s", "0M", "-y", "2021", "-r", "420400"});
    private static final Arguments arguments0N = new Arguments(new String[]{"-s", "0N", "-y", "2021", "-r", "420400"});
    private static final Arguments arguments0P = new Arguments(new String[]{"-s", "0P", "-y", "2021", "-r", "420400"});
    private static final Arguments arguments0Q = new Arguments(new String[]{"-s", "0Q", "-y", "2021", "-r", "420400"});

    private static final List<FieldDefinition> definitions = FieldDefinitions.getFieldDefinitions();

    static Stream<TestStringListInputAndResult> getArterUgyldigDriftProvider() {
        return Stream.of(
                new TestStringListInputAndResult(List.of("0A", "         "), true),
                new TestStringListInputAndResult(List.of("0I", "999999999"), true),
                new TestStringListInputAndResult(List.of("0I", "817920632"), false)
        );
    }

    static Stream<TestRecordListInputAndResult> control20Provider() {
        return Stream.of(
                new TestRecordListInputAndResult(arguments0A, List.of(new Record(Map.of("skjema", "0A", "kontoklasse", "1", "funksjon_kapittel", "841 "), definitions)), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(arguments0A, List.of(new Record(Map.of("skjema", "0A", "kontoklasse", "1", "funksjon_kapittel", "100 "), definitions)), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> control25Provider() {
        return Stream.of(
                new TestRecordListInputAndResult(arguments0A, List.of(new Record(Map.of("skjema", "0A", "kontoklasse", "1", "art_sektor", "921"), definitions)), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(arguments0I_1, List.of(new Record(Map.of("skjema", "0I", "kontoklasse", "3", "art_sektor", "921"), definitions)), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(arguments0I_2, List.of(new Record(Map.of("skjema", "0I", "kontoklasse", "3", "art_sektor", "921"), definitions)), true, Constants.CRITICAL_ERROR)
        );
    }

    @AfterEach
    public void resetStaticRecordCounter() {
        Record.resetLineCount();
    }


    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("getArterUgyldigDriftProvider")
    public void getArterUgyldigDriftTest(TestStringListInputAndResult inputAndResult) {
        List<String> arter = Main.getArterUgyldigDrift(inputAndResult.getStringList().get(0), inputAndResult.getStringList().get(1));
        System.out.println(arter);
        boolean result = isCodeInCodelist("921", arter);
        assertEquals(inputAndResult.isResult(), result);
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control25Provider")
    public void control25TestControl(TestRecordListInputAndResult inputAndResult) {
        boolean result = Main.kontroll25(inputAndResult.getErrorReport(), inputAndResult.getRecordList());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control20Provider")
    public void control20TestControl(TestRecordListInputAndResult inputAndResult) {
        boolean result = Main.kontroll20(inputAndResult.getErrorReport(), inputAndResult.getRecordList());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

    }
}
