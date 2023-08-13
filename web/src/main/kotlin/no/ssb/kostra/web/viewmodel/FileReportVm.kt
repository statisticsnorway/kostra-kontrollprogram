package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected

data class FileReportVm(
    val innparametere: KostraFormVm,
    val antallKontroller: Int,
    val feilkode: KostraErrorCode,
    val feil: Collection<FileReportEntryVm>
)
