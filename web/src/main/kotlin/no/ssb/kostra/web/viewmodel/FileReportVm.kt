package no.ssb.kostra.web.viewmodel

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.serde.annotation.Serdeable
import no.ssb.kostra.validation.report.Severity

@Serdeable
@JsonInclude(JsonInclude.Include.ALWAYS)
data class FileReportVm(
    val innparametere: KostraFormVm,
    val antallKontroller: Int,
    val severity: Severity,
    val feil: Collection<FileReportEntryVm>
)
