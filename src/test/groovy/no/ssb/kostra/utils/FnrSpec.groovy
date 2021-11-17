package no.ssb.kostra.utils

import spock.lang.Specification

import static no.ssb.kostra.utils.Fnr.isValidDate

class FnrSpec extends Specification {

    def "isValidDateTest"() {
        expect:
        isValidDate("010112", "ddMMyy")
        and:
        isValidDate("01012012", "ddMMyyyy")
        and:
        !isValidDate("30022012", "ddMMyyyy")
    }
}
