package no.ssb.kostra.validation.rule.famvern

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.string.shouldContain
import no.ssb.kostra.area.famvern.FamilievernConstants
import java.nio.file.NoSuchFileException

class FamilievernConstantsTest : BehaviorSpec({

    Given("a file name of an NON-existing mapping file") {
        val fileName = "non_existing_mapping_file.yaml"

        When("opening the NON-existing mapping file") {
            val thrown = shouldThrow<NoSuchFileException> {
                FamilievernConstants.getResourceAsMappingDescription(fileName)
            }
            Then("NoSuchFileException is thrown") {
                thrown.message shouldContain "Famvern mapping file not found"
            }
        }
    }

    Given("a file name of an empty mapping file") {
        When("opening the empty mapping file") {
            val thrown = shouldThrow<MismatchedInputException> {
                FamilievernConstants.getResourceAsMappingDescription("empty_file.yaml")
            }
            Then("MismatchedInputException is thrown") {
                thrown.message shouldContain "No content to map due to end-of-input"
            }
        }
    }

    Given("a file name of a mapping file") {
        val fileName = "mapping_familievern_region_fylke_kontor.yaml"

        When("opening the mapping file") {

            val result = FamilievernConstants
                .getResourceAsMappingDescription(fileName)

            Then("result is not empty and should contain data") {
                result.title.isNotEmpty()
                result.year != 0
                result.description.isNotEmpty()
                result.regions.isNotEmpty()
                result.regions.first().counties.isNotEmpty()
                result.regions.first().counties.first().offices.isNotEmpty()
            }
        }
    }

    Given("FamilievernConstants.kontorFylkeRegionMappingList") {
        val sut = FamilievernConstants.kontorFylkeRegionMappingList

        Then("kontorFylkeRegionMappingList should not be empty and should contain data") {
            sut.isNotEmpty()
            sut.shouldContain(
                FamilievernConstants.KontorFylkeRegionMapping(
                    region = "667200",
                    fylke = "03",
                    kontor = "030",
                ),
            )
            sut.shouldContain(
                FamilievernConstants.KontorFylkeRegionMapping(
                    region = "667600",
                    fylke = "56",
                    kontor = "205",
                ),
            )
        }
    }
})