package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected

@Introspected
data class FileReportEntryVm(
    val journalnummer: String,
    val saksbehandler: String,
    val kontrollnummer: String,
    val kontrolltekst: String,
    val feilkode: KostraErrorCode
)
