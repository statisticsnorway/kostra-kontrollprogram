package no.ssb.kostra.area.sosial.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

class MapExtensionsKtTest : BehaviorSpec({

    Given("Map<String, Collection<String>>.mapToValidationReportEntries") {

        forAll(
            row(
                "empty map",
                emptyMap(),
                emptyList()
            ),
            row(
                "map with single entry that maps to empty list",
                mapOf<String, Collection<String>>("~entry~" to emptyList()),
                emptyList()
            ),
            row(
                "map with single entry that maps to list with one item",
                mapOf<String, Collection<String>>("~entry~" to listOf("~listItem~")),
                listOf(
                    ValidationReportEntry(
                        severity = Severity.ERROR,
                        ruleName = "~someTitle~",
                        messageText = "Dublett for fødselsnummer (~entry~) for journalnummer (~listItem~)"
                    )
                )
            )
        ) { description, sut, expectedResult ->

            When(description) {

                val reportEntries = sut.mapToValidationReportEntries(
                    "~someTitle~",
                    messageTemplateFunc = { key, values -> "Dublett for fødselsnummer ($key) for journalnummer ($values)" }
                )

                Then("result should be as expected") {
                    reportEntries shouldBe expectedResult
                }
            }
        }
    }
})
