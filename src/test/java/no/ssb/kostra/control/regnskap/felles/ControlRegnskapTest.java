package no.ssb.kostra.control.regnskap.felles;

import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.control.regnskap.kostra.Main;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.Record;
import no.ssb.kostra.utils.TestRecordListInputAndResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static no.ssb.kostra.control.felles.Comparator.removeCodesFromCodelist;
import static no.ssb.kostra.control.regnskap.kostra.Main.getArterUgyldigInvestering;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControlRegnskapTest {
    static Stream<TestRecordListInputAndResult> controlKombinasjonKontoklasseArtInvesteringsregnskapProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0C", "-y", "2020", "-r", "340000"}),
                        List.of(
                                new Record("0C2020 340000                  0880 930   -31768\n", FieldDefinitions.getFieldDefinitions())
                        ), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0C", "-y", "2020", "-r", "340000"}),
                        List.of(
                                new Record("0C2020 340000                  1880 930    -1383\n", FieldDefinitions.getFieldDefinitions())
                        ), false, Constants.NO_ERROR)
        );
    }


    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("controlKombinasjonKontoklasseArtInvesteringsregnskapProvider")
    public void controlKombinasjonKontoklasseArtInvesteringsregnskapTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlRegnskap.controlKombinasjonKontoklasseArt(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList(),
                        List.of("0C"),
                        "0",
                        removeCodesFromCodelist(Main.getArtSektorAsList(inputAndResult.getErrorReport().getArgs().getSkjema(), inputAndResult.getErrorReport().getArgs().getRegion()), getArterUgyldigInvestering()),
                        "formattedControlText art = '%s'",
                        Constants.CRITICAL_ERROR
                ));
        System.out.println(inputAndResult.getErrorReport().generateReport());
    }
}

// boolean controlKombinasjonKontoklasseArtInvesteringsregnskap(ErrorReport er, List<Record> regnskap, List<String> bevilgningRegnskapList, String investeringKontoklasse, List<String> investeringArtList, String formattedControlText, int errorType)

