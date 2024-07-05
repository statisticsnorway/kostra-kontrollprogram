package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule013SektorTest : BehaviorSpec({
    Given("context") {
        val sut = Rule013Sektor(
            sektorList = listOf("100", "400")
        )
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_SEKTOR, from = 1, size = 3))

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                "100".toKostraRecord(1, fieldDefinitions),
                "400".toKostraRecord(2, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList, argumentsInTest).shouldBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "XXX".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList, argumentsInTest).shouldNotBeNull()
            }
        }
    }

    Given("no context") {
        val sut = Rule013Sektor(
            sektorList = emptyList()
        )
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_SEKTOR, from = 1, size = 3))

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                "100".toKostraRecord(1, fieldDefinitions),
                "400".toKostraRecord(2, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList, argumentsInTest).shouldNotBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "XXX".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList, argumentsInTest).shouldNotBeNull()
            }
        }
    }
})