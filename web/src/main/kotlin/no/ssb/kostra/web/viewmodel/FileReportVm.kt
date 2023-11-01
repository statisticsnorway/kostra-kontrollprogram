package no.ssb.kostra.web.viewmodel

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.core.annotation.Introspected
import no.ssb.kostra.validation.report.Severity

@Introspected
@JsonInclude(JsonInclude.Include.ALWAYS)
data class FileReportVm(
    val innparametere: KostraFormVm,
    val antallKontroller: Int,
    val severity: Severity,
    val feil: Collection<FileReportEntryVm>
)
