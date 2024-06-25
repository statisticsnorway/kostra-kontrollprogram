package no.ssb.kostra.program

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.extension.buildFieldDefinitions

class FileDescriptionLoaderTest : BehaviorSpec({
    Given("a file description with test values") {
        val sut = FileDescriptionLoader
            .getResourceAsFileDescription("file_description_test.yaml")

        When("FileDescription is created") {
            Then("FileDescription should be as expected") {
                assertSoftly(sut) {
                    title shouldBe "TEST Filbeskrivelse"
                    reportingYear shouldBe 2024
                    description shouldBe "Beskrivelse for filbeskrivelse"
                    fields.size shouldBe 7

                }
            }
        }
    }

    Given("a file description that does not exist") {
        val sut = FileDescriptionLoader
            .getResourceAsFileDescription("file_description_that_does_not_exist.yml")

        When("FileDescription is created") {
            Then("FileDescription should be as expected") {
                assertSoftly(sut) {
                    title shouldBe "File description"
                    reportingYear shouldBe 0
                    description shouldBe "Default file description"
                    fields.size shouldBe 0

                }
            }
        }
    }
    Given("a set of FileDescription") {
        forAll(
//            row("0AK1"),
//            row("0A"),
//            row("0B"),
//            row("0I"),
//            row("0J"),
//            row("0N"),
//            row("0Q"),
//            row("0F"),
//            row("0X"),
            row("11F Økonomisk sosialhjelp", "11F"),
            row("11CF Kvalifiseringsstønad", "11CF"),
//            row("52AF"),
//            row("52BF"),
//            row("53F"),
//            row("55F"),
//            row("FAIL"),

        ) { description, schema ->
            When("comparing consecutive FieldDefinitions for $description") {
                val sut = FileDescriptionLoader
                    .getResourceAsFileDescription("file_description_$schema.yaml")
                val fieldDefinitionPairs = sut.fields.buildFieldDefinitions().zipWithNext()

                Then("from plus size should be equal to the next from") {
                    assertSoftly(fieldDefinitionPairs) {
                        all { (a, b) -> a.from + a.size == b.from }
                    }
                }
            }
        }
    }

})