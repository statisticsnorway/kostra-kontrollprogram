package no.ssb.kostra.validation.rule.barnevern

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.BarnevernTestData.kostraIndividInTest
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.marshallInstance
import no.ssb.kostra.barn.xsd.KostraBarnevernType
import no.ssb.kostra.testutil.RandomUtils.generateRandomDuf
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.BarnevernValidator.validateBarnevern
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.BarnevernXmlStreamHandler
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.FixedValidationErrors
import java.time.Year

class BarnevernValidatorTest : BehaviorSpec({

    Given("BarnevernValidator") {
        forAll(
            row(
                "no individ",
                kostraAvgiverTypeInTest,
                mutableListOf(),
                false,
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    ruleName = IndividRuleId.INDIVID_00.title,
                    messageText = "Filen mangler individer"
                ), 4
            ),
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
                ), 54
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
                ), 50
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
                ), 4
            ),
            row(
                "duplicate individ",
                kostraAvgiverTypeInTest,
                mutableListOf(kostraIndividInTest, kostraIndividInTest),
                false,
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    ruleName = IndividRuleId.INDIVID_04.title,
                    messageText = "Dublett for fødselsnummer (${kostraIndividInTest.fodselsnummer}) for " +
                            "journalnummer (${kostraIndividInTest.journalnummer})"
                ), 104
            ),
            row(
                "duplicate individ #2",
                kostraAvgiverTypeInTest,
                mutableListOf(kostraIndividInTest, kostraIndividInTest),
                false,
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    ruleName = IndividRuleId.INDIVID_05.title,
                    messageText = "Dublett for journalnummer (${kostraIndividInTest.journalnummer}) for " +
                            "fødselsnummer (${kostraIndividInTest.fodselsnummer})"
                ), 104
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
                ), 0
            ),
            row(
                "individ fodselsnummer = null",
                kostraAvgiverTypeInTest,
                mutableListOf(
                    kostraIndividInTest.copy(
                        fodselsnummer = null,
                        duFnummer = generateRandomDuf(Year.now().value - 2, Year.now().value - 1)
                    )
                ),
                false,
                ValidationReportEntry(
                    caseworker = kostraIndividInTest.saksbehandler,
                    journalId = kostraIndividInTest.journalnummer,
                    individId = kostraIndividInTest.id,
                    contextId = kostraIndividInTest.id,
                    severity = Severity.WARNING,
                    ruleName = IndividRuleId.INDIVID_11.title,
                    messageText = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
                ), 54
            )
        ) { description, avgiver, individList, destroyXml, expectedResult, expectedNumberOfControls ->

            When(description) {
                val validationResult = validateBarnevern(
                    argumentsInTest.copy(
                        inputFileStream = marshallInstance(
                            KostraBarnevernType(
                                avgiver = avgiver,
                                individ = individList
                            )
                        ).let { if (destroyXml) it.substring(5, it.length) else it }.byteInputStream()
                    )
                )

                Then("result should be as expected") {
                    assertSoftly(validationResult) {
                        reportEntries shouldContain expectedResult
                        numberOfControls shouldBe expectedNumberOfControls
                    }
                }
            }
        }

        When("avgiver missing") {
            val validationResult = validateBarnevern(
                argumentsInTest.copy(inputFileStream = marshallInstance(kostraIndividInTest).byteInputStream())
            )

            Then("result should be as expected") {
                assertSoftly(validationResult) {
                    reportEntries shouldContain ValidationReportEntry(
                        severity = Severity.ERROR,
                        ruleName = AvgiverRuleId.AVGIVER_00.title,
                        messageText = "Antall avgivere skal være 1, fant 0"
                    )
                    numberOfControls shouldBe 50
                }
            }
        }
    }

    Given("BarnevernValidator with streamHandler that throws exception") {
        val streamHandler: BarnevernXmlStreamHandler = mockk()
        every { streamHandler.handleStream(any(), any(), any(), any()) } answers { throw NullPointerException() }

        When("streamHandler throws exception") {
            val result = validateBarnevern(argumentsInTest, streamHandler)

            Then("reportEntries should be as expected, and numberOfControls should be 0") {
                result.shouldNotBeNull()
                assertSoftly(result) {
                    it.reportEntries.size.shouldBe(1)
                    it.reportEntries.shouldContain(FixedValidationErrors.xmlFileError(null))
                    it.numberOfControls.shouldBe(0)
                }
            }
        }
    }
})