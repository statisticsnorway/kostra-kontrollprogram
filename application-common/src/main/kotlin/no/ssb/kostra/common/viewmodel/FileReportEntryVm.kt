package no.ssb.kostra.common.viewmodel

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.core.annotation.Introspected

@Introspected
@JsonInclude(JsonInclude.Include.ALWAYS)
data class FileReportEntryVm(
    val journalnummer: String,
    val saksbehandler: String,
    val kontrollnummer: String,
    val kontrolltekst: String,
    val feilkode: KostraErrorCode
)
