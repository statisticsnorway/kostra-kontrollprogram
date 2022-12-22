package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected
import no.ssb.kostra.web.validation.ValidForm
import no.ssb.kostra.web.validation.ValidFormType
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Introspected
@ValidForm
data class KostraFormVm(
    @field:Min(value = 2022, message = "År kan ikke være mindre enn {value}")
    val aar: Int = 0,

    @field:ValidFormType
    @field:NotBlank(message = "Skjematype er påkrevet")
    val skjema: String = "",

    @field:NotBlank(message = "Region er påkrevet")
    @field:Pattern(regexp = "\\d{6}", message = "Region må bestå av 6 siffer uten mellomrom")
    val region: String = "",

    val navn: String? = null,

    @field:Pattern(regexp = "[8|9]\\d{8}", message = "Må starte med 8 eller 9 etterfulgt av 8 siffer")
    val orgnrForetak: String? = null,

    @field:Valid
    val orgnrVirksomhet: Collection<CompanyIdVm>? = null,

    @field:NotBlank(message = "Filvedlegg er påkrevet")
    val filnavn: String = ""
)