package no.ssb.kostra.control.felles

import spock.lang.Specification

import static no.ssb.kostra.control.felles.Comparator.*

class ComparatorSpec extends Specification {

    def "should compare two Integers using an Operator, #int1 #operator #int2 -> #result "() {
        expect:
        compareIntegerOperatorInteger(int1, operator, int2) == result

        where:
        int1 | operator | int2 || result
        1    | "<"      | 2    || true
        2    | "<"      | 2    || false
        3    | "<"      | 2    || false
        2    | "<="     | 1    || false
        2    | "<="     | 2    || true
        2    | "<="     | 3    || true
        2    | ">"      | 1    || true
        2    | ">"      | 2    || false
        2    | ">"      | 3    || false
        1    | ">="     | 2    || false
        2    | ">="     | 2    || true
        3    | ">="     | 2    || true
        1    | "=="     | 2    || false
        2    | "=="     | 2    || true
        1    | "!="     | 2    || true
        2    | "!="     | 2    || false
        null | "=="     | 0    || false
        null | null     | 0    || false
    }

    def "should validate that code is in code list, #code in #codelist -> #result"() {
        expect:
        isCodeInCodelist(code, codelist) == result

        where:
        code            | codelist                  || result
        "code1"         | List.of("code1", "code2") || true
        "notInCodeList" | List.of("code1", "code2") || false
    }

    def "should validate that code is removed from code list, remove #removelist from #codelist -> #resultlist -> #result"() {
        expect:
        verifyAll {
            removeCodesFromCodelist(codelist, removelist) == resultlist
            (removeCodesFromCodelist(codelist, removelist) == resultlist) == result
        }

        where:
        codelist                           | removelist               || resultlist                         | result
        List.of("code1", "code2", "code3") | List.of("code2")         || List.of("code1", "code3")          | true
        List.of("code1", "code2", "code3") | List.of("notInCodeList") || List.of("code1", "code2", "code3") | true
    }

    def "should validate business id, #orgnr -> #result"() {
        expect:
        isValidOrgnr(orgnr) == result

        where:
        orgnr       || result
        "000000000" || false
        "123456789" || false
        "944117784" || true
        "999999999" || true
    }

    def "should validate that integer is between lower and upper threshold, #lower <= #int1 <= #upper -> #result"() {
        expect:
        between(int1, lower, upper) == result

        where:
        int1 | lower | upper || result
        1    | 1     | 3     || true
        2    | 1     | 3     || true
        3    | 1     | 3     || true
        0    | 1     | 3     || false
        4    | 1     | 3     || false

    }

    def "should validate that integer is outside lower or upper threshold, #int1 < #lower || #upper < #int1 -> #result"() {
        expect:
        outsideOf(int1, lower, upper) == result

        where:
        int1 | lower | upper || result
        1    | 1     | 3     || false
        2    | 1     | 3     || false
        3    | 1     | 3     || false
        0    | 1     | 3     || true
        4    | 1     | 3     || true

    }

    def "should validate that date is valid, #date using #format -> #result"() {
        expect:
        isValidDate(date, format) == result

        where:
        date                          | format       || result
        "01-01-2000"                  | "dd-MM-yyyy" || true
        "29-02-1900"                  | "dd-MM-yyyy" || true
        "190966"                      | "ddMMyy"     || true
        "19096699999".substring(0, 6) | "ddMMyy"     || true
        "42-01-2000"                  | "dd-MM-yyyy" || false
        "0000000000"                  | "dd-MM-yyyy" || false
        "          "                  | "dd-MM-yyyy" || false
    }

    def "should validate emptiness, #param -> #result"() {
        expect:
        isEmpty(param) == result

        where:
        param  || result
        "true" || false
        ""     || true
        null   || true
    }

    def "should validate setting a default string if null, #str -> #defaultStr -> #expectedStr"() {
        expect:
        defaultString(str, defaultStr).equalsIgnoreCase(expectedStr)

        where:
        str      | defaultStr      || expectedStr
        "string" | "defaultString" || "string"
        null     | "defaultString" || "defaultString"
    }

    def "should compare two string for equal content, string1 = '#string1' / string2 = '#string2'"() {
        expect:
        compareString1EqualsString2(string1, string2) == result

        where:
        string1  | string2  || result
        "string" | "string" || true
        "not"    | "equal"  || false
        "string" | ""       || false
        "string" | " "      || false
        "string" | null     || false

    }

}
// compareString1EqualsString2