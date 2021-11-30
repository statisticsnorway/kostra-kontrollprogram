package no.ssb.kostra.control.regnskap.kostra

import no.ssb.kostra.control.felles.Utils
import no.ssb.kostra.control.regnskap.FieldDefinitions
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.Constants
import no.ssb.kostra.felles.ErrorReport
import no.ssb.kostra.felles.FieldDefinition
import no.ssb.kostra.felles.Record
import spock.lang.Specification

import java.util.stream.Collectors

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodelist

class RegnskapKostraSpec extends Specification {
    private static final Arguments arguments = new Arguments(new String[]{"-s", "0A", "-y", "2021", "-r", "420400"})

    private static final Arguments arguments0A = new Arguments(new String[]{"-s", "0A", "-y", "2021", "-r", "420400"})
    private static final Arguments arguments0A_BYDEL = new Arguments(new String[]{"-s", "0A", "-y", "2021", "-r", "030101"})
    private static final Arguments arguments0A_LYBLS = new Arguments(new String[]{"-s", "0A", "-y", "2021", "-r", "211100"})
    private static final Arguments arguments0B = new Arguments(new String[]{"-s", "0B", "-y", "2021", "-r", "420400"})
    private static final Arguments arguments0C = new Arguments(new String[]{"-s", "0C", "-y", "2021", "-r", "420400"})
    private static final Arguments arguments0D = new Arguments(new String[]{"-s", "0D", "-y", "2021", "-r", "420400"})

    private static final Arguments arguments0I_LF = new Arguments(new String[]{"-s", "0I", "-y", "2021", "-r", "420400", "-u", "817920632"})
    private static final Arguments arguments0I_IKS = new Arguments(new String[]{"-s", "0I", "-y", "2021", "-r", "420400", "-u", "999999999"})
    private static final Arguments arguments0I = new Arguments(new String[]{"-s", "0I", "-y", "2021", "-r", "420400"})
    private static final Arguments arguments0J = new Arguments(new String[]{"-s", "0J", "-y", "2021", "-r", "420400"})
    private static final Arguments arguments0K = new Arguments(new String[]{"-s", "0K", "-y", "2021", "-r", "420400"})
    private static final Arguments arguments0L = new Arguments(new String[]{"-s", "0L", "-y", "2021", "-r", "420400"})

    private static final Arguments arguments0M = new Arguments(new String[]{"-s", "0M", "-y", "2021", "-r", "420400"})
    private static final Arguments arguments0N = new Arguments(new String[]{"-s", "0N", "-y", "2021", "-r", "420400"})
    private static final Arguments arguments0P = new Arguments(new String[]{"-s", "0P", "-y", "2021", "-r", "420400"})
    private static final Arguments arguments0Q = new Arguments(new String[]{"-s", "0Q", "-y", "2021", "-r", "420400"})

    private static final List<FieldDefinition> definitions = FieldDefinitions.getFieldDefinitions()

    def "Skal validere at en gitt art er ugyldig i driftsregnskapet', #skjema #orgnr -> #result"() {
        given:
        def arter = Main.getArterUgyldigDrift(skjema, orgnr)

        expect:
        isCodeInCodelist("921", arter) == result

        where:
        skjema | orgnr       || result
        "0A"   | "         " || true
        "0I"   | "999999999" || true
        "0I"   | "817920632" || false
    }

    def "Skal validere Kontroll 20, kontoklasse #kontoklasse / funksjon #funksjon -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon), definitions)))

        when:
        def testResult = Main.kontroll20(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | funksjon || expectedResult | errorlevel
        arguments0A | "0A"   | "1"         | "100 "   || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "1"         | "841 "   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 25, kontoklasse #kontoklasse / art #art -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions)))

        when:
        def testResult = Main.kontroll25(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args            | skjema | kontoklasse | art   || expectedResult | errorlevel
        arguments0A     | "0A"   | "1"         | "921" || true           | Constants.CRITICAL_ERROR
        arguments0I_LF  | "0I"   | "3"         | "921" || false          | Constants.NO_ERROR
        arguments0I_IKS | "0I"   | "3"         | "921" || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 30, kontoklasse #kontoklasse / art #art -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions)))

        when:
        def testResult = Main.kontroll30(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | art   || expectedResult | errorlevel
        arguments0A | "0A"   | "1"         | "100" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "1"         | "285" || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "1"         | "660" || true           | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 35, kontoklasse #kontoklasse / art #art -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions)))

        when:
        def testResult = Main.kontroll35(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | art   || expectedResult | errorlevel
        arguments0A | "0A"   | "1"         | "100" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "1"         | "520" || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "1"         | "920" || true           | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 40, kontoklasse #kontoklasse / funksjon #funksjon -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon), definitions)))

        when:
        def testResult = Main.kontroll40(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | funksjon || expectedResult | errorlevel
        arguments0A | "0A"   | "0"         | "100 "   || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "800 "   || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "850 "   || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "840 "   || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "860 "   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 45, kontoklasse #kontoklasse / funksjon #funksjon -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon), definitions)))

        when:
        def testResult = Main.kontroll45(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | funksjon || expectedResult | errorlevel
        arguments0A | "0A"   | "0"         | "100 "   || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "110 "   || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "121 "   || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "170 "   || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "171 "   || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "400 "   || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "410 "   || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "421 "   || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "470 "   || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "471 "   || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "201 "   || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "510 "   || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 50, kontoklasse #kontoklasse / art #art -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions)))

        when:
        def testResult = Main.kontroll50(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | art   || expectedResult | errorlevel
        arguments0A | "0A"   | "0"         | "010" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "070" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "080" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "110" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "114" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "240" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "509" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "570" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "590" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "600" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "629" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "630" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "640" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "800" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "870" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "874" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "875" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "877" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "909" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "990" || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 55, kontoklasse #kontoklasse / art #art -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions)))

        when:
        def testResult = Main.kontroll55(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | art   || expectedResult | errorlevel
        arguments0A | "0A"   | "0"         | "010" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "620" || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "650" || true           | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "900" || true           | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 60, kontoklasse #kontoklasse / funksjon #funksjon / art #art -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art), definitions)))

        when:
        def testResult = Main.kontroll60(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | funksjon | art   || expectedResult | errorlevel
        arguments0A | "0A"   | "0"         | "100 "   | "729" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "0"         | "841 "   | "010" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "0"         | "841 "   | "729" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "1"         | "100 "   | "729" || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 65, funksjon #funksjon / art #art -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "funksjon_kapittel", funksjon, "art_sektor", art), definitions)))

        when:
        def testResult = Main.kontroll65(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | funksjon | art   || expectedResult | errorlevel
        arguments0A | "0A"   | "899 "   | "010" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "899 "   | "589" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "899 "   | "980" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "899 "   | "989" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "100 "   | "589" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "100 "   | "980" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "100 "   | "989" || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 70, funksjon #funksjon / art #art -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "funksjon_kapittel", funksjon, "art_sektor", art), definitions)))

        when:
        def testResult = Main.kontroll70(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | funksjon | art   || expectedResult | errorlevel
        arguments0A | "0A"   | "899 "   | "530" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "880 "   | "530" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "899 "   | "980" || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 75, funksjon #funksjon / art #art -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "funksjon_kapittel", funksjon, "art_sektor", art), definitions)))

        when:
        def testResult = Main.kontroll75(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | funksjon | art   || expectedResult | errorlevel
        arguments0A | "0A"   | "800 "   | "010" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "800 "   | "870" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "800 "   | "874" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "800 "   | "875" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "800 "   | "877" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "100 "   | "870" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "100 "   | "874" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "100 "   | "875" || true           | Constants.CRITICAL_ERROR
        arguments0A | "0A"   | "100 "   | "877" || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 80, funksjon #funksjon / art #art -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "funksjon_kapittel", funksjon, "art_sektor", art), definitions)))

        when:
        def testResult = Main.kontroll80(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | funksjon | art   || expectedResult | errorlevel
        arguments0A | "0A"   | "840 "   | "010" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "840 "   | "800" || false          | Constants.NO_ERROR
        arguments0A | "0A"   | "100 "   | "800" || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 85 utgiftsposteringer i investeringsregnskapet, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlSumInvesteringsUtgifter(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "0"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "0"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "0"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | "0"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "4"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "4"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "4"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "4"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "4"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "4"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "4"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "4"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "4"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 90 inntektsposteringer i investeringsregnskapet, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlSumInvesteringsInntekter(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "0"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "0"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "0"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | "0"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "4"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "4"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "4"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "4"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "4"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "4"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "4"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "4"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "4"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
    }


    def "Skal validere Kontroll 95 differanse i investeringsregnskapet, skjema #skjema / kontoklasse #kontoklasse / art_belop #art_belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def unnumberedRecords = new ArrayList<Record>()
        art_belop.forEach(a_b -> {
            def record = new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", a_b.get("art"), "belop", a_b.get("belop")), definitions)
            unnumberedRecords.add(record)
        })

        def records = Utils.addLineNumbering(unnumberedRecords)

        when:
        def testResult = Main.controlSumInvesteringsDifferanse(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art_belop                                                                           || expectedResult | errorlevel
        arguments0A       | "0A"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0K       | "0K"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0M       | "0M"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 100 utgiftsposteringer i driftsregnskapet, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlSumDriftsUtgifter(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "1"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "1"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 105 inntektsposteringer i driftsregnskapet, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlSumDriftsInntekter(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "1"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "1"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 110 differanse i driftsregnskapet, skjema #skjema / kontoklasse #kontoklasse / art_belop #art_belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def unnumberedRecords = new ArrayList<Record>()
        art_belop.forEach(a_b -> {
            def record = new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", a_b.get("art"), "belop", a_b.get("belop")), definitions)
            unnumberedRecords.add(record)
        })

        def records = Utils.addLineNumbering(unnumberedRecords)

        when:
        def testResult = Main.controlSumDriftsDifferanse(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art_belop                                                                           || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "1"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 115 registrering av aktiva i balanseregnskapet, skjema #skjema / kontoklasse #kontoklasse / sektor #sektor / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", kapittel, "art_sektor", sektor, "belop", belop), definitions)))

        when:
        def testResult = Main.controlSumAktiva(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | kapittel | sektor | belop || expectedResult | errorlevel
        arguments0B | "0B"   | "2"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        arguments0B | "0B"   | "2"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        arguments0B | "0B"   | "2"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0D | "0D"   | "2"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        arguments0D | "0D"   | "2"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        arguments0D | "0D"   | "2"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0J | "0J"   | "5"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        arguments0J | "0J"   | "5"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        arguments0J | "0J"   | "5"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0L | "0L"   | "5"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        arguments0L | "0L"   | "5"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        arguments0L | "0L"   | "5"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0N | "0N"   | "5"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        arguments0N | "0N"   | "5"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        arguments0N | "0N"   | "5"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0Q | "0Q"   | "5"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        arguments0Q | "0Q"   | "5"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        arguments0Q | "0Q"   | "5"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 120 registrering av passiva i balanseregnskapet, skjema #skjema / kontoklasse #kontoklasse / kapittel #kapittel / sektor #sektor / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", kapittel, "art_sektor", sektor, "belop", belop), definitions)))

        when:
        def testResult = Main.controlSumPassiva(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | kapittel | sektor | belop || expectedResult | errorlevel
        arguments0B | "0B"   | "2"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        arguments0B | "0B"   | "2"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        arguments0B | "0B"   | "2"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0D | "0D"   | "2"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        arguments0D | "0D"   | "2"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        arguments0D | "0D"   | "2"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0J | "0J"   | "5"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        arguments0J | "0J"   | "5"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        arguments0J | "0J"   | "5"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0L | "0L"   | "5"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        arguments0L | "0L"   | "5"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        arguments0L | "0L"   | "5"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0N | "0N"   | "5"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        arguments0N | "0N"   | "5"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        arguments0N | "0N"   | "5"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0Q | "0Q"   | "5"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        arguments0Q | "0Q"   | "5"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        arguments0Q | "0Q"   | "5"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 125 differanse i driftsregnskapet, skjema #skjema / kontoklasse #kontoklasse / kapittel_belop #kapittel_belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def unnumberedRecords = kapittel_belop.stream()
                .map(k_b -> new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", k_b.get("kapittel"), "art_sektor", sektor, "belop", k_b.get("belop")), definitions))
                .collect(Collectors.toList())
        def records = Utils.addLineNumbering(unnumberedRecords)

        when:
        def testResult = Main.controlSumBalanseDifferanse(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args        | skjema | kontoklasse | kapittel_belop                                                                                  | sektor || expectedResult | errorlevel
        arguments0B | "0B"   | "2"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        arguments0B | "0B"   | "2"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
        arguments0D | "0D"   | "2"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        arguments0D | "0D"   | "2"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
        arguments0J | "0J"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        arguments0J | "0J"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
        arguments0L | "0L"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        arguments0L | "0L"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
        arguments0N | "0N"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        arguments0N | "0N"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
        arguments0Q | "0Q"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        arguments0Q | "0Q"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 130 Skatteinntekter , skjema #skjema / kontoklasse #kontoklasse / funksjon #funksjon / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlSkatteInntekter(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "1"         | "800 "   | "870" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "800 "   | "870" | "0"   || false          | Constants.NO_ERROR
        arguments0A_LYBLS | "0A"   | "1"         | "800 "   | "870" | "0"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "800 "   | "870" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "3"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "800 "   | "870" | "0"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "800 "   | "870" | "0"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "800 "   | "870" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "3"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "800 "   | "870" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 135 Rammetilskudd , skjema #skjema / kontoklasse #kontoklasse / funksjon #funksjon / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlRammetilskudd(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "1"         | "840 "   | "800" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "840 "   | "800" | "0"   || false          | Constants.NO_ERROR
        arguments0A_LYBLS | "0A"   | "1"         | "840 "   | "800" | "0"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "840 "   | "800" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "3"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "840 "   | "800" | "0"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "840 "   | "800" | "0"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "840 "   | "800" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "3"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "840 "   | "800" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 140 Overforing mellom drifts- og investeringsregnskap, skjema #skjema / list #list -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def unnumberedRecords = list.stream()
                .map(l -> new Record(Map.of("skjema", skjema, "kontoklasse", l.get("kontoklasse"), "funksjon_kapittel", l.get("funksjon"), "art_sektor", l.get("art"), "belop", l.get("belop")), definitions))
                .collect(Collectors.toList())
        def records = Utils.addLineNumbering(unnumberedRecords)

        when:
        def testResult = Main.controlOverforingMellomDriftOgInvestering(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | list                                                                                                                                                                || expectedResult | errorlevel
        arguments0A       | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "0", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "0", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "0", "funksjon", "100 ", "art", "970", "belop", "-1000")) || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "0", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "0", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0K       | "0K"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0M       | "0M"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 145 Avskrivninger, motpost avskrivninger, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlMotpostAvskrivninger(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "1"         | "860 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "860 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "860 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "3"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "860 "   | "990" | "0"   || true           | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "860 "   | "990" | "0"   || true           | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "860 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "3"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "860 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 150 Avskrivninger, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlAvskrivninger(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "1"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 155 Avskrivninger, differanse, skjema #skjema / list #list -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def unnumberedRecords = list.stream()
                .map(l -> new Record(Map.of("skjema", skjema, "kontoklasse", l.get("kontoklasse"), "funksjon_kapittel", l.get("funksjon"), "art_sektor", l.get("art"), "belop", l.get("belop")), definitions))
                .collect(Collectors.toList())
        def records = Utils.addLineNumbering(unnumberedRecords)

        when:
        def testResult = Main.controlAvskrivningerDifferanse(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | list                                                                                                                                                                || expectedResult | errorlevel
        arguments0A       | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "1", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "1", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0A_BYDEL | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "1", "funksjon", "860 ", "art", "990", "belop", "-1000")) || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "1", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "1", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0K       | "0K"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0M       | "0M"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 160 Avskrivninger, avskrivninger fort pa andre funksjoner, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlAvskrivningerAndreFunksjoner(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0A       | "0A"   | "1"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "800 "   | "590" | "1"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0C       | "0C"   | "1"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "3"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0K       | "0K"   | "3"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0M       | "0M"   | "3"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "3"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 165 Avskrivninger, motpost avskrivninger fort pa andre funksjoner, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlAvskrivningerMotpostAndreFunksjoner(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0A       | "0A"   | "1"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "100 "   | "990" | "1"   || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0C       | "0C"   | "1"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0I       | "0I"   | "3"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0K       | "0K"   | "3"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0M       | "0M"   | "3"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "3"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 170 Funksjon 290, investeringsregnskapet, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlFunksjon290Investering(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "0"         | "290 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        arguments0A       | "0A"   | "0"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0A_BYDEL | "0A"   | "0"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "4"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "4"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "4"         | "290 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        arguments0M       | "0M"   | "4"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "4"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "4"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 175 Funksjon 290, driftsregnskapet, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlFunksjon290Drift(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())
        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "290 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        arguments0A       | "0A"   | "1"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "290 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        arguments0M       | "0M"   | "3"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 180 Funksjon 465, investeringsregnskapet, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlFunksjon465Investering(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "0"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "0"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0A_BYDEL | "0A"   | "0"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "0"         | "465 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        arguments0C       | "0C"   | "0"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "4"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "4"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "4"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "4"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "4"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "4"         | "465 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "4"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 185 Funksjon 465, driftsregnskapet, skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def records = Utils.addLineNumbering(List.of(new Record(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

        when:
        def testResult = Main.controlFunksjon465Drift(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        arguments0A       | "0A"   | "1"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0A       | "0A"   | "1"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0A_BYDEL | "0A"   | "1"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0C       | "0C"   | "1"         | "465 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        arguments0C       | "0C"   | "1"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0I       | "0I"   | "3"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0K       | "0K"   | "3"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        arguments0M       | "0M"   | "3"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        arguments0P       | "0P"   | "3"         | "465 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        arguments0P       | "0P"   | "3"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
    }


    def "Skal validere Kontroll 190 Memoriakonti, skjema #skjema / list #list -> #expectedResult / #errorlevel"() {
        given:
        def errorReport = new ErrorReport(args)
        def unnumberedRecords = list.stream()
                .map(l -> new Record(Map.of("skjema", skjema, "kontoklasse", l.get("kontoklasse"), "funksjon_kapittel", l.get("kapittel"), "art_sektor", l.get("sektor"), "belop", l.get("belop")), definitions))
                .collect(Collectors.toList())
        def records = Utils.addLineNumbering(unnumberedRecords)

        when:
        def testResult = Main.controlMemoriaKonti(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        args              | skjema | list                                                                                                                                                                || expectedResult | errorlevel
        arguments0B       | "0B"   | List.of(Map.of("kontoklasse", "2", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "2", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0B       | "0B"   | List.of(Map.of("kontoklasse", "2", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "2", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NORMAL_ERROR
        arguments0D       | "0D"   | List.of(Map.of("kontoklasse", "2", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "2", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0D       | "0D"   | List.of(Map.of("kontoklasse", "2", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "2", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NORMAL_ERROR
        arguments0J       | "0J"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0J       | "0J"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NORMAL_ERROR
        arguments0L       | "0L"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0L       | "0L"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NORMAL_ERROR
        arguments0N       | "0N"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0N       | "0N"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NORMAL_ERROR
        arguments0Q       | "0Q"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        arguments0Q       | "0Q"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NORMAL_ERROR
    }


}

