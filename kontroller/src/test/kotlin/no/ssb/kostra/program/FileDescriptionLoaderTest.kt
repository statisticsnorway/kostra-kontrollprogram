package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class FileDescriptionLoaderTest : BehaviorSpec({

    Given("paths to file descriptions and expected counts") {
        forAll(
            row("sosialhjelp_11_filedescription.yaml", 78),
            row("emptyFileDescription.yaml", 0),
            row("non_existing_file.yaml", 0),
        ) { fileName, expectedFieldsCount ->
            When(fileName) {
                val sut = FileDescriptionLoader.getResourceAsFieldDefinitionList(fileName)

                Then("expected count should be $expectedFieldsCount") {
                    sut.size shouldBe expectedFieldsCount

                }
            }
        }
    }
})