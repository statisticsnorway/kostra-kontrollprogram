package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected

@Introspected
data class ReportRequestVm(
    val aar: Int,
    val skjema: String,
    val region: String,
    val organisasjon: String?
)
