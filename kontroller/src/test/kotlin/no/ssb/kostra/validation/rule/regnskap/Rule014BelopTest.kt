package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule014BelopTest : BehaviorSpec({
    Given("valid context") {
        val sut = Rule014Belop()
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_BELOP, from = 1, size = 3))

        forAll(
            row("'  1'", listOf("  1".toKostraRecord(1, fieldDefinitions))),
            row("' -1'", listOf(" -1".toKostraRecord(1, fieldDefinitions))),
            row("'999'", listOf("999".toKostraRecord(1, fieldDefinitions))),
        ) { description, kostraRecordList ->
            When(description) {
                Then("validation should pass with no errors") {
                    sut.validate(kostraRecordList, argumentsInTest).shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        val sut = Rule014Belop()
        val fieldDefinitions = listOf(FieldDefinition(name = FIELD_BELOP, from = 1, size = 3))

        forAll(
            row("'   '", listOf("   ".toKostraRecord(1, fieldDefinitions))),
            row("'XXX'", listOf("XXX".toKostraRecord(1, fieldDefinitions))),
            row("'-1 '", listOf("-1 ".toKostraRecord(1, fieldDefinitions))),
            row("'1  '", listOf("1  ".toKostraRecord(1, fieldDefinitions))),
            row("'\\t-1'", listOf("\t-1".toKostraRecord(1, fieldDefinitions))),
            row("'\\t\\t\\t'", listOf("\t\t\t".toKostraRecord(1, fieldDefinitions)))
        ) { description, kostraRecordList ->
            When(description) {
                Then("validation should result in errors") {
                    sut.validate(kostraRecordList, argumentsInTest).shouldNotBeNull()
                }
            }
        }
    }
})