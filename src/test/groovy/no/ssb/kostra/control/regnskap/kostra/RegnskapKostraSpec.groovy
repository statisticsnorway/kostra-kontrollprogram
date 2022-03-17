package no.ssb.kostra.control.regnskap.kostra

import no.ssb.kostra.control.felles.Utils
import no.ssb.kostra.control.regnskap.FieldDefinitions
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.Constants
import no.ssb.kostra.felles.ErrorReport
import no.ssb.kostra.felles.FieldDefinition
import no.ssb.kostra.felles.KostraRecord
import spock.lang.Specification

import java.util.stream.Collectors

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodeList

class RegnskapKostraSpec extends Specification {
    private static final String yyyy = "2021"
    private static final List<FieldDefinition> definitions = FieldDefinitions.getFieldDefinitions()

    def "Skal validere at en gitt art er ugyldig i driftsregnskapet', skjema #skjema / orgnr #orgnr / region #region / art #art -> #result"() {
        given:
        def arter = Main.getArterUgyldigDrift(skjema, orgnr, region)

        expect:
        isCodeInCodeList(art, arter) == result

        where:
        skjema | orgnr       | region   | art   || result
        "0A"   | "         " | "420400" | "921" || true
        "0A"   | "         " | "420400" | "280" || true
        "0C"   | "         " | "500000" | "921" || true
        "0C"   | "         " | "500000" | "280" || true
        "0I"   | "999999999" | "420400" | "921" || true
        "0I"   | "817920632" | "500000" | "921" || false
        "0I"   | "999999999" | "420400" | "280" || true
        "0I"   | "817920632" | "500000" | "280" || true
        "0K"   | "999999999" | "420400" | "921" || true
        "0K"   | "817920632" | "500000" | "921" || false
        "0K"   | "999999999" | "420400" | "280" || true
        "0K"   | "817920632" | "500000" | "280" || true
        "0M"   | "         " | "420400" | "921" || true
        "0M"   | "         " | "500000" | "921" || false
        "0M"   | "         " | "420400" | "280" || true
        "0M"   | "         " | "500000" | "280" || true
        "0P"   | "         " | "420400" | "921" || true
        "0P"   | "         " | "500000" | "921" || false
        "0P"   | "         " | "420400" | "280" || true
        "0P"   | "         " | "500000" | "280" || true
    }

    def "Skal validere Kontroll 20, skjema #skjema / kontoklasse #kontoklasse / funksjon #funksjon -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon), definitions)))

        when:
        boolean testResult = Main.kontroll20(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        skjema | kontoklasse | funksjon || expectedResult | errorlevel
        "0A"   | "1"         | "100 "   || false          | Constants.NO_ERROR
        "0A"   | "1"         | "841 "   || true           | Constants.CRITICAL_ERROR
        "0C"   | "1"         | "100 "   || false          | Constants.NO_ERROR
        "0C"   | "1"         | "841 "   || true           | Constants.CRITICAL_ERROR
        "0I"   | "3"         | "100 "   || false          | Constants.NO_ERROR
        "0I"   | "3"         | "841 "   || true           | Constants.CRITICAL_ERROR
        "0K"   | "3"         | "100 "   || false          | Constants.NO_ERROR
        "0K"   | "3"         | "841 "   || true           | Constants.CRITICAL_ERROR
        "0M"   | "3"         | "100 "   || false          | Constants.NO_ERROR
        "0M"   | "3"         | "841 "   || true           | Constants.CRITICAL_ERROR
        "0P"   | "3"         | "100 "   || false          | Constants.NO_ERROR
        "0P"   | "3"         | "841 "   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 25, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / orgnr #orgnr -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region, "-u", orgnr})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions)))

        when:
        boolean testResult = Main.kontroll25(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        region   | skjema | kontoklasse | art   | orgnr       || expectedResult | errorlevel
        "030100" | "0A"   | "1"         | "010" | "         " || false          | Constants.NO_ERROR
        "030100" | "0A"   | "1"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "030100" | "0A"   | "1"         | "921" | "         " || true           | Constants.CRITICAL_ERROR
        "030100" | "0C"   | "1"         | "010" | "         " || false          | Constants.NO_ERROR
        "030100" | "0C"   | "1"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "030100" | "0C"   | "1"         | "921" | "         " || true           | Constants.CRITICAL_ERROR
        "030100" | "0I"   | "3"         | "010" | "958935420" || false          | Constants.NO_ERROR
        "030100" | "0I"   | "3"         | "280" | "958935420" || true           | Constants.CRITICAL_ERROR
        "030100" | "0I"   | "3"         | "921" | "958935420" || false          | Constants.NO_ERROR
        "030100" | "0K"   | "3"         | "010" | "958935420" || false          | Constants.NO_ERROR
        "030100" | "0K"   | "3"         | "280" | "958935420" || true           | Constants.CRITICAL_ERROR
        "030100" | "0K"   | "3"         | "921" | "958935420" || false          | Constants.NO_ERROR
        "030100" | "0M"   | "3"         | "010" | "         " || false          | Constants.NO_ERROR
        "030100" | "0M"   | "3"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "030100" | "0M"   | "3"         | "921" | "         " || false          | Constants.NO_ERROR
        "030100" | "0P"   | "3"         | "010" | "         " || false          | Constants.NO_ERROR
        "030100" | "0P"   | "3"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "030100" | "0P"   | "3"         | "921" | "         " || false          | Constants.NO_ERROR
        "300500" | "0A"   | "1"         | "010" | "         " || false          | Constants.NO_ERROR
        "300500" | "0A"   | "1"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "300500" | "0A"   | "1"         | "921" | "         " || true           | Constants.CRITICAL_ERROR
        "300500" | "0C"   | "1"         | "010" | "         " || false          | Constants.NO_ERROR
        "300500" | "0C"   | "1"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "300500" | "0C"   | "1"         | "921" | "         " || true           | Constants.CRITICAL_ERROR
        "300500" | "0I"   | "3"         | "010" | "921234554" || false          | Constants.NO_ERROR
        "300500" | "0I"   | "3"         | "280" | "921234554" || true           | Constants.CRITICAL_ERROR
        "300500" | "0I"   | "3"         | "921" | "921234554" || false          | Constants.NO_ERROR
        "300500" | "0K"   | "3"         | "010" | "921234554" || false          | Constants.NO_ERROR
        "300500" | "0K"   | "3"         | "280" | "921234554" || true           | Constants.CRITICAL_ERROR
        "300500" | "0K"   | "3"         | "921" | "921234554" || false          | Constants.NO_ERROR
        "300500" | "0M"   | "3"         | "010" | "         " || false          | Constants.NO_ERROR
        "300500" | "0M"   | "3"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "300500" | "0M"   | "3"         | "921" | "         " || false          | Constants.NO_ERROR
        "300500" | "0P"   | "3"         | "010" | "         " || false          | Constants.NO_ERROR
        "300500" | "0P"   | "3"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "300500" | "0P"   | "3"         | "921" | "         " || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "010" | "         " || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "420400" | "0A"   | "1"         | "921" | "         " || true           | Constants.CRITICAL_ERROR
        "420400" | "0C"   | "1"         | "010" | "         " || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "420400" | "0C"   | "1"         | "921" | "         " || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "010" | "999999999" || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "280" | "999999999" || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "921" | "999999999" || true           | Constants.CRITICAL_ERROR
        "420400" | "0K"   | "3"         | "010" | "999999999" || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "280" | "999999999" || true           | Constants.CRITICAL_ERROR
        "420400" | "0K"   | "3"         | "921" | "999999999" || true           | Constants.CRITICAL_ERROR
        "420400" | "0M"   | "3"         | "010" | "         " || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "420400" | "0M"   | "3"         | "921" | "         " || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "010" | "         " || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "280" | "         " || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "921" | "         " || true           | Constants.CRITICAL_ERROR

//        "300500" | "921234554" | "0I"   | "3"         | "921" || false          | Constants.NO_ERROR
//        "460100" | "964338531" | "0I"   | "3"         | "921" || false          | Constants.NO_ERROR
//        "030100" | "         " | "0M"   | "3"         | "921" || false          | Constants.NO_ERROR
//        "300500" | "         " | "0M"   | "3"         | "921" || false          | Constants.NO_ERROR
//        "460100" | "         " | "0M"   | "3"         | "921" || false          | Constants.NO_ERROR
//        "500000" | "         " | "0P"   | "3"         | "921" || false          | Constants.NO_ERROR

    }

    def "Skal validere Kontroll 30, skjema #skjema / kontoklasse #kontoklasse / art #art -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions)))

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
        skjema | kontoklasse | art   || expectedResult | errorlevel
        "0A"   | "1"         | "100" || false          | Constants.NO_ERROR
        "0A"   | "1"         | "285" || true           | Constants.NO_ERROR
        "0A"   | "1"         | "660" || true           | Constants.NO_ERROR
        "0C"   | "1"         | "100" || false          | Constants.NO_ERROR
        "0C"   | "1"         | "285" || true           | Constants.NO_ERROR
        "0C"   | "1"         | "660" || true           | Constants.NO_ERROR
        "0I"   | "3"         | "100" || false          | Constants.NO_ERROR
        "0I"   | "3"         | "285" || true           | Constants.NO_ERROR
        "0I"   | "3"         | "660" || true           | Constants.NO_ERROR
        "0K"   | "3"         | "100" || false          | Constants.NO_ERROR
        "0K"   | "3"         | "285" || true           | Constants.NO_ERROR
        "0K"   | "3"         | "660" || true           | Constants.NO_ERROR
        "0M"   | "3"         | "100" || false          | Constants.NO_ERROR
        "0M"   | "3"         | "285" || true           | Constants.NO_ERROR
        "0M"   | "3"         | "660" || true           | Constants.NO_ERROR
        "0P"   | "3"         | "100" || false          | Constants.NO_ERROR
        "0P"   | "3"         | "285" || true           | Constants.NO_ERROR
        "0P"   | "3"         | "660" || true           | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 35, skjema #skjema / kontoklasse #kontoklasse / art #art -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions)))

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
        skjema | kontoklasse | art   || expectedResult | errorlevel
        "0A"   | "1"         | "100" || false          | Constants.NO_ERROR
        "0A"   | "1"         | "520" || true           | Constants.NO_ERROR
        "0A"   | "1"         | "920" || true           | Constants.NO_ERROR
        "0C"   | "1"         | "100" || false          | Constants.NO_ERROR
        "0C"   | "1"         | "520" || true           | Constants.NO_ERROR
        "0C"   | "1"         | "920" || true           | Constants.NO_ERROR
        "0I"   | "3"         | "100" || false          | Constants.NO_ERROR
        "0I"   | "3"         | "520" || true           | Constants.NO_ERROR
        "0I"   | "3"         | "920" || true           | Constants.NO_ERROR
        "0K"   | "3"         | "100" || false          | Constants.NO_ERROR
        "0K"   | "3"         | "520" || true           | Constants.NO_ERROR
        "0K"   | "3"         | "920" || true           | Constants.NO_ERROR
        "0M"   | "3"         | "100" || false          | Constants.NO_ERROR
        "0M"   | "3"         | "520" || true           | Constants.NO_ERROR
        "0M"   | "3"         | "920" || true           | Constants.NO_ERROR
        "0P"   | "3"         | "100" || false          | Constants.NO_ERROR
        "0P"   | "3"         | "520" || true           | Constants.NO_ERROR
        "0P"   | "3"         | "920" || true           | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 40, skjema #skjema / kontoklasse #kontoklasse / funksjon #funksjon -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon), definitions)))

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
        skjema | kontoklasse | funksjon || expectedResult | errorlevel
        "0A"   | "0"         | "100 "   || false          | Constants.NO_ERROR
        "0A"   | "0"         | "800 "   || true           | Constants.CRITICAL_ERROR
        "0A"   | "0"         | "850 "   || true           | Constants.CRITICAL_ERROR
        "0A"   | "0"         | "840 "   || true           | Constants.CRITICAL_ERROR
        "0A"   | "0"         | "860 "   || true           | Constants.CRITICAL_ERROR
        "0C"   | "0"         | "100 "   || false          | Constants.NO_ERROR
        "0C"   | "0"         | "800 "   || true           | Constants.CRITICAL_ERROR
        "0C"   | "0"         | "850 "   || true           | Constants.CRITICAL_ERROR
        "0C"   | "0"         | "840 "   || true           | Constants.CRITICAL_ERROR
        "0C"   | "0"         | "860 "   || true           | Constants.CRITICAL_ERROR
        "0I"   | "4"         | "100 "   || false          | Constants.NO_ERROR
        "0I"   | "4"         | "800 "   || true           | Constants.CRITICAL_ERROR
        "0I"   | "4"         | "850 "   || true           | Constants.CRITICAL_ERROR
        "0I"   | "4"         | "840 "   || true           | Constants.CRITICAL_ERROR
        "0I"   | "4"         | "860 "   || true           | Constants.CRITICAL_ERROR
        "0K"   | "4"         | "100 "   || false          | Constants.NO_ERROR
        "0K"   | "4"         | "800 "   || true           | Constants.CRITICAL_ERROR
        "0K"   | "4"         | "850 "   || true           | Constants.CRITICAL_ERROR
        "0K"   | "4"         | "840 "   || true           | Constants.CRITICAL_ERROR
        "0K"   | "4"         | "860 "   || true           | Constants.CRITICAL_ERROR
        "0M"   | "4"         | "100 "   || false          | Constants.NO_ERROR
        "0M"   | "4"         | "800 "   || true           | Constants.CRITICAL_ERROR
        "0M"   | "4"         | "850 "   || true           | Constants.CRITICAL_ERROR
        "0M"   | "4"         | "840 "   || true           | Constants.CRITICAL_ERROR
        "0M"   | "4"         | "860 "   || true           | Constants.CRITICAL_ERROR
        "0P"   | "4"         | "100 "   || false          | Constants.NO_ERROR
        "0P"   | "4"         | "800 "   || true           | Constants.CRITICAL_ERROR
        "0P"   | "4"         | "850 "   || true           | Constants.CRITICAL_ERROR
        "0P"   | "4"         | "840 "   || true           | Constants.CRITICAL_ERROR
        "0P"   | "4"         | "860 "   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 45, skjema #skjema / kontoklasse #kontoklasse / funksjon #funksjon -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon), definitions)))

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
        skjema | kontoklasse | funksjon || expectedResult | errorlevel
        "0A"   | "0"         | "100 "   || true           | Constants.NO_ERROR
        "0A"   | "0"         | "400 "   || true           | Constants.NO_ERROR
        "0A"   | "0"         | "201 "   || false          | Constants.NO_ERROR
        "0A"   | "0"         | "510 "   || false          | Constants.NO_ERROR
        "0C"   | "0"         | "100 "   || true           | Constants.NO_ERROR
        "0C"   | "0"         | "400 "   || true           | Constants.NO_ERROR
        "0C"   | "0"         | "201 "   || false          | Constants.NO_ERROR
        "0C"   | "0"         | "510 "   || false          | Constants.NO_ERROR
        "0I"   | "4"         | "100 "   || true           | Constants.NO_ERROR
        "0I"   | "4"         | "400 "   || true           | Constants.NO_ERROR
        "0I"   | "4"         | "201 "   || false          | Constants.NO_ERROR
        "0I"   | "4"         | "510 "   || false          | Constants.NO_ERROR
        "0K"   | "4"         | "100 "   || true           | Constants.NO_ERROR
        "0K"   | "4"         | "400 "   || true           | Constants.NO_ERROR
        "0K"   | "4"         | "201 "   || false          | Constants.NO_ERROR
        "0K"   | "4"         | "510 "   || false          | Constants.NO_ERROR
        "0M"   | "4"         | "100 "   || true           | Constants.NO_ERROR
        "0M"   | "4"         | "400 "   || true           | Constants.NO_ERROR
        "0M"   | "4"         | "201 "   || false          | Constants.NO_ERROR
        "0M"   | "4"         | "510 "   || false          | Constants.NO_ERROR
        "0P"   | "4"         | "100 "   || true           | Constants.NO_ERROR
        "0P"   | "4"         | "400 "   || true           | Constants.NO_ERROR
        "0P"   | "4"         | "201 "   || false          | Constants.NO_ERROR
        "0P"   | "4"         | "510 "   || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 50, skjema #skjema / kontoklasse #kontoklasse / art #art -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions)))

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
        skjema | kontoklasse | art   || expectedResult | errorlevel
        "0A"   | "0"         | "010" || false          | Constants.NO_ERROR
        "0A"   | "0"         | "070" || true           | Constants.CRITICAL_ERROR
        "0A"   | "0"         | "990" || true           | Constants.CRITICAL_ERROR
        "0C"   | "0"         | "010" || false          | Constants.NO_ERROR
        "0C"   | "0"         | "070" || true           | Constants.CRITICAL_ERROR
        "0C"   | "0"         | "990" || true           | Constants.CRITICAL_ERROR
        "0I"   | "4"         | "010" || false          | Constants.NO_ERROR
        "0I"   | "4"         | "070" || true           | Constants.CRITICAL_ERROR
        "0I"   | "4"         | "990" || true           | Constants.CRITICAL_ERROR
        "0K"   | "4"         | "010" || false          | Constants.NO_ERROR
        "0K"   | "4"         | "070" || true           | Constants.CRITICAL_ERROR
        "0K"   | "4"         | "990" || true           | Constants.CRITICAL_ERROR
        "0M"   | "4"         | "010" || false          | Constants.NO_ERROR
        "0M"   | "4"         | "070" || true           | Constants.CRITICAL_ERROR
        "0M"   | "4"         | "990" || true           | Constants.CRITICAL_ERROR
        "0P"   | "4"         | "010" || false          | Constants.NO_ERROR
        "0P"   | "4"         | "070" || true           | Constants.CRITICAL_ERROR
        "0P"   | "4"         | "990" || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 55, skjema #skjema / kontoklasse #kontoklasse / art #art -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "art_sektor", art), definitions)))

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
        skjema | kontoklasse | art   || expectedResult | errorlevel
        "0A"   | "0"         | "010" || false          | Constants.NO_ERROR
        "0A"   | "0"         | "620" || true           | Constants.NO_ERROR
        "0A"   | "0"         | "650" || true           | Constants.NO_ERROR
        "0A"   | "0"         | "900" || true           | Constants.NO_ERROR
        "0C"   | "0"         | "010" || false          | Constants.NO_ERROR
        "0C"   | "0"         | "620" || true           | Constants.NO_ERROR
        "0C"   | "0"         | "650" || true           | Constants.NO_ERROR
        "0C"   | "0"         | "900" || true           | Constants.NO_ERROR
        "0I"   | "4"         | "010" || false          | Constants.NO_ERROR
        "0I"   | "4"         | "620" || true           | Constants.NO_ERROR
        "0I"   | "4"         | "650" || true           | Constants.NO_ERROR
        "0I"   | "4"         | "900" || true           | Constants.NO_ERROR
        "0K"   | "4"         | "010" || false          | Constants.NO_ERROR
        "0K"   | "4"         | "620" || true           | Constants.NO_ERROR
        "0K"   | "4"         | "650" || true           | Constants.NO_ERROR
        "0K"   | "4"         | "900" || true           | Constants.NO_ERROR
        "0M"   | "4"         | "010" || false          | Constants.NO_ERROR
        "0M"   | "4"         | "620" || true           | Constants.NO_ERROR
        "0M"   | "4"         | "650" || true           | Constants.NO_ERROR
        "0M"   | "4"         | "900" || true           | Constants.NO_ERROR
        "0P"   | "4"         | "010" || false          | Constants.NO_ERROR
        "0P"   | "4"         | "620" || true           | Constants.NO_ERROR
        "0P"   | "4"         | "650" || true           | Constants.NO_ERROR
        "0P"   | "4"         | "900" || true           | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 60, skjema #skjema / kontoklasse #kontoklasse / funksjon #funksjon / art #art -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art), definitions)))

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
        skjema | kontoklasse | funksjon | art   || expectedResult | errorlevel
        "0A"   | "0"         | "100 "   | "729" || true           | Constants.CRITICAL_ERROR
        "0A"   | "0"         | "841 "   | "010" || false          | Constants.NO_ERROR
        "0A"   | "0"         | "841 "   | "729" || false          | Constants.NO_ERROR
        "0A"   | "1"         | "100 "   | "729" || false          | Constants.NO_ERROR
        "0C"   | "0"         | "100 "   | "729" || true           | Constants.CRITICAL_ERROR
        "0C"   | "0"         | "841 "   | "010" || false          | Constants.NO_ERROR
        "0C"   | "0"         | "841 "   | "729" || false          | Constants.NO_ERROR
        "0C"   | "1"         | "100 "   | "729" || false          | Constants.NO_ERROR
        "0I"   | "4"         | "100 "   | "729" || true           | Constants.CRITICAL_ERROR
        "0I"   | "4"         | "841 "   | "010" || false          | Constants.NO_ERROR
        "0I"   | "4"         | "841 "   | "729" || false          | Constants.NO_ERROR
        "0I"   | "3"         | "100 "   | "729" || false          | Constants.NO_ERROR
        "0K"   | "4"         | "100 "   | "729" || true           | Constants.CRITICAL_ERROR
        "0K"   | "4"         | "841 "   | "010" || false          | Constants.NO_ERROR
        "0K"   | "4"         | "841 "   | "729" || false          | Constants.NO_ERROR
        "0K"   | "3"         | "100 "   | "729" || false          | Constants.NO_ERROR
        "0M"   | "4"         | "100 "   | "729" || true           | Constants.CRITICAL_ERROR
        "0M"   | "4"         | "841 "   | "010" || false          | Constants.NO_ERROR
        "0M"   | "4"         | "841 "   | "729" || false          | Constants.NO_ERROR
        "0M"   | "3"         | "100 "   | "729" || false          | Constants.NO_ERROR
        "0P"   | "4"         | "100 "   | "729" || true           | Constants.CRITICAL_ERROR
        "0P"   | "4"         | "841 "   | "010" || false          | Constants.NO_ERROR
        "0P"   | "4"         | "841 "   | "729" || false          | Constants.NO_ERROR
        "0P"   | "3"         | "100 "   | "729" || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 65, skjema #skjema / funksjon #funksjon / art #art -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "funksjon_kapittel", funksjon, "art_sektor", art), definitions)))

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
        skjema | funksjon | art   || expectedResult | errorlevel
        "0A"   | "899 "   | "010" || true           | Constants.CRITICAL_ERROR
        "0A"   | "899 "   | "589" || false          | Constants.NO_ERROR
        "0A"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0A"   | "899 "   | "989" || false          | Constants.NO_ERROR
        "0A"   | "100 "   | "589" || true           | Constants.CRITICAL_ERROR
        "0A"   | "100 "   | "980" || true           | Constants.CRITICAL_ERROR
        "0A"   | "100 "   | "989" || true           | Constants.CRITICAL_ERROR
        "0C"   | "899 "   | "010" || true           | Constants.CRITICAL_ERROR
        "0C"   | "899 "   | "589" || false          | Constants.NO_ERROR
        "0C"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0C"   | "899 "   | "989" || false          | Constants.NO_ERROR
        "0C"   | "100 "   | "589" || true           | Constants.CRITICAL_ERROR
        "0C"   | "100 "   | "980" || true           | Constants.CRITICAL_ERROR
        "0C"   | "100 "   | "989" || true           | Constants.CRITICAL_ERROR
        "0I"   | "899 "   | "010" || true           | Constants.CRITICAL_ERROR
        "0I"   | "899 "   | "589" || false          | Constants.NO_ERROR
        "0I"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0I"   | "899 "   | "989" || false          | Constants.NO_ERROR
        "0I"   | "100 "   | "589" || true           | Constants.CRITICAL_ERROR
        "0I"   | "100 "   | "980" || true           | Constants.CRITICAL_ERROR
        "0I"   | "100 "   | "989" || true           | Constants.CRITICAL_ERROR
        "0K"   | "899 "   | "010" || true           | Constants.CRITICAL_ERROR
        "0K"   | "899 "   | "589" || false          | Constants.NO_ERROR
        "0K"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0K"   | "899 "   | "989" || false          | Constants.NO_ERROR
        "0K"   | "100 "   | "589" || true           | Constants.CRITICAL_ERROR
        "0K"   | "100 "   | "980" || true           | Constants.CRITICAL_ERROR
        "0K"   | "100 "   | "989" || true           | Constants.CRITICAL_ERROR
        "0M"   | "899 "   | "010" || true           | Constants.CRITICAL_ERROR
        "0M"   | "899 "   | "589" || false          | Constants.NO_ERROR
        "0M"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0M"   | "899 "   | "989" || false          | Constants.NO_ERROR
        "0M"   | "100 "   | "589" || true           | Constants.CRITICAL_ERROR
        "0M"   | "100 "   | "980" || true           | Constants.CRITICAL_ERROR
        "0M"   | "100 "   | "989" || true           | Constants.CRITICAL_ERROR
        "0P"   | "899 "   | "010" || true           | Constants.CRITICAL_ERROR
        "0P"   | "899 "   | "589" || false          | Constants.NO_ERROR
        "0P"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0P"   | "899 "   | "989" || false          | Constants.NO_ERROR
        "0P"   | "100 "   | "589" || true           | Constants.CRITICAL_ERROR
        "0P"   | "100 "   | "980" || true           | Constants.CRITICAL_ERROR
        "0P"   | "100 "   | "989" || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 70, skjema #skjema / funksjon #funksjon / art #art -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "funksjon_kapittel", funksjon, "art_sektor", art), definitions)))

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
        skjema | funksjon | art   || expectedResult | errorlevel
        "0A"   | "899 "   | "530" || true           | Constants.CRITICAL_ERROR
        "0A"   | "880 "   | "530" || false          | Constants.NO_ERROR
        "0A"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0C"   | "899 "   | "530" || true           | Constants.CRITICAL_ERROR
        "0C"   | "880 "   | "530" || false          | Constants.NO_ERROR
        "0C"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0I"   | "899 "   | "530" || true           | Constants.CRITICAL_ERROR
        "0I"   | "880 "   | "530" || false          | Constants.NO_ERROR
        "0I"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0K"   | "899 "   | "530" || true           | Constants.CRITICAL_ERROR
        "0K"   | "880 "   | "530" || false          | Constants.NO_ERROR
        "0K"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0M"   | "899 "   | "530" || true           | Constants.CRITICAL_ERROR
        "0M"   | "880 "   | "530" || false          | Constants.NO_ERROR
        "0M"   | "899 "   | "980" || false          | Constants.NO_ERROR
        "0P"   | "899 "   | "530" || true           | Constants.CRITICAL_ERROR
        "0P"   | "880 "   | "530" || false          | Constants.NO_ERROR
        "0P"   | "899 "   | "980" || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 75, skjema #skjema / funksjon #funksjon / art #art -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "funksjon_kapittel", funksjon, "art_sektor", art), definitions)))

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
        skjema | funksjon | art   || expectedResult | errorlevel
        "0A"   | "800 "   | "010" || false          | Constants.NO_ERROR
        "0A"   | "800 "   | "870" || false          | Constants.NO_ERROR
        "0A"   | "800 "   | "874" || false          | Constants.NO_ERROR
        "0A"   | "800 "   | "875" || false          | Constants.NO_ERROR
        "0A"   | "800 "   | "877" || false          | Constants.NO_ERROR
        "0A"   | "100 "   | "870" || true           | Constants.CRITICAL_ERROR
        "0A"   | "100 "   | "874" || true           | Constants.CRITICAL_ERROR
        "0A"   | "100 "   | "875" || true           | Constants.CRITICAL_ERROR
        "0A"   | "100 "   | "877" || false          | Constants.NO_ERROR
        "0C"   | "800 "   | "010" || false          | Constants.NO_ERROR
        "0C"   | "800 "   | "870" || false          | Constants.NO_ERROR
        "0C"   | "800 "   | "874" || false          | Constants.NO_ERROR
        "0C"   | "800 "   | "875" || false          | Constants.NO_ERROR
        "0C"   | "800 "   | "877" || false          | Constants.NO_ERROR
        "0C"   | "100 "   | "870" || true           | Constants.CRITICAL_ERROR
        "0C"   | "100 "   | "874" || true           | Constants.CRITICAL_ERROR
        "0C"   | "100 "   | "875" || true           | Constants.CRITICAL_ERROR
        "0C"   | "100 "   | "877" || false          | Constants.NO_ERROR
        "0I"   | "800 "   | "010" || false          | Constants.NO_ERROR
        "0I"   | "800 "   | "870" || false          | Constants.NO_ERROR
        "0I"   | "800 "   | "874" || false          | Constants.NO_ERROR
        "0I"   | "800 "   | "875" || false          | Constants.NO_ERROR
        "0I"   | "800 "   | "877" || false          | Constants.NO_ERROR
        "0I"   | "100 "   | "870" || true           | Constants.CRITICAL_ERROR
        "0I"   | "100 "   | "874" || true           | Constants.CRITICAL_ERROR
        "0I"   | "100 "   | "875" || true           | Constants.CRITICAL_ERROR
        "0I"   | "100 "   | "877" || false          | Constants.NO_ERROR
        "0K"   | "800 "   | "010" || false          | Constants.NO_ERROR
        "0K"   | "800 "   | "870" || false          | Constants.NO_ERROR
        "0K"   | "800 "   | "874" || false          | Constants.NO_ERROR
        "0K"   | "800 "   | "875" || false          | Constants.NO_ERROR
        "0K"   | "800 "   | "877" || false          | Constants.NO_ERROR
        "0K"   | "100 "   | "870" || true           | Constants.CRITICAL_ERROR
        "0K"   | "100 "   | "874" || true           | Constants.CRITICAL_ERROR
        "0K"   | "100 "   | "875" || true           | Constants.CRITICAL_ERROR
        "0K"   | "100 "   | "877" || false          | Constants.NO_ERROR
        "0M"   | "800 "   | "010" || false          | Constants.NO_ERROR
        "0M"   | "800 "   | "870" || false          | Constants.NO_ERROR
        "0M"   | "800 "   | "874" || false          | Constants.NO_ERROR
        "0M"   | "800 "   | "875" || false          | Constants.NO_ERROR
        "0M"   | "800 "   | "877" || false          | Constants.NO_ERROR
        "0M"   | "100 "   | "870" || true           | Constants.CRITICAL_ERROR
        "0M"   | "100 "   | "874" || true           | Constants.CRITICAL_ERROR
        "0M"   | "100 "   | "875" || true           | Constants.CRITICAL_ERROR
        "0M"   | "100 "   | "877" || false          | Constants.NO_ERROR
        "0P"   | "800 "   | "010" || false          | Constants.NO_ERROR
        "0P"   | "800 "   | "870" || false          | Constants.NO_ERROR
        "0P"   | "800 "   | "874" || false          | Constants.NO_ERROR
        "0P"   | "800 "   | "875" || false          | Constants.NO_ERROR
        "0P"   | "800 "   | "877" || false          | Constants.NO_ERROR
        "0P"   | "100 "   | "870" || true           | Constants.CRITICAL_ERROR
        "0P"   | "100 "   | "874" || true           | Constants.CRITICAL_ERROR
        "0P"   | "100 "   | "875" || true           | Constants.CRITICAL_ERROR
        "0P"   | "100 "   | "877" || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 80, skjema #skjema / funksjon #funksjon / art #art -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "funksjon_kapittel", funksjon, "art_sektor", art), definitions)))

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
        skjema | funksjon | art   || expectedResult | errorlevel
        "0A"   | "840 "   | "010" || false          | Constants.NO_ERROR
        "0A"   | "840 "   | "800" || false          | Constants.NO_ERROR
        "0A"   | "100 "   | "800" || true           | Constants.CRITICAL_ERROR
        "0C"   | "840 "   | "010" || false          | Constants.NO_ERROR
        "0C"   | "840 "   | "800" || false          | Constants.NO_ERROR
        "0C"   | "100 "   | "800" || true           | Constants.CRITICAL_ERROR
        "0I"   | "840 "   | "010" || false          | Constants.NO_ERROR
        "0I"   | "840 "   | "800" || false          | Constants.NO_ERROR
        "0I"   | "100 "   | "800" || true           | Constants.CRITICAL_ERROR
        "0K"   | "840 "   | "010" || false          | Constants.NO_ERROR
        "0K"   | "840 "   | "800" || false          | Constants.NO_ERROR
        "0K"   | "100 "   | "800" || true           | Constants.CRITICAL_ERROR
        "0M"   | "840 "   | "010" || false          | Constants.NO_ERROR
        "0M"   | "840 "   | "800" || false          | Constants.NO_ERROR
        "0M"   | "100 "   | "800" || true           | Constants.CRITICAL_ERROR
        "0P"   | "840 "   | "010" || false          | Constants.NO_ERROR
        "0P"   | "840 "   | "800" || false          | Constants.NO_ERROR
        "0P"   | "100 "   | "800" || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 85, region #region / skjema #skjema / utgiftsposteringer i investeringsregnskapet, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "0"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0A"   | "0"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0A"   | "0"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | "0"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "4"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0I"   | "4"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0I"   | "4"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "4"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "4"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "4"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "4"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0P"   | "4"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0P"   | "4"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 90 inntektsposteringer i investeringsregnskapet, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "0"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0A"   | "0"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0A"   | "0"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | "0"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "4"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "4"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "4"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "4"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "4"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "4"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "4"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "4"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "4"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 95 differanse i investeringsregnskapet, region #region / skjema #skjema / kontoklasse #kontoklasse / list #list -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> unnumberedRecords = list.stream()
                .map(item -> new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", item.get("art"), "belop", item.get("belop")), definitions))
                .collect(Collectors.toList())

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
        region   | skjema | kontoklasse | funksjon | list                                                                                || expectedResult | errorlevel
        "420400" | "0A"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0A"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "030100" | "0A"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0I"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0K"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0M"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0M"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0P"   | "4"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 100 utgiftsposteringer i driftsregnskapet, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | "1"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0K"   | "3"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "100 "   | "010" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 105 inntektsposteringer i driftsregnskapet, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | "1"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0K"   | "3"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "100 "   | "600" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "100 "   | "990" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "100 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 110 differanse i driftsregnskapet, region #region / skjema #skjema / kontoklasse #kontoklasse / list #list-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> unnumberedRecords = list.stream()
                .map(item -> new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", item.get("art"), "belop", item.get("belop")), definitions))
                .collect(Collectors.toList())

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
        region   | skjema | kontoklasse | funksjon | list                                                                                || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | "1"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0K"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0M"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "100 "   | List.of(Map.of("art", "590", "belop", "1"), Map.of("art", "600", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 115 registrering av aktiva i balanseregnskapet, skjema #skjema / kontoklasse #kontoklasse / sektor #sektor / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", kapittel, "art_sektor", sektor, "belop", belop), definitions)))

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
        skjema | kontoklasse | kapittel | sektor | belop || expectedResult | errorlevel
        "0B"   | "2"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        "0B"   | "2"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        "0B"   | "2"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
        "0D"   | "2"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        "0D"   | "2"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        "0D"   | "2"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
        "0J"   | "5"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        "0J"   | "5"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        "0J"   | "5"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
        "0L"   | "5"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        "0L"   | "5"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        "0L"   | "5"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
        "0N"   | "5"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        "0N"   | "5"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        "0N"   | "5"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
        "0Q"   | "5"         | "10  "   | "010"  | "1"   || false          | Constants.NO_ERROR
        "0Q"   | "5"         | "10  "   | "590"  | "1"   || false          | Constants.NO_ERROR
        "0Q"   | "5"         | "10  "   | "590"  | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 120 registrering av passiva i balanseregnskapet, skjema #skjema / kontoklasse #kontoklasse / kapittel #kapittel / sektor #sektor / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", kapittel, "art_sektor", sektor, "belop", belop), definitions)))

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
        skjema | kontoklasse | kapittel | sektor | belop || expectedResult | errorlevel
        "0B"   | "2"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        "0B"   | "2"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        "0B"   | "2"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
        "0D"   | "2"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        "0D"   | "2"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        "0D"   | "2"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
        "0J"   | "5"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        "0J"   | "5"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        "0J"   | "5"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
        "0L"   | "5"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        "0L"   | "5"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        "0L"   | "5"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
        "0N"   | "5"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        "0N"   | "5"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        "0N"   | "5"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
        "0Q"   | "5"         | "31  "   | "600"  | "-1"  || false          | Constants.NO_ERROR
        "0Q"   | "5"         | "31  "   | "990"  | "-1"  || false          | Constants.NO_ERROR
        "0Q"   | "5"         | "31  "   | "990"  | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 125 differanse i driftsregnskapet, skjema #skjema / kontoklasse #kontoklasse / list #list-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> unnumberedRecords = list.stream()
                .map(item -> new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", item.get("kapittel"), "art_sektor", sektor, "belop", item.get("belop")), definitions))
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
        skjema | kontoklasse | list                                                                                            | sektor || expectedResult | errorlevel
        "0B"   | "2"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        "0B"   | "2"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
        "0D"   | "2"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        "0D"   | "2"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
        "0J"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        "0J"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
        "0L"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        "0L"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
        "0N"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        "0N"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
        "0Q"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1"))    | "000"  || false          | Constants.NO_ERROR
        "0Q"   | "5"         | List.of(Map.of("kapittel", "10  ", "belop", "1"), Map.of("kapittel", "31  ", "belop", "-1000")) | "000"  || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 130 Skatteinntekter, region #region / skjema #skjema / kontoklasse #kontoklasse / funksjon #funksjon / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "800 "   | "870" | "0"   || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | "1"         | "800 "   | "870" | "0"   || false          | Constants.NO_ERROR
        "211100" | "0A"   | "1"         | "800 "   | "870" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "800 "   | "870" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "800 "   | "870" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "800 "   | "870" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "800 "   | "870" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "800 "   | "870" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "800 "   | "870" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 135 Rammetilskudd, region #region / skjema #skjema / kontoklasse #kontoklasse / funksjon #funksjon / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "840 "   | "800" | "0"   || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | "1"         | "840 "   | "800" | "0"   || false          | Constants.NO_ERROR
        "211100" | "0A"   | "1"         | "840 "   | "800" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "840 "   | "800" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "840 "   | "800" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "840 "   | "800" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "840 "   | "800" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "840 "   | "800" | "-1"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "840 "   | "800" | "0"   || true           | Constants.CRITICAL_ERROR
        // unntak for gitte kommuner
        "501400" | "0A"   | "1"         | "840 "   | "800" | "0"   || false          | Constants.NO_ERROR
        "501400" | "0M"   | "3"         | "840 "   | "800" | "0"   || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 140 Overforing mellom drifts- og investeringsregnskap, region #region / skjema #skjema / list #list -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> unnumberedRecords = list.stream()
                .map(l -> new KostraRecord(Map.of("skjema", skjema, "kontoklasse", l.get("kontoklasse"), "funksjon_kapittel", l.get("funksjon"), "art_sektor", l.get("art"), "belop", l.get("belop")), definitions))
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
        region   | skjema | list                                                                                                                                                                || expectedResult | errorlevel
        "420400" | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "0", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "0", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "0", "funksjon", "100 ", "art", "970", "belop", "-1000")) || false          | Constants.NO_ERROR
        "211100" | "0C"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "0", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0C"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "0", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0I"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0K"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0K"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0M"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0M"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0P"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "570", "belop", "1"), Map.of("kontoklasse", "4", "funksjon", "100 ", "art", "970", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 145 Avskrivninger, motpost avskrivninger, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "860 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | "1"         | "860 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "860 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "860 "   | "990" | "0"   || true           | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "860 "   | "990" | "0"   || true           | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "860 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "860 "   | "990" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "860 "   | "990" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 150 Avskrivninger, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | "1"         | "100 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "100 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "100 "   | "590" | "0"   || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 155 Avskrivninger, differanse, region #region / skjema #skjema / list #list -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> unnumberedRecords = list.stream()
                .map(l -> new KostraRecord(Map.of("skjema", skjema, "kontoklasse", l.get("kontoklasse"), "funksjon_kapittel", l.get("funksjon"), "art_sektor", l.get("art"), "belop", l.get("belop")), definitions))
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
        region   | skjema | list                                                                                                                                                                || expectedResult | errorlevel
        "420400" | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "1", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "1", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "030101" | "0A"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "1", "funksjon", "860 ", "art", "990", "belop", "-1000")) || false          | Constants.NO_ERROR
        "420400" | "0C"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "1", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0C"   | List.of(Map.of("kontoklasse", "1", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "1", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0I"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0K"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0K"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0M"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0M"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "420400" | "0P"   | List.of(Map.of("kontoklasse", "3", "funksjon", "100 ", "art", "590", "belop", "1"), Map.of("kontoklasse", "3", "funksjon", "860 ", "art", "990", "belop", "-1000")) || true           | Constants.CRITICAL_ERROR
    }

    def "Skal validere Kontroll 160 Avskrivninger, avskrivninger fort pa andre funksjoner, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0A"   | "1"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "030101" | "0A"   | "1"         | "800 "   | "590" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0C"   | "1"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0K"   | "3"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0M"   | "3"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "800 "   | "590" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "800 "   | "590" | "0"   || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 165 Avskrivninger, motpost avskrivninger fort pa andre funksjoner, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0A"   | "1"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "030101" | "0A"   | "1"         | "100 "   | "990" | "1"   || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0C"   | "1"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0I"   | "3"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0K"   | "3"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0M"   | "3"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "100 "   | "990" | "1"   || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "100 "   | "990" | "0"   || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 170 Funksjon 290, investeringsregnskapet, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "0"         | "290 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        "420400" | "0A"   | "0"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "030101" | "0A"   | "0"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "4"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "4"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "4"         | "290 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        "420400" | "0M"   | "4"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "4"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "4"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 175 Funksjon 290, driftsregnskapet, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "290 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        "420400" | "0A"   | "1"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "030101" | "0A"   | "1"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "290 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        "420400" | "0M"   | "3"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "290 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "290 "   | "010" | "30"  || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 180 Funksjon 465, investeringsregnskapet, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "0"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0A"   | "0"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "030101" | "0A"   | "0"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "0"         | "465 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        "420400" | "0C"   | "0"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "4"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "4"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "4"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "4"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "4"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "4"         | "465 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "4"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
    }

    def "Skal validere Kontroll 185 Funksjon 465, driftsregnskapet, region #region / skjema #skjema / kontoklasse #kontoklasse / art #art / belop #belop-> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", region})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon, "art_sektor", art, "belop", belop), definitions)))

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
        region   | skjema | kontoklasse | funksjon | art   | belop || expectedResult | errorlevel
        "420400" | "0A"   | "1"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0A"   | "1"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "030101" | "0A"   | "1"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0C"   | "1"         | "465 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        "420400" | "0C"   | "1"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0I"   | "3"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0K"   | "3"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "465 "   | "010" | "31"  || false          | Constants.NO_ERROR
        "420400" | "0M"   | "3"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
        "420400" | "0P"   | "3"         | "465 "   | "010" | "31"  || true           | Constants.CRITICAL_ERROR
        "420400" | "0P"   | "3"         | "465 "   | "010" | "30"  || false          | Constants.NO_ERROR
    }


    def "Skal validere Kontroll 190 Memoriakonti, skjema #skjema / list #list -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "420400"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> unnumberedRecords = list.stream()
                .map(l -> new KostraRecord(Map.of("skjema", skjema, "kontoklasse", l.get("kontoklasse"), "funksjon_kapittel", l.get("kapittel"), "art_sektor", l.get("sektor"), "belop", l.get("belop")), definitions))
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
        skjema | list                                                                                                                                                                      || expectedResult | errorlevel
        "0B"   | List.of(Map.of("kontoklasse", "2", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "2", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "0B"   | List.of(Map.of("kontoklasse", "2", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "2", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NO_ERROR
        "0D"   | List.of(Map.of("kontoklasse", "2", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "2", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "0D"   | List.of(Map.of("kontoklasse", "2", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "2", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NO_ERROR
        "0J"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "0J"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NO_ERROR
        "0L"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "0L"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NO_ERROR
        "0N"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "0N"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NO_ERROR
        "0Q"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1"))    || false          | Constants.NO_ERROR
        "0Q"   | List.of(Map.of("kontoklasse", "5", "kapittel", "9100", "sektor", "000", "belop", "1"), Map.of("kontoklasse", "5", "kapittel", "9999", "sektor", "990", "belop", "-1000")) || true           | Constants.NO_ERROR
    }
}
