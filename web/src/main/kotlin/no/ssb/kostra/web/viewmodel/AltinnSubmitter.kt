package no.ssb.kostra.web.viewmodel

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AltinnSubmitter(
    val period: String,
    val quarter: String,
    val formId: String,
    val region: String,
    val name: String,
    val organizationId: String,
)
