package no.ssb.kostra.program.util

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.program.extension.toInt
import no.ssb.kostra.program.util.ConversionUtils.fromArguments
import no.ssb.kostra.program.util.ConversionUtils.toErrorReportEntry
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

class ConversionUtilsTest : BehaviorSpec({

    Given("ValidationReportEntry with all props set") {
        forAll(
            row("with line numbers", listOf(1, 2, 3), " (linje(r): 1, 2, 3)"),
            row("without line numbers", emptyList(), "")
        ) { description, lineNumbers, expectedLineNumbersText ->

            val validationReportEntry = ValidationReportEntry(
                severity = Severity.WARNING,
                caseworker = "~caseworker~",
                journalId = "~journalId~",
                individId = "~individId~",
                contextId = "~contextId~",
                ruleName = "~ruleName~",
                messageText = "~messageText~",
                lineNumbers = lineNumbers
            )

            When("toErrorReportEntry $description") {
                val errorReportEntry = toErrorReportEntry(validationReportEntry)

                Then("errorReportEntry should be as expected") {
                    assertSoftly(errorReportEntry) {
                        saksbehandler shouldBe validationReportEntry.caseworker
                        journalnummer shouldBe validationReportEntry.journalId
                        individId shouldBe validationReportEntry.individId
                        refNr shouldBe ""
                        kontrollNr shouldBe validationReportEntry.ruleName
                        errorText shouldBe validationReportEntry.messageText + expectedLineNumbersText
                        errorType shouldBe validationReportEntry.severity.toInt()
                    }
                }
            }
        }
    }

    Given("Arguments with all props set") {
        val arguments = Arguments(
            "~skjema~",
            "~aargang~",
            "~kvartal~",
            "~region~",
            "~navn~",
            "~orgnr~",
            "~foretaknr~",
            false,
            false,
            listOf("line #1", "line #2")
        )

        forAll(
            row("contentAsString = false expect empty string", false, ""),
            row("contentAsString = true expect non-empty string", true, "line #1\nline #2")
        ) { description, contentAsString, expectedContent ->

            When("toErrorReportEntry $description") {
                val kotlinArguments = fromArguments(arguments, contentAsString)

                Then("kotlinArguments should be as expected") {
                    assertSoftly(kotlinArguments) {
                        skjema shouldBe arguments.skjema
                        aargang shouldBe arguments.aargang
                        kvartal shouldBe arguments.kvartal
                        region shouldBe arguments.region
                        navn shouldBe arguments.navn
                        orgnr shouldBe arguments.orgnr
                        foretaknr shouldBe arguments.foretaknr
                        harVedlegg shouldBe arguments.harVedlegg()
                        isRunAsExternalProcess shouldBe arguments.isRunAsExternalProcess
                        inputFileStream shouldBe arguments.inputContentAsInputStream
                        inputFileContent shouldBe expectedContent
                    }
                }
            }
        }
    }
})
