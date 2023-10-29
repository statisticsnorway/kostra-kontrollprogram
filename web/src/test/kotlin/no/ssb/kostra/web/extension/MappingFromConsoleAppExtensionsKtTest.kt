package no.ssb.kostra.web.extension

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.report.ValidationResult
import no.ssb.kostra.web.extensions.toErrorReportVm
import no.ssb.kostra.web.viewmodel.CompanyIdVm
import java.time.LocalDateTime

class MappingFromConsoleAppExtensionsKtTest : BehaviorSpec({

    Given("ValidationReportArguments.toErrorReportVm") {
        val kotlinArguments = KotlinArguments(
            skjema = "0A",
            aargang = "2020",
            kvartal = "1",
            region = "123456",
            navn = "Uoppgitt",
            orgnr = "987654321",
            foretaknr = "123456789",
            harVedlegg = true,
            isRunAsExternalProcess = false,
            inputFileContent = "1;2;3;4;5;6;7;8;9;10;11;12;13;14;15",
            startTime = LocalDateTime.now()
        )

        val validationReportEntry = ValidationReportEntry(
            severity = Severity.ERROR,
            caseworker = "caseworker",
            journalId = "journalId",
            individId = "individId",
            contextId = "contextId",
            ruleName = "ruleName",
            messageText = "messageText",
            lineNumbers = listOf(1, 2, 3)
        )

        val validationResult = ValidationResult(
            reportEntries = listOf(validationReportEntry),
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
                    kvartal shouldBe kotlinArguments.kvartal
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
                    severity shouldBe validationReportEntry.severity
                    caseworker shouldBe validationReportEntry.caseworker
                    journalId shouldBe validationReportEntry.journalId
                    individId shouldBe validationReportEntry.individId
                    contextId shouldBe validationReportEntry.contextId
                    ruleName shouldBe validationReportEntry.ruleName
                    messageText shouldBe validationReportEntry.messageText.replace("<br/>", "")
                    lineNumbers shouldBe validationReportEntry.lineNumbers
                }
            }
        }
    }
})
