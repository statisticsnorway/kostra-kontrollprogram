package no.ssb.kostra.program.extension

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class StringExtensionsKtTest : BehaviorSpec({

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
            row("invalid fødselsnummer, expect null", "123", null),
            row("valid fødselsnummer, expect age", "05011399292", 9),
            row("valid D-nr, expect age", "41011088188", 12)
        ) { description, socialSecurityId, expectedAge ->
            When(description) {
                val ageInYears = socialSecurityId.ageInYears(2022)

                Then("ageInYears should be as expected") {
                    ageInYears shouldBe expectedAge
                }
            }
        }
    }
})
