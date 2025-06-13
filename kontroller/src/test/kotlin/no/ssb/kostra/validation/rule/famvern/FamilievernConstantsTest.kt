package no.ssb.kostra.validation.rule.famvern

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.program.FileLoader

class FamilievernConstantsTest :
    BehaviorSpec({
        Given("a file name of a mapping file") {
            val fileName = "mapping_familievern_region_fylke_kontor_2025.yaml"

            When("opening the mapping file") {

                val result =
                    FileLoader
                        .getResource<FamilievernConstants.FamvernHierarchyMapping>(fileName)

                Then("result is not empty and should contain data") {
                    result.title.isNotEmpty()
                    result.year != 0
                    result.description.isNotEmpty()
                    result.mappings.isNotEmpty()
                }
            }
        }

        Given("FamilievernConstants.kontorFylkeRegionMappingList") {
            val sut = FamilievernConstants.famvernHierarchyKontorFylkeRegionMappingList

            Then("kontorFylkeRegionMappingList should not be empty and should contain data") {
                sut.isNotEmpty()
            }
        }
    })
