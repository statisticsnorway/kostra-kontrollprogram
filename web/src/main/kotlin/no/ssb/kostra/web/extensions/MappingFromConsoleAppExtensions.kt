package no.ssb.kostra.web.extensions

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.web.viewmodel.AltinnReport
import no.ssb.kostra.web.viewmodel.AltinnReportEntry
import no.ssb.kostra.web.viewmodel.AltinnSubmitter
import no.ssb.kostra.web.viewmodel.FileReportEntryVm
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraFormVm

fun List<ValidationReportEntry>.groupReportEntries() =
    this
        .groupBy { it.copy(lineNumbers = emptyList()) }
        .map { (key, group) ->
            key.copy(lineNumbers = group.flatMap { it.lineNumbers }.distinct().sorted())
        }

fun ValidationReportArguments.toErrorReportVm(): FileReportVm =
    this.validationResult.reportEntries
        .groupReportEntries()
        .map {
            FileReportEntryVm(
                severity = it.severity,
                caseworker = it.caseworker,
                journalId = it.journalId,
                individId = it.individId,
                contextId = it.contextId,
                ruleName = it.ruleName,
                messageText = it.messageText.replace("<br/>", ""),
                lineNumbers = it.lineNumbers,
            )
        }.let { reportEntries ->
            FileReportVm(
                innparametere =
                    with(this.kotlinArguments) {
                        KostraFormVm(
                            aar = aargang.toInt(),
                            skjema = skjema,
                            region = region,
                            orgnrForetak = orgnr,
                            navn = navn,
                        )
                    },
                antallKontroller = this.validationResult.numberOfControls,
                feil = reportEntries,
                severity = reportEntries.map { it.severity }.maxByOrNull { it.ordinal } ?: Severity.OK,
            )
        }

fun ValidationReportArguments.toAltinnReport(): AltinnReport =
    this.validationResult.reportEntries
        .groupReportEntries()
        .map {
            AltinnReportEntry(
                severity = it.severity,
                ruleName = it.ruleName,
                messageText = it.messageText.replace("<br/>", ""),
                lineNumbers = it.lineNumbers,
            )
        }.let { reportEntries ->
            AltinnReport(
                submitter =
                    with(this.kotlinArguments) {
                        AltinnSubmitter(
                            period = aargang,
                            quarter = kvartal,
                            formId = skjema,
                            region = region,
                            organizationId = orgnr,
                            name = navn,
                        )
                    },
                controlsRunCount = this.validationResult.numberOfControls,
                entries = reportEntries,
                severity = reportEntries.map { it.severity }.maxByOrNull { it.ordinal } ?: Severity.OK,
            )
        }
