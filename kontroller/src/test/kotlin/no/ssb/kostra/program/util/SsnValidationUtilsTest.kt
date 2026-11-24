package no.ssb.kostra.program.util

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.util.SsnValidationUtils.isModulo11Valid
import no.ssb.kostra.program.util.SsnValidationUtils.isValidSocialSecurityId
import no.ssb.kostra.program.util.SsnValidationUtils.isValidSocialSecurityIdOrDnr
import no.ssb.kostra.program.util.SsnValidationUtils.validateDUF
import java.time.LocalDate

class SsnValidationUtilsTest :
    BehaviorSpec({
        val reportingYear = 2025

        Given("extractBirthDateFromSocialSecurityId") {
            data class Case(
                val socialSecurityId: String,
                val type: String,
                val expectedDate: LocalDate?,
            )

            listOf(
                Case("123", "N/A", null),
                Case("05011399292", "fnr", LocalDate.of(2013, 1, 5)),
                Case("41011088188", "dnr", LocalDate.of(2010, 1, 1)),
                Case("01811088188", "tnr", LocalDate.of(2010, 1, 1)),
                Case("01010100100", "N/A", LocalDate.of(2001, 1, 1)),
                Case("01018000100", "N/A", LocalDate.of(1980, 1, 1)),
                Case("01018000200", "N/A", LocalDate.of(1980, 1, 1)),
                Case("01012599999", "N/A", LocalDate.of(2025, 1, 1)),
                Case("01012699999", "N/A", LocalDate.of(2026, 1, 1)),
                Case("01012799999", "N/A", LocalDate.of(1927, 1, 1)),
            ).forEach { (socialSecurityId, type, expectedDate) ->
                When("$socialSecurityId $type") {

                    val dateOfBirth = SsnValidationUtils.extractBirthDateFromSocialSecurityId(socialSecurityId, reportingYear)

                    Then("dateOfBirth ($dateOfBirth) should be as expected ($expectedDate)") {
                        dateOfBirth shouldBe expectedDate
                    }
                }
            }
        }

        Given("parseDateWithAutoPivotYear") {
            data class Case(
                val socialSecurityId: String,
                val type: String,
                val expectedDate: LocalDate?,
            )

            listOf(
                Case("01015450068", "fnr", LocalDate.of(1854, 1, 1)),
                Case("31129974810", "fnr", LocalDate.of(1899, 12, 31)),
                Case("01010000110", "fnr", LocalDate.of(1900, 1, 1)),
                Case("31129949980", "fnr", LocalDate.of(1999, 12, 31)),
                Case("01014090017", "fnr", LocalDate.of(1940, 1, 1)),
                Case("31129999813", "fnr", LocalDate.of(1999, 12, 31)),
                Case("01010050053", "fnr", LocalDate.of(2000, 1, 1)),
                Case("31123999854", "fnr", LocalDate.of(2039, 12, 31)),
                Case("31124588890", "fnr", LocalDate.of(2045, 12, 31)),
                Case("31123974912", "fnr", LocalDate.of(2039, 12, 31)),
                Case("01014074912", "fnr", LocalDate.of(2040, 1, 1)),
            ).forEach { (socialSecurityId, type, expectedDate) ->
                When("$socialSecurityId $type") {

                    val dateOfBirth = SsnValidationUtils.parseDateWithAutoPivotYear(socialSecurityId)

                    Then("dateOfBirth ($dateOfBirth) should be as expected ($expectedDate)") {
                        dateOfBirth shouldBe expectedDate
                    }
                }
            }
        }

        Given("isValidSocialSecurityId") {
            data class Case(
                val description: String,
                val socialSecurityId: String,
                val expectedResult: Boolean,
            )

            listOf(
                Case("socialSecurityId does not match regex", "abc", false),
                Case("socialSecurityId does matches regex", "12345612345", false),
                Case("invalid date of birth in socialSecurityId", "01840612345", false),
                Case("valid socialSecurityId from 1854, 500 - 749 series", "01015450068", true),
                Case("valid socialSecurityId from 1899, 500 - 749 series", "31129974810", true),
                Case("valid socialSecurityId from 1900, 000 - 499 series", "01010000110", true),
                Case("valid socialSecurityId from 1999, 000 - 499 series", "31129949980", true),
                Case("valid socialSecurityId from 1940, 900 - 999 series", "01014090017", true),
                Case("valid socialSecurityId from 1999, 900 - 999 series", "31129999813", true),
                Case("valid socialSecurityId from 2000, 500 - 999 series", "01010050053", true),
                Case("valid socialSecurityId from 2039, 500 - 999 series", "31123999854", true),
            ).forEach { (description, socialSecurityId, expectedResult) ->
                When(description) {
                    val isValidSocialSecurityId = isValidSocialSecurityId(socialSecurityId)

                    Then("isValidSocialSecurityId($socialSecurityId) should be $expectedResult") {
                        isValidSocialSecurityId shouldBe expectedResult
                    }
                }
            }
        }

        Given("isValidSocialSecurityIdOrDnr") {
            data class Case(
                val description: String,
                val socialSecurityId: String,
                val expectedResult: Boolean,
            )

            listOf(
                Case("ssn does not match regex", "abc", false),
                Case("valid ssn", "05011399292", true),
                Case("valid D-number", "41011088188", true),
                Case("invalid date of birth in ssn", "01840612345", false),
                Case("valid freg ssn", "01840699478", true),
                Case("valid unborn ssn", "05012999999", true),
            ).forEach { (description, socialSecurityId, expectedResult) ->
                When(description) {
                    val isValidSocialSecurityIdOrDnr = isValidSocialSecurityIdOrDnr(socialSecurityId, reportingYear)

                    Then("isValidSocialSecurityIdOrDnr($socialSecurityId) should be $expectedResult") {
                        isValidSocialSecurityIdOrDnr shouldBe expectedResult
                    }
                }
            }
        }

        Given("isModulo11Valid") {
            data class Case(
                val socialSecurityId: String,
                val type: String,
                val expectedResult: Boolean,
            )

            listOf(
                Case("05011399292", "fnr", true),
                Case("41011088188", "dnr", true),
                Case("01020304050", "fnr", false),
                Case("ABCDEFGHIJK", "N/A", false),
                Case("123456", "N/A", false),
                Case("24101219220", "fnr", true),
                Case("01000000040", "fnr", false),
            ).forEach { (fnr, type, expectedResult) ->
                When("validate norwegian social security number $fnr ($type)") {
                    val isValidSocialSecurityId = isModulo11Valid(fnr)

                    Then("isValidSocialSecurityId should be $expectedResult") {
                        isValidSocialSecurityId shouldBe expectedResult
                    }
                }
            }
        }

        Given("validateDUF") {
            data class Case(
                val duf: String,
                val expectedResult: Boolean,
            )

            listOf(
                Case("201017238203", true),
                Case("200816832910", true),
                Case("201012345678", false),
                Case("ABCDEFGHIJKL", false),
                Case("123456", false),
                Case("241012192200", false),
                Case("010000000400", false),
            ).forEach { (duf, expectedResult) ->
                When("validateDUF: $duf") {
                    val isValidDuf = validateDUF(duf)

                    Then("isValidDuf should be $expectedResult") {
                        isValidDuf shouldBe expectedResult
                    }
                }
            }
        }
    })
