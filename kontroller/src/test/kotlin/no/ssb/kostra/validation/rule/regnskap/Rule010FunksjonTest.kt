package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.extension.toKostraRecord

class Rule010FunksjonTest : BehaviorSpec({
    Given("context") {
        val sut = Rule010Funksjon(
            funksjonList = listOf("100 ", "400 ")
        )
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_FUNKSJON, from = 1, to = 4))

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
        val sut = Rule010Funksjon(
            funksjonList = emptyList()
        )
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_FUNKSJON, from = 1, to = 4))

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