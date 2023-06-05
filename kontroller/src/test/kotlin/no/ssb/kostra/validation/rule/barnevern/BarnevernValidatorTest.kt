package no.ssb.kostra.validation.rule.barnevern

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContain
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
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.AvgiverRuleId
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleId
import java.time.Year

class BarnevernValidatorTest : BehaviorSpec({

    Given("validateBarnevern") {

        forAll(
            row(
                "empty individ",
                kostraAvgiverTypeInTest,
                mutableListOf(kostraIndividInTest),
                false,
                ValidationReportEntry(
                    caseworker = kostraIndividInTest.saksbehandler,
                    journalId = kostraIndividInTest.journalnummer,
                    individId = kostraIndividInTest.id,
                    contextId = kostraIndividInTest.id,
                    severity = Severity.ERROR,
                    ruleName = IndividRuleId.INDIVID_06.title,
                    messageText = "Individet har ingen meldinger, planer eller tiltak i løpet av året"
                )
            ),
            row(
                "invalid avgiver",
                kostraAvgiverTypeInTest.copy(versjon = 42),
                mutableListOf(kostraIndividInTest),
                false,
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    ruleName = AvgiverRuleId.AVGIVER_01.title,
                    messageText = "Klarer ikke å validere Avgiver mot filspesifikasjon"
                )
            ),
            row(
                "invalid individ",
                kostraAvgiverTypeInTest,
                mutableListOf(kostraIndividInTest.copy(fodselsnummer = "abc")),
                false,
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    ruleName = IndividRuleId.INDIVID_01.title,
                    messageText = "Definisjon av Individ er feil i forhold til filspesifikasjonen"
                )
            ),
            row(
                "duplicate individ",
                kostraAvgiverTypeInTest,
                mutableListOf(kostraIndividInTest, kostraIndividInTest),
                false,
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    ruleName = IndividRuleId.INDIVID_04.title,
                    messageText = "Dublett for fødselsnummer for journalnummer (${kostraIndividInTest.journalnummer})"
                )
            ),
            row(
                "duplicate individ #2",
                kostraAvgiverTypeInTest,
                mutableListOf(kostraIndividInTest, kostraIndividInTest),
                false,
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    ruleName = IndividRuleId.INDIVID_05.title,
                    messageText = "Dublett for journalnummer for fødselsnummer (${kostraIndividInTest.fodselsnummer})"
                )
            ),
            row(
                "invalid XML",
                kostraAvgiverTypeInTest,
                mutableListOf(kostraIndividInTest),
                true,
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    messageText = "Klarer ikke å lese fil. Får feilmeldingen: Unexpected character 'e' " +
                            "(code 101) in prolog; expected '<'\n at [row,col {unknown-source}]: [1,1]"
                )
            )
        ) { description, avgiver, individList, destroyXml, expectedResult ->

            val barnevernType = KostraBarnevernType(
                avgiver = avgiver,
                individ = individList
            )

            val xml = marshallInstance(barnevernType)

            val args = KotlinArguments(
                skjema = "15F",
                aargang = (Year.now().value - 1).toString(),
                region = "1234",
                inputFileStream = (if (destroyXml) xml.substring(5, xml.length) else xml).byteInputStream()
            )

            When(description) {
                val reportEntries = validateBarnevern(args)

                Then("result should be as expected") {
                    reportEntries shouldContain expectedResult
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
