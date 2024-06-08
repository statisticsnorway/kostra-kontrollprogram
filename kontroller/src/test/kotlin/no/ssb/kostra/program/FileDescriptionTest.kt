package no.ssb.kostra.program

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions
import no.ssb.kostra.program.extension.toFileDescription

class FileDescriptionTest : BehaviorSpec({

    Given("FileDescription with default values") {
        val sut = "test".toFileDescription()

        When("FileDescription is created") {
            Then("FileDescription should be as expected") {
                assertSoftly(sut) {
                    title shouldBe "Filebeskrivelse"
                    fields.size shouldBe 7
                    fields[0].dataType shouldBe DataType.STRING_TYPE
                }
                println(sut)
            }
        }
    }

    Given("Non-existing fileDescription") {
        When("FileDescription is created") {
            Then("result should throw NullPointerException") {
                shouldThrow<NullPointerException> { "FAIL".toFileDescription() }
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
            row("11F Økonomisk sosialhjelp", "11F", SosialhjelpFieldDefinitions),
            row("11CF Kvalifiseringsstønad", "11CF", KvalifiseringFieldDefinitions),
//            row("52AF"),
//            row("52BF"),
//            row("53F"),
//            row("55F"),
//            row("FAIL"),

        ) { description, schema, fieldDefinitionsClass ->
            When("comparing FileDescription for $description with ${fieldDefinitionsClass::class}") {
                val sut = schema.toFileDescription()
                val fileFieldDefinitions = sut.fields
                val fieldDefinitions = fieldDefinitionsClass.fieldDefinitions
                val definitions = fileFieldDefinitions.zip(fieldDefinitions)

                Then("FileDescription should be as expected") {
                    assertSoftly(sut) {
                        fileFieldDefinitions.size shouldBe fieldDefinitions.size
                        definitions.forEach { (fileFieldDefinition, fieldDefinition) ->
                            fileFieldDefinition.shouldBeEqualToComparingFields(fieldDefinition)
                        }
                    }
                }
            }
        }
    }


})