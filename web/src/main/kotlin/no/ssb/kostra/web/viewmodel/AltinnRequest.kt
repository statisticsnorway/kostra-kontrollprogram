package no.ssb.kostra.web.viewmodel


import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import no.ssb.kostra.web.validation.ValidForm
import no.ssb.kostra.web.validation.ValidFormType

@Serdeable
@JsonInclude(JsonInclude.Include.ALWAYS)
@ValidForm
data class AltinnRequest(
    @field:ValidFormType
    val respondent: AltinnRespondent,

    @field:NotBlank(message = "Filvedlegg er påkrevet")
    val base64KodedeData: String = "",
)
