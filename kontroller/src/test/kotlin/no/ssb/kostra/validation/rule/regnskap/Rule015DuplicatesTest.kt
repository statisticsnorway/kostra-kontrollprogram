package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule015DuplicatesTest : BehaviorSpec({
    Given("valid context") {
        val sut = Rule015Duplicates(listOf("a", "b") to listOf("A", "B"))

        forAll(
            row(
                "No duplicates",
                listOf(
                    mapOf("a" to "1", "b" to "2").toKostraRecord(),
                    mapOf("a" to "2", "b" to "1").toKostraRecord(2)
                )
            )
        ) { description, kostraRecordList ->
            When(description) {
                Then("validation should pass with no errors") {
                    sut.validate(kostraRecordList).shouldBeNull()
                }
            }
        }

        forAll(
            row(
                "Has duplicates",
                listOf(
                    mapOf("a" to "1", "b" to "2").toKostraRecord(),
                    mapOf("a" to "1", "b" to "2").toKostraRecord(2),
                    mapOf("a" to "3", "b" to "4").toKostraRecord(3),
                    mapOf("a" to "3", "b" to "4").toKostraRecord(4)
                )
            )
        ) { description, kostraRecordList ->
            When(description) {
                val validationReportEntries = sut.validate(kostraRecordList)

                Then("validation should result in errors") {
                    validationReportEntries.shouldNotBeNull()
                }
            }
        }
    }
})