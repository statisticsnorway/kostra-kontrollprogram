package no.ssb.kostra.validation.rule.famvern

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.program.FileLoader

class FamilievernConstantsTest :
    BehaviorSpec({
        Given("a file name of a mapping file") {
            val fileName = "mapping_familievern_region_fylke_kontor_2024.yaml"

            When("opening the mapping file") {

                val result =
                    FileLoader
                        .getResource<FamilievernConstants.MappingDescription>(fileName)

                Then("result is not empty and should contain data") {
                    result.title.isNotEmpty()
                    result.year != 0
                    result.description.isNotEmpty()
                    result.regions.isNotEmpty()
                    result.regions
                        .first()
                        .counties
                        .isNotEmpty()
                    result.regions
                        .first()
                        .counties
                        .first()
                        .offices
                        .isNotEmpty()
                }
            }
        }

        Given("FamilievernConstants.kontorFylkeRegionMappingList") {
            val sut = FamilievernConstants.kontorFylkeRegionMappingList

            Then("kontorFylkeRegionMappingList should not be empty and should contain data") {
                sut.isNotEmpty()
                sut.shouldContain(
                    FamilievernConstants.KontorFylkeRegionMapping(
                        region = "667600",
                        fylke = "03",
                        kontor = "030",
                    ),
                )
                sut.shouldContain(
                    FamilievernConstants.KontorFylkeRegionMapping(
                        region = "667200",
                        fylke = "56",
                        kontor = "205",
                    ),
                )
            }
        }
    })
