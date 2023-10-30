package no.ssb.kostra.web.extension

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.report.ValidationResult
import no.ssb.kostra.web.extension.MappingToConsoleAppExtensionsKtTest.Companion.generateCompanyIdInTest
import no.ssb.kostra.web.extensions.reduceReportEntries
import no.ssb.kostra.web.extensions.toErrorReportVm
import no.ssb.kostra.web.extensions.toKostraArguments
import no.ssb.kostra.web.viewmodel.CompanyIdVm
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.time.Year

class MappingFromConsoleAppExtensionsKtTest : BehaviorSpec({

    Given("List<ValidationReportEntry>.reduceReportEntries") {
        val firstValidationReportEntry = ValidationReportEntry(
            severity = Severity.ERROR,
            caseworker = "caseworker",
            journalId = "journalId",
            individId = "individId",
            contextId = "contextId",
            ruleName = "ruleName",
            messageText = "messageText",
            lineNumbers = listOf(3, 2, 1, 4)
        )

        val secondValidationReportEntry = firstValidationReportEntry.copy(
            caseworker = "caseworker2",
            journalId = "journalId2",
            individId = "individId2",
            contextId = "contextId2",
            lineNumbers = listOf(4, 5, 6)
        )

        val thirdValidationReportEntry = firstValidationReportEntry.copy(
            caseworker = "caseworker3",
            journalId = "journalId3",
            individId = "individId3",
            contextId = "contextId3",
            lineNumbers = emptyList()
        )

        When("reduceReportEntries") {
            val reducedList = listOf(
                firstValidationReportEntry,
                secondValidationReportEntry,
                thirdValidationReportEntry
            ).reduceReportEntries()

            Then("reducedList should be as expected") {
                reducedList.size shouldBe 2
                assertSoftly(reducedList.first()) {
                    severity shouldBe firstValidationReportEntry.severity
                    caseworker shouldBe firstValidationReportEntry.caseworker
                    journalId shouldBe firstValidationReportEntry.journalId
                    individId shouldBe firstValidationReportEntry.individId
                    contextId shouldBe firstValidationReportEntry.contextId
                    ruleName shouldBe firstValidationReportEntry.ruleName
                    messageText shouldBe firstValidationReportEntry.messageText
                    lineNumbers shouldBe listOf(3, 2, 1, 4, 4, 5, 6)
                }

                assertSoftly(reducedList.last()) {
                    severity shouldBe thirdValidationReportEntry.severity
                    caseworker shouldBe thirdValidationReportEntry.caseworker
                    journalId shouldBe thirdValidationReportEntry.journalId
                    individId shouldBe thirdValidationReportEntry.individId
                    contextId shouldBe thirdValidationReportEntry.contextId
                    ruleName shouldBe thirdValidationReportEntry.ruleName
                    messageText shouldBe thirdValidationReportEntry.messageText
                    lineNumbers.shouldBeEmpty()
                }
            }
        }
    }

    Given("ValidationReportArguments.toErrorReportVm") {
        val kotlinArguments = KostraFormVm(
            skjema = "0A",
            aar = Year.now().value,
            region = "123456",
            orgnrForetak = generateCompanyIdInTest('9'),
            orgnrVirksomhet = listOf(
                CompanyIdVm(generateCompanyIdInTest('8')),
                CompanyIdVm(generateCompanyIdInTest('9'))
            )
        ).toKostraArguments("".byteInputStream(), null)

        val firstValidationReportEntry = ValidationReportEntry(
            severity = Severity.ERROR,
            caseworker = "caseworker",
            journalId = "journalId",
            individId = "individId",
            contextId = "contextId",
            ruleName = "ruleName",
            messageText = "messageText",
            lineNumbers = listOf(4, 2, 1, 3)
        )

        val secondValidationReportEntry = firstValidationReportEntry.copy(
            caseworker = "caseworker2",
            journalId = "journalId2",
            individId = "individId2",
            contextId = "contextId2",
            lineNumbers = listOf(4, 5, 6)
        )

        val validationResult = ValidationResult(
            reportEntries = listOf(firstValidationReportEntry, secondValidationReportEntry),
            numberOfControls = 42
        )

        val sut = ValidationReportArguments(
            kotlinArguments = kotlinArguments,
            validationResult = validationResult
        )

        When("toErrorReportVm") {
            val fileReportVm = sut.toErrorReportVm()

            Then("fileReportVm should be as expected") {
                assertSoftly(fileReportVm.innparametere) {
                    skjema shouldBe kotlinArguments.skjema
                    aar shouldBe kotlinArguments.aargang.toInt()
                    region shouldBe kotlinArguments.region
                    orgnrForetak shouldBe kotlinArguments.foretaknr
                    navn shouldBe kotlinArguments.navn
                    orgnrVirksomhet shouldBe kotlinArguments.orgnr.split(",")
                        .map { CompanyIdVm(orgnr = it) }
                }

                assertSoftly(fileReportVm) {
                    antallKontroller shouldBe validationResult.numberOfControls
                    severity shouldBe Severity.ERROR
                    feil.size shouldBe 1
                }

                assertSoftly(fileReportVm.feil.first()) {
                    severity shouldBe firstValidationReportEntry.severity
                    caseworker shouldBe firstValidationReportEntry.caseworker
                    journalId shouldBe firstValidationReportEntry.journalId
                    individId shouldBe firstValidationReportEntry.individId
                    contextId shouldBe firstValidationReportEntry.contextId
                    ruleName shouldBe firstValidationReportEntry.ruleName
                    messageText shouldBe firstValidationReportEntry.messageText.replace("<br/>", "")
                    lineNumbers shouldBe listOf(1, 2, 3, 4, 5, 6)
                }
            }
        }
    }
})
