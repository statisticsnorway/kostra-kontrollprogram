package no.ssb.kostra.validation

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.rule.AbstractRule

class PositionedFileValidatorTest : BehaviorSpec({

    Given("Validator with empty FieldDefinitions") {
        val sut = object : PositionedFileValidator(
            KotlinArguments(
                skjema = "S",
                aargang = "YYYY",
                region = "RRRR"
            )
        ) {
            override val fatalRules: List<AbstractRule<List<String>>>
                get() = emptyList()
            override val validationRules: List<AbstractRule<List<KostraRecord>>>
                get() = emptyList()
            override val fieldDefinitions: FieldDefinitions
                get() = object : FieldDefinitions {
                    override val fieldDefinitions: List<FieldDefinition>
                        get() = emptyList()
                }
        }

        When("validate") {
            val thrown = shouldThrow<IndexOutOfBoundsException> {
                sut.validate()
            }

            Then("exception should be as expected") {
                thrown.message shouldBe "validate(): fieldDefinitions are missing"
            }
        }
    }
})
