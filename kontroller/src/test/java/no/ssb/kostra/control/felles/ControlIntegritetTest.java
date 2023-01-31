package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.KostraRecord;
import no.ssb.kostra.utils.TestRecordListInputAndResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControlIntegritetTest {
    static Stream<TestRecordListInputAndResult> inputSkjemaProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0G2000 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F                                              ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputAargangProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2000 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("  2020                                          ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputKvartalProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("      9                                         ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F20009300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputRegionProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 303030976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("       300500                                   ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputOrganisasjonsnummerProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 303030987654321         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("             976989732                          ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputForetaksnummerProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                      999999999                 ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 3030309769897329876543213041 650    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputKontoklasseProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 303030976989732         9041 650    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                               3                ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputFunksjonProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 303030976989732         9abc 650    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                041             ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputArtProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 303030976989732         9041 abc    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 650    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                    650         ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputKapittelProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 303030976989732         9041 abc    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         31234650    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                1234            ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputSektorProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 303030976989732         9041 abc    -8695", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 123    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                    123         ", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> inputBelopProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                                ", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("************************************************", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 303030976989732         9041 abc      abc", FieldDefinitions.getFieldDefinitions())), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("0F2020 300500976989732         3041 123    -8695", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                             123", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(new Arguments(new String[]{"-s", "0F", "-y", "2020", "-r", "300500", "-u", "976989732"}), List.of(new KostraRecord("                                               0", FieldDefinitions.getFieldDefinitions())), false, Constants.NO_ERROR)
        );
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputSkjemaProvider")
    public void controlSkjemaTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlSkjema(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList()));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputAargangProvider")
    public void controlAargangTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlAargang(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList()));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputKvartalProvider")
    public void controlKvartalTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlKvartal(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList()));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputRegionProvider")
    public void controlRegionTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlRegion(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList()));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputOrganisasjonsnummerProvider")
    public void controlOrganisasjonsnummerTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlOrganisasjonsnummer(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList()));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputForetaksnummerProvider")
    public void controlForetaksnummerTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlForetaksnummer(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList()));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputKontoklasseProvider")
    public void controlKontoklasseTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlKontoklasse(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList(),
                        List.of("3")));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputFunksjonProvider")
    public void controlFunksjonTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlFunksjon(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList(),
                        List.of("041 ")));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputArtProvider")
    public void controlArtTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlArt(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList(),
                        List.of("650")));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputKapittelProvider")
    public void controlKapittelTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlKapittel(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList(),
                        List.of("1234")));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputSektorProvider")
    public void controlSektorTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlSektor(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList(),
                        List.of("123")));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputBelopProvider")
    public void controlBelopTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlBelop(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList()));
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("inputBelopProvider")
    public void controlUgyldigeBelopTest(TestRecordListInputAndResult inputAndResult) {
        assertEquals(
                inputAndResult.isResult(),
                ControlIntegritet.controlUgyldigeBelop(
                        inputAndResult.getErrorReport(),
                        inputAndResult.getRecordList()));
    }
}
