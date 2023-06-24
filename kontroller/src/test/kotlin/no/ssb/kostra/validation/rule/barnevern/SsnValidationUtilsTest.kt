package no.ssb.kostra.validation.rule.barnevern

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.util.SsnValidationUtils
import no.ssb.kostra.validation.util.SsnValidationUtils.isModulo11Valid
import no.ssb.kostra.validation.util.SsnValidationUtils.isValidSocialSecurityIdOrDnr
import no.ssb.kostra.validation.util.SsnValidationUtils.validateDUF
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

    Given("getAgeFromSocialSecurityId") {
        forAll(
            row("invalid fødselsnummer, expect null", "123", null),
            row("valid fødselsnummer, expect age", "05011399292",9),
            row("valid D-nr, expect age", "41011088188",12)
        ) { description, socialSecurityId, expectedAge ->
            When(description) {
                val ageInYears = SsnValidationUtils.getAgeFromSocialSecurityId(
                    socialSecurityId = socialSecurityId,
                    reportingYearAsString = "2022"
                )

                Then("ageInYears should be as expected") {
                    ageInYears shouldBe expectedAge
                }
            }
        }

        When("invalid registrationYear") {
            val thrown = shouldThrow<NumberFormatException> {
                SsnValidationUtils.getAgeFromSocialSecurityId(
                    socialSecurityId = "41011088188",
                    reportingYearAsString = "abc"
                )
            }

            Then("thrown should be as expected") {
                thrown.message shouldBe "For input string: \"abc\""
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
