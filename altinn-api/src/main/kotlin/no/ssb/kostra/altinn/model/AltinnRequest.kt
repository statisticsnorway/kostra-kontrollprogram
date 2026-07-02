package no.ssb.kostra.altinn.model


import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
@JsonInclude(JsonInclude.Include.ALWAYS)
data class AltinnRequest(
    val respondent: AltinnRespondent,

    @field:NotBlank(message = "Filvedlegg er påkrevet")
    val base64KodedeData: String = "",
)
