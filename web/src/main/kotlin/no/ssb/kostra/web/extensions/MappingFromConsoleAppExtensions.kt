package no.ssb.kostra.web.extensions

import no.ssb.kostra.felles.Constants.*
import no.ssb.kostra.validation.report.ValidationReportArguments
import no.ssb.kostra.web.viewmodel.FileReportEntryVm
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraErrorCode
import no.ssb.kostra.web.viewmodel.KostraFormVm

fun Int.toKostraErrorCode(): KostraErrorCode = when (this) {
    NORMAL_ERROR -> KostraErrorCode.NORMAL_ERROR
    CRITICAL_ERROR -> KostraErrorCode.CRITICAL_ERROR
    SYSTEM_ERROR -> KostraErrorCode.SYSTEM_ERROR
    PARAMETER_ERROR -> KostraErrorCode.PARAMETER_ERROR
    else -> KostraErrorCode.NO_ERROR
}

fun ValidationReportArguments.toErrorReportVm(kostraForm: KostraFormVm): FileReportVm {
    val validationReportEntries = this.validationResult.reportEntries.map {
        FileReportEntryVm(
            journalnummer = it.journalId,
            saksbehandler = it.caseworker,
            kontrollnummer = it.ruleName,
            kontrolltekst = it.messageText.replace("<br/>", ""),
            feilkode = it.severity.info.returnCode.toKostraErrorCode()
        )
    }

    return FileReportVm(
        innparametere = kostraForm,
        antallKontroller = this.validationResult.numberOfControls,
        feil = validationReportEntries,
        feilkode = validationReportEntries.map { it.feilkode }
            .maxByOrNull { it.ordinal } ?: KostraErrorCode.NO_ERROR
    )
}