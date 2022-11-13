package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected
import no.ssb.kostra.web.validation.GyldigSkjema
import no.ssb.kostra.web.validation.GyldigSkjemaType
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Introspected
@GyldigSkjema
data class KostraFormVm(
    @field:Min(value = 2021, message = "År kan ikke være mindre enn {value}")
    val aar: Int = 0,

    @field:GyldigSkjemaType
    @field:NotBlank(message = "Skjematype må være utfylt")
    val skjema: String = "",

    @field:Pattern(regexp = "\\d{6}", message = "Region må bestå av 6 siffer uten mellomrom")
    val region: String = "",

    val navn: String? = null,

    @field:Pattern(regexp = "[8|9]\\d{8}", message = "Må starte med [8|9] etterfulgt av 8 siffer")
    val orgnrForetak: String? = null,

    /** When Micronaut 4, add validation to each entry like orgnrForetak */
    val orgnrVirksomhet: Collection<String>? = null,

    @field:NotBlank(message = "Filvedlegg mangler")
    val base64EncodedContent: String? = null
)