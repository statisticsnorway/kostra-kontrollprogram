package no.ssb.kostra.program.extension

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class StringExtensionsKtTest :
    BehaviorSpec({

        Given("municipalityIdFromRegion and districtIdFromRegion") {
            data class Case(
                val description: String,
                val region: String,
                val expectedMunicipalityId: String?,
                val expectedDistrictId: String?,
            )

            listOf(
                Case("valid region, expect municipalityId and districtId", "030102", "0301", "02"),
                Case("invalid region, expect exception", "", null, null),
            ).forEach { (description, region, expectedMunicipalityId, expectedDistrictId) ->
                When("municipalityIdFromRegion, $description") {
                    when (expectedMunicipalityId) {
                        null -> {
                            shouldThrow<IndexOutOfBoundsException> {
                                region.municipalityIdFromRegion()
                            }
                        }

                        else -> {
                            val actualMunicipalityId = region.municipalityIdFromRegion()

                            Then("actualMunicipalityId should be as expected") {
                                actualMunicipalityId shouldBe expectedMunicipalityId
                            }
                        }
                    }
                }

                When("districtIdFromRegion, $description") {
                    when (expectedDistrictId) {
                        null -> {
                            shouldThrow<IndexOutOfBoundsException> {
                                region.districtIdFromRegion()
                            }
                        }

                        else -> {
                            val actualDistrictId = region.districtIdFromRegion()

                            Then("actualDistrictId should be as expected") {
                                actualDistrictId shouldBe expectedDistrictId
                            }
                        }
                    }
                }
            }
        }

        Given("getAgeFromSocialSecurityId") {
            data class Case(
                val description: String,
                val socialSecurityId: String,
                val expectedAge: Int?,
            )

            listOf(
                Case("invalid format, invalid fødselsnummer, expect null", "123", null),
                Case("valid format, valid fødselsnummer, expect age", "05011399292", 9),
                Case("valid format, valid D-nr, expect age", "41011088188", 12),
                Case("valid format, invalid fødselsnummer, unborn child, expect age", "01012399999", -1),
            ).forEach { (description, socialSecurityId, expectedAge) ->
                When(description) {
                    val ageInYears = socialSecurityId.ageInYears(2022)

                    Then("ageInYears ($ageInYears) should be expected age ($expectedAge)") {
                        ageInYears shouldBe expectedAge
                    }
                }
            }
        }

        Given("isValidDate") {
            data class Case(
                val description: String,
                val dateString: String,
                val dateFormatString: String,
                val expected: Boolean,
            )

            listOf(
                Case("valid date, 010124, expect true", "010124", "ddMMyy", true),
                Case("valid date, 290224, expect true", "290224", "ddMMyy", true),
                Case("valid date, 311224, expect true", "311224", "ddMMyy", true),
                Case("invalid date, 300224, expect false", "300224", "ddMMyy", false),
                Case("invalid date, 290223, expect false", "290223", "ddMMyy", false),
                Case("invalid date, 310624, expect false", "310624", "ddMMyy", false),
                Case("invalid date, 000000, expect false", "000000", "ddMMyy", false),
                Case("invalid date, DDMMYY, expect false", "DDMMYY", "ddMMyy", false),
                Case("invalid date, '      ', expect false", "     ", "ddMMyy", false),
            ).forEach { (description, dateString, dateFormatString, expected) ->
                When(description) {
                    val result = dateString.isValidDate(dateFormatString)

                    Then("result should be as expected") {
                        result shouldBe expected
                    }
                }
            }
        }
    })
