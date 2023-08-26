package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule009KontoklasseTest : BehaviorSpec({
    Given("context") {
        val sut = Rule009Kontoklasse(
            kontoklasseList = listOf("0", "1")
        )
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_KONTOKLASSE, from = 1, to = 1))

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                "0".toKostraRecord(1, fieldDefinitions),
                "1".toKostraRecord(2, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList, argumentsInTest).shouldBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "X".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList, argumentsInTest).shouldNotBeNull()
            }
        }
    }
})