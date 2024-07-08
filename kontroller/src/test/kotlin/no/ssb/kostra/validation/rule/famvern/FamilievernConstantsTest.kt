package no.ssb.kostra.validation.rule.famvern

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.string.shouldContain
import no.ssb.kostra.area.famvern.FamilievernConstants
import java.nio.file.NoSuchFileException

class FamilievernConstantsTest : BehaviorSpec({

    Given("a file name of an NON-exsisting mapping file") {
        val fileName = "non_existing_mapping_file.yaml"

        When("opening NON-exsisting mapping file") {
            val thrown = shouldThrow<NoSuchFileException> {
                FamilievernConstants.getResourceAsMappingDescription(fileName)
            }
            Then("NoSuchFileException is thrown") {
                thrown.message shouldContain "Famvern mapping file not found"
            }
        }
    }

    Given("an empty mapping file") {
        When("opening empty mapping filen") {
            val thrown = shouldThrow<MismatchedInputException> {
                FamilievernConstants.getResourceAsMappingDescription("empty_file.yaml")
            }
            Then("MismatchedInputException is thrown") {
                thrown.message shouldContain "No content to map due to end-of-input"
            }
        }
    }

    Given("FamilievernConstants.kontorFylkeRegionMappingList") {
        val sut = FamilievernConstants.kontorFylkeRegionMappingList

        Then("kontorFylkeRegionMappingList should not be empty") {
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