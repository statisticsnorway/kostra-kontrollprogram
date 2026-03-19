package no.ssb.kostra.web.viewmodel

data class AltinnSubmitter(
    val period: String,
    val quarter: String,
    val formId: String,
    val region: String,
    val name: String,
    val organizationId: String,
)
