package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.toKostraRecord

class Rule003SkjemaTest : BehaviorSpec({
    Given("context") {
        val sut = Rule003Skjema(
            arguments = KotlinArguments(skjema = "OK", aargang = "2023", region = "1234")
        )
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_SKJEMA, from = 1, to = 2))

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                "OK".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList).shouldBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "XX".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList).shouldNotBeNull()
            }
        }
    }
})