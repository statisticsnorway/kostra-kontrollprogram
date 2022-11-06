package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Pattern

@Introspected
data class CompanyId(
    @field:Pattern(regexp = "\\d{9}", message = "Orgnr må bestå av 9 siffer uten mellomrom")
    val orgnr: String = ""
)
