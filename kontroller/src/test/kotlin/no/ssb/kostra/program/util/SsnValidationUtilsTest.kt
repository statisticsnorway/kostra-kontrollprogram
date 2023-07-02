package no.ssb.kostra.program.util

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.util.SsnValidationUtils.isModulo11Valid
import no.ssb.kostra.program.util.SsnValidationUtils.isValidSocialSecurityId
import no.ssb.kostra.program.util.SsnValidationUtils.isValidSocialSecurityIdOrDnr
import no.ssb.kostra.program.util.SsnValidationUtils.validateDUF
import java.time.LocalDate

class SsnValidationUtilsTest : BehaviorSpec({

    Given("extractBirthDateFromSocialSecurityId") {
        forAll(
            row("123", "N/A", null),
            row("05011399292", "fnr", LocalDate.of(2013, 1, 5)),
            row("41011088188", "dnr", LocalDate.of(2010, 1, 1))
        ) { socialSecurityId, type, expectedDate ->
            When("$socialSecurityId $type") {

                val dateOfBirth = SsnValidationUtils.extractBirthDateFromSocialSecurityId(socialSecurityId)

                Then("dateOfBirth should be as expected") {
                    dateOfBirth shouldBe expectedDate
                }
            }
        }
    }

    Given("isValidSocialSecurityId") {
        forAll(
            row("socialSecurityId does not match regex", "abc", false),
            row("socialSecurityId does matches regex", "12345612345", false),
            row("invalid date of birth in socialSecurityId", "01840612345", false),
            row("valid FREG socialSecurityId", "31925298037", true),
        ) { description, socialSecurityId, expectedResult ->
            When(description) {
                val isValidSocialSecurityId = isValidSocialSecurityId(socialSecurityId)

                Then("isValidSocialSecurityId should be expectedResult") {
                    isValidSocialSecurityId shouldBe expectedResult
                }
            }
        }
    }

    Given("isValidSocialSecurityIdOrDnr") {
        forAll(
            row("ssn does not match regex", "abc", false),
            row("valid ssn", "05011399292", true),
            row("valid D-number", "41011088188", true),
            row("invalid date of birth in ssn", "01840612345", false),
            row("valid freg ssn", "01840699478", true),
            row("valid unborn ssn", "05012399999", true)
        ) { description, socialSecurityId, expectedResult ->
            When(description) {
                val isValidSocialSecurityIdOrDnr = isValidSocialSecurityIdOrDnr(socialSecurityId)

                Then("isValidSocialSecurityId should be expectedResult") {
                    isValidSocialSecurityIdOrDnr shouldBe expectedResult
                }
            }
        }
    }

    Given("isModulo11Valid") {
        forAll(
            row("05011399292", "fnr", true),
            row("41011088188", "dnr", true),
            row("01020304050", "fnr", false),
            row("ABCDEFGHIJK", "N/A", false),
            row("123456", "N/A", false),
            row("24101219220", "fnr", true),
            row("01000000040", "fnr", false)
        ) { fnr, type, expectedResult ->
            When("validate norwegian social security number $fnr ($type)") {
                val isValidSocialSecurityId = isModulo11Valid(fnr)

                Then("isValidSocialSecurityId should be expectedResult") {
                    isValidSocialSecurityId shouldBe expectedResult
                }
            }
        }
    }

    Given("validateDUF") {
        forAll(
            row("201017238203", true),
            row("200816832910", true),
            row("201012345678", false),
            row("ABCDEFGHIJKL", false),
            row("123456", false),
            row("241012192200", false),
            row("010000000400", false)
        ) { duf, expectedResult ->
            When("validateDUF: $duf") {
                val isValidDuf = validateDUF(duf)

                Then("isValidDuf should be expectedResult") {
                    isValidDuf shouldBe expectedResult
                }
            }
        }
    }
})
