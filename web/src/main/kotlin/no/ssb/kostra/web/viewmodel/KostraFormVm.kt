package no.ssb.kostra.web.viewmodel

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import no.ssb.kostra.web.validation.ValidForm
import no.ssb.kostra.web.validation.ValidFormType

@Serdeable
@JsonInclude(JsonInclude.Include.ALWAYS)
@ValidForm
data class KostraFormVm(
    @field:Min(value = 2022, message = "År kan ikke være mindre enn {value}")
    val aar: Int = 0,

    @field:ValidFormType
    val skjema: String = "",

    @field:Pattern(regexp = "\\d{6}", message = "Region må bestå av 6 siffer uten mellomrom")
    val region: String = "",

    val navn: String? = null,

    @field:Pattern(regexp = "[8|9]\\d{8}", message = "Må starte med 8 eller 9 etterfulgt av 8 siffer")
    val orgnrForetak: String? = null,

    @field:NotBlank(message = "Filvedlegg er påkrevet")
    val filnavn: String = ""
)