package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class FileDescriptionLoaderTest : BehaviorSpec({

    Given("SosialhjelpFieldDefinitions") {
        forAll(
            row("sosialhjelp_11_filedescription.yaml", 78),
            row("nonExistingFile.yaml", 0),
        ) { fileName, expectedFieldsCount ->
            When(fileName) {
                val sut = FileDescriptionLoader.getResourceAsFileDescription(fileName)

                Then("expected count should be $expectedFieldsCount") {
                    if (expectedFieldsCount == 0) {
                        sut.shouldBeNull()

                    } else {
                        sut.shouldNotBeNull()
                        sut.fields.size shouldBe expectedFieldsCount

                    }
                }
            }
        }
    }
})