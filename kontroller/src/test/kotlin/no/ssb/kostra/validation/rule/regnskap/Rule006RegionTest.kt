package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toKostraRecord

class Rule006RegionTest : BehaviorSpec({
    Given("context") {
        val sut = Rule006Region()
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_REGION, from = 1, size = 4))
        val arguments = KotlinArguments(skjema = "OK", aargang = "2023", region = "1234")

        When("valid list of KostraRecord") {
            val kostraRecordList = listOf(
                "1234".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should pass with no errors") {
                sut.validate(kostraRecordList, arguments).shouldBeNull()
            }
        }

        When("invalid list of KostraRecord") {
            val kostraRecordList = listOf(
                "XXXX".toKostraRecord(1, fieldDefinitions)
            )

            Then("validation should result in errors") {
                sut.validate(kostraRecordList, arguments).shouldNotBeNull()
            }
        }
    }
})