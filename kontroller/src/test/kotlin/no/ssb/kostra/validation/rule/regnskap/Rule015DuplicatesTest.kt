package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KostraRecord

class Rule015DuplicatesTest : BehaviorSpec({
    Given("valid context") {
        val sut = Rule015Duplicates(
            listOf("a", "b") to listOf("A", "B")
        )
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 1, name = "a"),
            FieldDefinition(from = 3, to = 6, name = "b")
        ).associateBy { it.name }

        forAll(
            row(
                "No duplicates",
                listOf(
                    KostraRecord(
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf("a" to "1", "b" to "2"),
                        lineNumber = 1
                    ),
                    KostraRecord(
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf("a" to "2", "b" to "1"),
                        lineNumber = 2
                    )
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
                    KostraRecord(
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf("a" to "1", "b" to "2"),
                        lineNumber = 1
                    ),
                    KostraRecord(
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf("a" to "1", "b" to "2"),
                        lineNumber = 2
                    ),
                    KostraRecord(
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf("a" to "3", "b" to "4"),
                        lineNumber = 3
                    ),
                    KostraRecord(
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf("a" to "3", "b" to "4"),
                        lineNumber = 4
                    )
                )
            )
        ) { description, kostraRecordList ->
            When(description) {
                val validationReportEntries = sut.validate(kostraRecordList)
                Then("validation should result in errors") {
                    validationReportEntries.shouldNotBeNull()
                    println(validationReportEntries)
                }
            }
        }
    }
})