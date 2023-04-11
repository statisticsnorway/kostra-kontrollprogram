package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FORETAKSNR
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.toKostraRecord

class Rule008ForetaksnummerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule008Foretaksnummer(
            arguments = Arguments(skjema = "OK", aargang = "2023", region = "1234", foretaknr = "987654321")
        )
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_FORETAKSNR, from = 1, to = 9))

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                "987654321".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList).shouldBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "XXXXXXXXX".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList).shouldNotBeNull()
            }
        }
    }
})