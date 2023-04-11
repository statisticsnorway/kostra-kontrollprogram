package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.toKostraRecord

class Rule011KapittelTest : BehaviorSpec({
    Given("context") {
        val sut = Rule011Kapittel(
            kapittelList = listOf("100 ", "400 ")
        )
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_KAPITTEL, from = 1, to = 4))

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                "100 ".toKostraRecord(1, fieldDefinitions),
                "400 ".toKostraRecord(2, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList).shouldBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "XXX ".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList).shouldNotBeNull()
            }
        }
    }

    Given("no context") {
        val sut = Rule011Kapittel(
            kapittelList = listOf()
        )
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_KAPITTEL, from = 1, to = 4))

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                "100 ".toKostraRecord(1, fieldDefinitions),
                "400 ".toKostraRecord(2, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList).shouldBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "XXX ".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList).shouldBeNull()
            }
        }
    }
})