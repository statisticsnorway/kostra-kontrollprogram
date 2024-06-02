package no.ssb.kostra.web.extensions

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.web.viewmodel.CompanyIdVm
import no.ssb.kostra.web.viewmodel.FileReportEntryVm
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraFormVm


fun List<ValidationReportEntry>.groupReportEntries() = this
    .groupBy { it.copy(lineNumbers = emptyList()) }
    .map { (key, group) ->
        key.copy(lineNumbers = group.flatMap { it.lineNumbers }.distinct().sorted())
    }

fun ValidationReportArguments.toErrorReportVm(): FileReportVm =
    this.validationResult.reportEntries.groupReportEntries().map {
        FileReportEntryVm(
            severity = it.severity,
            caseworker = it.caseworker,
            journalId = it.journalId,
            individId = it.individId,
            contextId = it.contextId,
            ruleName = it.ruleName,
            messageText = it.messageText.replace("<br/>", ""),
            lineNumbers = it.lineNumbers
        )
    }.let { reportEntries ->
        FileReportVm(
            innparametere = with(this.kotlinArguments) {
                KostraFormVm(
                    aar = aargang.toInt(),
                    skjema = skjema,
                    region = region,
                    orgnrForetak = foretaknr,
                    orgnrVirksomhet = orgnr.split(",").map { CompanyIdVm(orgnr = it) },
                    navn = navn
                )
            },
            antallKontroller = this.validationResult.numberOfControls,
            feil = reportEntries,
            severity = reportEntries.map { it.severity }.maxByOrNull { it.ordinal } ?: Severity.OK
        )
    }