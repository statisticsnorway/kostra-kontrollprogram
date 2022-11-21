package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.utils.TestStringListInputAndResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControlFelt1ListeBlankTest {
    private ErrorReport errorReport;

    static Stream<TestStringListInputAndResult> inputDoControlProvider() {
        return Stream.of(
                new TestStringListInputAndResult(List.of("", "", "", "123"), true),
                new TestStringListInputAndResult(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), true),
                new TestStringListInputAndResult(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), true),
                new TestStringListInputAndResult(List.of("", "", "", ""), false)
        );
    }

    @BeforeEach
    public void beforeEachTest() {
        var args0F = new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"});
        errorReport = new ErrorReport(args0F);
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputDoControlProvider")
    public void doControlTest(TestStringListInputAndResult inputAndResult) {
        final var result =  ControlFelt1ListeBlank.doControl(er, "Categori Title", "Title", inputAndResult.getStringList(), Constants.CRITICAL_ERROR);

        if (Constants.DEBUG)
            System.out.println(er.generateReport());

        assertEquals(inputAndResult.isResult(), result);
    }

}
