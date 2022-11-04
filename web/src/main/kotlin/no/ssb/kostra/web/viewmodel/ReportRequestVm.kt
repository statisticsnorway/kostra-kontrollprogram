package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@Introspected
data class ReportRequestVm(
    @field:Min(value = 2021) val aar: Int,
    @field:NotBlank val skjema: String,
    @field:Pattern(regexp = "\\d{6}") val region: String,
    val organisasjon: String?,
    @field:NotNull val base64EncodedContent: String?
)