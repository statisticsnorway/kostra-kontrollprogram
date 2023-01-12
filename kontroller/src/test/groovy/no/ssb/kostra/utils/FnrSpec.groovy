package no.ssb.kostra.utils

import spock.lang.Specification

import java.util.regex.Pattern

import static no.ssb.kostra.utils.Fnr.*

class FnrSpec extends Specification {
    def "should validate date and format"() {
        expect:
        isValidDate(date, format) == result.toBoolean()

        where:
        date       | format     || result
        "010112"   | "ddMMyy"   || true
        "01012012" | "ddMMyyyy" || true
        "30022012" | "ddMMyyyy" || false
        null       | "ddMMyy"   || false
        "010112"   | null       || false
        null       | null       || false
    }

    def "should validate FNR"() {
        expect:
        isValidNorwId(fnr) == result.toBoolean()

        where:
        fnr           || result
        "01010150589" || true
        "41010150572" || true
        "01011200100" || false
        "01011200200" || false
        "01011255555" || false
        "01011299999" || false
        "           " || false
        null          || false
    }

    def "should validate partial FNR"() {
        expect:
        isPartiallyValidNorwId(fnr) == result.toBoolean()

        where:
        fnr           || result
        "01010150589" || true
        "41010150572" || true
        "01011200100" || true
        "01011200200" || true
        "01011200000" || false
        "01011299999" || false
        "           " || false
        "1234567890"  || false
        null          || false
    }

    def "should validate DUF"() {
        expect:
        isValidDUFnr(duf) == result.toBoolean()

        where:
        duf            || result
        "201212345603" || true
        "201234567890" || false
        "            " || false
        "12345678901"  || false
        null           || false
    }

    def "should validate age from the datepart of FNR"() {
        expect:
        getAlderFromFnr(fnr, year) == expectedAge.intValue()

        where:
        fnr           | year   || expectedAge
        "01010150589" | "2021" || 20
        "01010100000" | "2021" || 20
        "01012100000" | "2021" || 0
        null          | "2021" || -1
        "01012100000" | null   || -1
        null          | null   || -1
    }
}
