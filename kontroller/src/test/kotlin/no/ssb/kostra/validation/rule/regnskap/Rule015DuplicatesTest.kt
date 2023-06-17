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
            listOf("a", "a") to listOf("A", "B")
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
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf("a" to "1", "b" to "2")
                    ),
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf("a" to "2", "b" to "1")
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
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf("a" to "1", "b" to "2")
                    ),
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf("a" to "1", "b" to "2")
                    )
                )
            )
        ) { description, kostraRecordList ->
            When(description) {
                Then("validation should result in errors") {
                    sut.validate(kostraRecordList).shouldNotBeNull()
                }
            }
        }
    }
})