package no.ssb.kostra.program

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.nio.file.NoSuchFileException

class FileLoaderTest :
    BehaviorSpec({
        Given("a file description with test values") {
            val sut =
                FileLoader
                    .getResource<FileDescription>("file_description_test.yaml")

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

        Given("a file name of an NON-existing mapping file") {
            val fileName = "non_existing.yaml"

            When("opening the NON-existing mapping file") {
                val thrown =
                    shouldThrow<NoSuchFileException> {
                        FileLoader.getResource<FileDescription>(fileName)
                    }
                Then("NoSuchFileException is thrown") {
                    thrown.message shouldContain "File not found"
                }
            }
        }

        Given("a file name of an empty mapping file") {
            When("opening the empty mapping file") {
                val thrown =
                    shouldThrow<MismatchedInputException> {
                        FileLoader.getResource<FileDescription>("empty.yaml")
                    }
                Then("MismatchedInputException is thrown") {
                    thrown.message shouldContain "No content to map due to end-of-input"
                }
            }
        }

        Given("a set of FileDescription") {
            forAll(
                row(
                    "11F Økonomisk sosialhjelp",
                    "11F_2025",
                    338
                ),
                row(
                    "11CF Kvalifiseringsstønad",
                    "11CF_2025",
                    152
                ),
                row(
                    "52AF Familievernsaker, klientrapportering",
                    "52AF_2024",
                    151,
                ),
                row(
                    "52BF Gruppeskjema for familievernet",
                    "52BF_2024",
                    88
                ),
                row(
                    "53F Utadrettet virksomhet i familieverntjenesten",
                    "53F_2024",
                    61,
                ),
                row(
                    "55F Meklingssaker i familieverntjenesten",
                    "55F_2024",
                    986,
                ),
                row(
                    "Alle regnskapsskjema",
                    "Regnskap",
                    48
                ),
            ) { description, schema, recordLength ->
                When("checking $description") {
                    val sut =
                        FileLoader
                            .getResource<FileDescription>("file_description_$schema.yaml")

                    Then("from plus size should be equal to the next from") {
                        sut.fields.zipWithNext().forEach { (a, b) ->
                            a.from + a.size shouldBe b.from
                        }
                    }

                    Then("the 'to' of the last field definition should be equal to $recordLength") {
                        sut.fields.last().to shouldBe recordLength
                    }

                    Then("the sum of size for all fields should be equal to $recordLength") {
                        sut.fields.sumOf { it.size } shouldBe recordLength
                    }
                }
            }
        }
    })
