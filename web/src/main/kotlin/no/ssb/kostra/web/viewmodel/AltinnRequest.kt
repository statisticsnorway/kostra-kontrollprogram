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
data class AltinnRequest(
    @field:Min(value = 2026, message = "Periode kan ikke være mindre enn {value}")
    val period: Int = 0,

    @field:Pattern(regexp = "^[1-4 ]$", message = "Kvartal er én av: 1, 2, 3, 4 eller et mellomrom for årlig rapportering. Default er mellomrom for årlig rapportering.")
    val quarter: String = " ",

    @field:ValidFormType
    val formId: String = "",

    @field:Pattern(regexp = "\\d{6}", message = "Region må bestå av 6 siffer uten mellomrom")
    val region: String = "",

    val name: String? = null,

    @field:Pattern(regexp = "[8|9]\\d{8}", message = "Må starte med 8 eller 9 etterfulgt av 8 siffer")
    val organizationId: String? = null,

    @field:NotBlank(message = "Filvedlegg er påkrevet")
    val base64EncodedFileAttachment: String = "",
)
