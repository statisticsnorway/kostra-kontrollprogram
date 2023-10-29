package no.ssb.kostra.web.extensions

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportArguments
import no.ssb.kostra.web.viewmodel.CompanyIdVm
import no.ssb.kostra.web.viewmodel.FileReportEntryVm
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraFormVm

fun ValidationReportArguments.toErrorReportVm(): FileReportVm =
    this.validationResult.reportEntries.map {
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
    }.fold(mutableMapOf<String, FileReportEntryVm>()) { acc, fileReportEntry ->
        /** Group by ruleName + messageText and accumulate line numbers */
        acc.also { accumulator ->
            (fileReportEntry.ruleName + fileReportEntry.messageText).also { key ->
                accumulator[key]?.let { currentEntry ->
                    accumulator.replace(
                        key, currentEntry.copy(lineNumbers = currentEntry.lineNumbers + fileReportEntry.lineNumbers)
                    )
                } ?: accumulator.put(key, fileReportEntry)
            }
        }
    }.values.let { reducedValidationReportEntries ->
        FileReportVm(
            innparametere = with(this.kotlinArguments) {
                KostraFormVm(
                    aar = aargang.toInt(),
                    skjema = skjema,
                    region = region,
                    kvartal = kvartal,
                    orgnrForetak = foretaknr,
                    orgnrVirksomhet = orgnr.split(",").map { CompanyIdVm(orgnr = it) },
                    navn = navn
                )
            },
            antallKontroller = this.validationResult.numberOfControls,
            feil = reducedValidationReportEntries,
            severity = reducedValidationReportEntries.map { it.severity }.maxByOrNull { it.ordinal } ?: Severity.OK
        )
    }