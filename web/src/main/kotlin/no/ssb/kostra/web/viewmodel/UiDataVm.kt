package no.ssb.kostra.web.viewmodel

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class UiDataVm(
    val releaseVersion: String,
    val years: Collection<Int>,
    val formTypes: Collection<KostraFormTypeVm>
)
