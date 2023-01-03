package no.ssb.kostra.web.extension

import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.Constants.CRITICAL_ERROR
import no.ssb.kostra.felles.Constants.NORMAL_ERROR
import no.ssb.kostra.felles.Constants.PARAMETER_ERROR
import no.ssb.kostra.felles.Constants.SYSTEM_ERROR
import no.ssb.kostra.felles.ErrorReport
import no.ssb.kostra.web.viewmodel.CompanyIdVm
import no.ssb.kostra.web.viewmodel.FileReportEntryVm
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraErrorCode
import no.ssb.kostra.web.viewmodel.KostraFormVm

fun Arguments.toReportRequestVm(): KostraFormVm {
    return KostraFormVm(
        aar = this.aargang.toInt(),
        skjema = this.skjema,
        region = this.region,
        navn = this.navn,
        orgnrForetak = this.foretaknr.ifBlank { null },
        orgnrVirksomhet =
        if (this.orgnr.isNullOrEmpty()) setOf()
        else this.orgnr
            .split(",")
            .filterNot { it.isBlank() }
            .map { CompanyIdVm(it) }
    )
}

fun Int.toKostraErrorCode(): KostraErrorCode = when (this) {
    NORMAL_ERROR -> KostraErrorCode.NORMAL_ERROR
    CRITICAL_ERROR -> KostraErrorCode.CRITICAL_ERROR
    SYSTEM_ERROR -> KostraErrorCode.SYSTEM_ERROR
    PARAMETER_ERROR -> KostraErrorCode.PARAMETER_ERROR
    else -> KostraErrorCode.NO_ERROR
}

fun ErrorReport.toErrorReportVm(): FileReportVm {

    val errorReportEntries = this.rapportMap.entries.flatMap { (saksbehandler, errorDetails) ->
        errorDetails.entries.flatMap { (journalnummer, journalDetails) ->
            journalDetails.map { (_, errorList) ->
                FileReportEntryVm(
                    journalnummer = journalnummer,
                    saksbehandler = saksbehandler,
                    kontrollnummer = errorList[0],
                    kontrolltekst = errorList[1].replace("<br/>", ""),
                    feilkode = errorList[2].toInt().toKostraErrorCode()
                )
            }
        }
    }

    return FileReportVm(
        innparametere = this.args.toReportRequestVm(),
        antallKontroller = this.count.toInt(),
        feil = errorReportEntries,
        feilkode = errorReportEntries.map { it.feilkode }
            .maxByOrNull { it.ordinal } ?: KostraErrorCode.NO_ERROR
    )
}