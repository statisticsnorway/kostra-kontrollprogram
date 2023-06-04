package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_AARGANG
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.toKostraRecord

class Rule004AargangTest : BehaviorSpec({
    Given("context") {
        val sut = Rule004Aargang(
            arguments = KotlinArguments(skjema = "OK", aargang = "2023", region = "1234")
        )
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_AARGANG, from = 1, to = 4))

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                "2023".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList).shouldBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "XXXX".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList).shouldNotBeNull()
            }
        }
    }
})