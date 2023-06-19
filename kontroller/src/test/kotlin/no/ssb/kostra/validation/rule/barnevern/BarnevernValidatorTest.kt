package no.ssb.kostra.validation.rule.barnevern

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContain
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.BarnevernTestData.kostraIndividInTest
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.marshallInstance
import no.ssb.kostra.barn.xsd.KostraBarnevernType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.RandomUtils.generateRandomDuf
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
                    messageText = "Dublett for fødselsnummer (${kostraIndividInTest.fodselsnummer}) for " +
                            "journalnummer (${kostraIndividInTest.journalnummer})"
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
                    messageText = "Dublett for journalnummer (${kostraIndividInTest.journalnummer}) for " +
                            "fødselsnummer (${kostraIndividInTest.fodselsnummer})"
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
                )
            )
        ) { description, avgiver, individList, destroyXml, expectedResult ->

            When(description) {
                val reportEntries = validateBarnevern(
                    arguments = KotlinArguments(
                        skjema = "15F",
                        aargang = (Year.now().value - 1).toString(),
                        region = "1234",
                        inputFileStream = marshallInstance(
                            KostraBarnevernType(
                                avgiver = avgiver,
                                individ = individList
                            )
                        ).let { if (destroyXml) it.substring(5, it.length) else it }.byteInputStream()
                    )
                )

                Then("result should be as expected") {
                    reportEntries shouldContain expectedResult
                }
            }
        }
    }
})