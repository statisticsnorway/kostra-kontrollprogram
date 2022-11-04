package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected

@Introspected
data class ErrorReportVm(
    val innparametere: ReportRequestVm,
    val antallKontroller: Int,
    val feilkode: KostraErrorCode,
    val feil: Collection<ErrorDetailsVm>
)
