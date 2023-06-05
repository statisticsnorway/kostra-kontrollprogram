package no.ssb.kostra.validation.rule.barnevern

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.BarnevernTestData.kostraIndividInTest
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.marshallInstance
import no.ssb.kostra.barn.xsd.KostraBarnevernType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.BarnevernValidator.mapToValidationReportEntries
import no.ssb.kostra.validation.rule.barnevern.BarnevernValidator.validateBarnevern
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleId
import java.time.Year

class BarnevernValidatorTest : BehaviorSpec({

    Given("validateBarnevern") {

        forAll(
            row(
                "empty individ",
                kostraAvgiverTypeInTest,
                kostraIndividInTest,
                listOf(
                    ValidationReportEntry(
                        caseworker = "~saksbehandler~",
                        journalId = "~journalnummer~",
                        individId = "C1",
                        contextId = "C1",
                        severity = Severity.ERROR,
                        ruleName = IndividRuleId.INDIVID_06.title,
                        messageText="Individet har ingen meldinger, planer eller tiltak i løpet av året"
                    )
                )
            )
        ) { description, avgiver, individ, expectedResult ->

            val barnevernType = KostraBarnevernType(
                avgiver = avgiver,
                individ = mutableListOf(individ)
            )

            val args = KotlinArguments(
                skjema = "15F",
                aargang = (Year.now().value - 1).toString(),
                region = "1234",
                inputFileStream = marshallInstance(barnevernType).byteInputStream()
            )

            When(description) {
                val reportEntries = validateBarnevern(args)

                Then("result should be as expected") {
                    reportEntries shouldBe expectedResult
                }
            }
        }
    }

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
                        ruleName = IndividRuleId.INDIVID_04.title,
                        messageText = "~messageText~ (~listItem~)"
                    )
                )
            )
        ) { description, sut, expectedResult ->

            When(description) {

                val reportEntries = sut.mapToValidationReportEntries(
                    IndividRuleId.INDIVID_04.title,
                    "~messageText~"
                )

                Then("result should be as expected") {
                    reportEntries shouldBe expectedResult
                }
            }
        }
    }
})
