package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ORGNR
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toKostraRecord

class Rule007OrganisasjonsnummerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule007Organisasjonsnummer()
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_ORGNR, from = 1, to = 9))
        val arguments = KotlinArguments(skjema = "OK", aargang = "2023", region = "1234", orgnr = "987654321,123456789")

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                "987654321".toKostraRecord(1, fieldDefinitions),
                "123456789".toKostraRecord(2, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList, arguments).shouldBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "XXXXXXXXX".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList, arguments).shouldNotBeNull()
            }
        }
    }
})