package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected

@Introspected
data class UiDataVm(
    val releaseVersion: String,
    val years: Collection<Int>,
    val formTypes: Collection<KostraFormTypeVm>
)
