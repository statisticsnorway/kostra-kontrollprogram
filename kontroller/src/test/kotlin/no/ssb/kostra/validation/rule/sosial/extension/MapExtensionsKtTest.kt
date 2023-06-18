package no.ssb.kostra.validation.rule.sosial.extension

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
                        messageText = "~messageText~ (~listItem~)"
                    )
                )
            )
        ) { description, sut, expectedResult ->

            When(description) {

                val reportEntries = sut.mapToValidationReportEntries(
                    "~someTitle~",
                    "~messageText~"
                )

                Then("result should be as expected") {
                    reportEntries shouldBe expectedResult
                }
            }
        }
    }
})
