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

    Given("a file description that is empty") {
        val sut = FileDescriptionLoader
            .getResourceAsFileDescription("file_description_empty.yml")

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
            row("11F Økonomisk sosialhjelp", "11F", 322),
            row("11CF Kvalifiseringsstønad", "11CF", 136),
            row("52AF Familievernsaker, klientrapportering", "52AF", 151),
            row("52BF Gruppeskjema for familievernet", "52BF", 86),
            row("53F Utadrettet virksomhet i familieverntjenesten", "53F", 61),
            row("55F Meklingssaker i familieverntjenesten", "55F", 986),
            row("Alle regnskapsskjema", "Regnskap", 48),
        ) { description, schema, recordLength ->
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

            When("last field definition is found for $description") {
                val sut = FileDescriptionLoader
                    .getResourceAsFileDescription("file_description_$schema.yaml")
                val fieldDefinition = sut.fields.buildFieldDefinitions().last()

                Then("length of all fields should be as expected, $recordLength") {
                    assertSoftly(fieldDefinition) {
                        it.from + it.size - 1 shouldBe recordLength
                    }
                }

            }
        }
    }
})