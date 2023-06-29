package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KVARTAL
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toKostraRecord

class Rule005KvartalTest : BehaviorSpec({
    Given("context") {
        val sut = Rule005Kvartal()
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_KVARTAL, from = 1, to = 1))
        val arguments = KotlinArguments(skjema = "OK", aargang = "2023", region = "1234")

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                " ".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList, arguments).shouldBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "X".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList, arguments).shouldNotBeNull()
            }
        }
    }
})