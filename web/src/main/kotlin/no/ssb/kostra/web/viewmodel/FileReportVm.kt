package no.ssb.kostra.web.viewmodel

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.core.annotation.Introspected

@Introspected
@JsonInclude(JsonInclude.Include.ALWAYS)
data class FileReportVm(
    val innparametere: KostraFormVm,
    val antallKontroller: Int,
    val feilkode: KostraErrorCode,
    val feil: Collection<FileReportEntryVm>
)
