package no.ssb.kostra.program.extension

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class StringExtensionsKtTest :
    BehaviorSpec({

        Given("municipalityIdFromRegion and districtIdFromRegion") {
            forAll(
                row("valid region, expect municipalityId and districtId", "030102", "0301", "02"),
                row("invalid region, expect exception", "", null, null),
            ) { description, region, expectedMunicipalityId, expectedDistrictId ->
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
            forAll(
                row("invalid format, invalid fødselsnummer, expect null", "123", null),
                row("valid format, valid fødselsnummer, expect age", "05011399292", 9),
                row("valid format, valid D-nr, expect age", "41011088188", 12),
                row("valid format, invalid fødselsnummer, unborn child, expect age", "01012399999", -1),
            ) { description, socialSecurityId, expectedAge ->
                When(description) {
                    val ageInYears = socialSecurityId.ageInYears(2022)

                    Then("ageInYears ($ageInYears) should be expected age ($expectedAge)") {
                        ageInYears shouldBe expectedAge
                    }
                }
            }
        }

        Given("isValidDate") {
            forAll(
                row("valid date, 010124, expect true", "010124", "ddMMyy", true),
                row("valid date, 290224, expect true", "290224", "ddMMyy", true),
                row("valid date, 311224, expect true", "311224", "ddMMyy", true),
                row("invalid date, 300224, expect false", "300224", "ddMMyy", false),
                row("invalid date, 290223, expect false", "290223", "ddMMyy", false),
                row("invalid date, 310624, expect false", "310624", "ddMMyy", false),
                row("invalid date, 000000, expect false", "000000", "ddMMyy", false),
                row("invalid date, DDMMYY, expect false", "DDMMYY", "ddMMyy", false),
                row("invalid date, '      ', expect false", "     ", "ddMMyy", false),
            ) { description, dateString, dateFormatString, expected ->
                When(description) {
                    val result = dateString.isValidDate(dateFormatString)

                    Then("result should be as expected") {
                        result shouldBe expected
                    }
                }
            }
        }
    })
