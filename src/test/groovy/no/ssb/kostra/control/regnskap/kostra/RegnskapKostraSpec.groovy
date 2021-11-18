package no.ssb.kostra.control.regnskap.kostra

import no.ssb.kostra.control.regnskap.FieldDefinitions
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.Constants
import no.ssb.kostra.felles.ErrorReport
import no.ssb.kostra.felles.FieldDefinition
import no.ssb.kostra.felles.Record
import spock.lang.Specification

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodelist

class RegnskapKostraSpec extends Specification {
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

    def "Skal validere at en gitt art er ugyldig i driftsregnskapet', #skjema #orgnr -> #result"() {
        given:
        def arter = Main.getArterUgyldigDrift(skjema, orgnr)

        expect:
        isCodeInCodelist("921", arter) == result.toBoolean()

        where:
        skjema | orgnr       || result
        "0A"   | "         " || true
        "0I"   | "999999999" || true
        "0I"   | "817920632" || false
    }

    def "Skal validere Kontroll 20"() {
        given:
        def args = arguments0A
        def er = new ErrorReport(args)
        def recordList = List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon), definitions))

        expect:
        verifyAll {
            Main.kontroll20(er, recordList) == result.toBoolean()
            er.getErrorType() == errorlevel.intValue()
        }
        System.out.println(er.generateReport())


        where:
        skjema | kontoklasse | funksjon || result | errorlevel
        "0A"   | "1"         | "100 "   || false  | Constants.NO_ERROR
        "0A"   | "1"         | "841 "   || true   | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 25"() {
        given:
        def er = new ErrorReport(args)
        def recordList = List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions))

        expect:
        verifyAll {
            Main.kontroll25(er, recordList) == result.toBoolean()
            er.getErrorType() == errorlevel.intValue()
        }
        System.out.println(er.generateReport())


        where:
        args          | skjema | kontoklasse | art   || result | errorlevel
        arguments0A   | "0A"   | "1"         | "921" || true   | Constants.CRITICAL_ERROR
        arguments0I_1 | "0I"   | "3"         | "921" || false  | Constants.NO_ERROR
        arguments0I_2 | "0I"   | "3"         | "921" || true   | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 60"() {
        given:
        def args = arguments0A
        def er = new ErrorReport(args)
        def recordList = List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon,"art_sektor", art), definitions))

        expect:
        verifyAll {
            Main.kontroll60(er, recordList) == result.toBoolean()
            er.getErrorType() == errorlevel.intValue()
        }
        System.out.println(er.generateReport())

        where:
        skjema | kontoklasse | funksjon | art   || result | errorlevel
        "0A"   | "0"         | "100 "   | "729" || true   | Constants.CRITICAL_ERROR
        "0A"   | "0"         | "841 "   | "010" || false  | Constants.NO_ERROR
        "0A"   | "0"         | "841 "   | "729" || false  | Constants.NO_ERROR
        "0A"   | "1"         | "100 "   | "729" || false  | Constants.NO_ERROR
    }

    def "Skal validere Kontroll XX"() {
        given:
        def args = arguments0A
        def er = new ErrorReport(args)

        expect:
        Main.getKontoklasseAsMap(er.getArgs().getSkjema()).get("I").equalsIgnoreCase("0")

    }

}


/*
    static Stream<TestRecordListInputAndResult> control60Provider() {
        return Stream.of(
                new TestRecordListInputAndResult(arguments0A, List.of(new Record(Map.of("skjema", "0A", "kontoklasse", "0", "funksjon_kapittel", "100 ", "art_sektor", "729"), definitions)), true, Constants.CRITICAL_ERROR),
                new TestRecordListInputAndResult(arguments0A, List.of(new Record(Map.of("skjema", "0A", "kontoklasse", "0", "funksjon_kapittel", "841 ", "art_sektor", "729"), definitions)), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(arguments0A, List.of(new Record(Map.of("skjema", "0A", "kontoklasse", "0", "funksjon_kapittel", "841 ", "art_sektor", "010"), definitions)), false, Constants.NO_ERROR),
                new TestRecordListInputAndResult(arguments0A, List.of(new Record(Map.of("skjema", "0A", "kontoklasse", "1", "funksjon_kapittel", "100 ", "art_sektor", "729"), definitions)), false, Constants.NO_ERROR)
        );
    }
    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control60Provider")
    public void control60TestControl(TestRecordListInputAndResult inputAndResult) {
        boolean result = Main.kontroll60(inputAndResult.getErrorReport(), inputAndResult.getRecordList());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

    }


*/