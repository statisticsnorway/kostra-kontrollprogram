package no.ssb.kostra.web.error

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.core.annotation.Introspected
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Generic class for returning errors to client.
 *
 * @author roar.skinderviken@ssb.no
 * @since 1.0
 */
@Introspected
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class ApiError(
    @Schema(description = "System timestamp in milliseconds since 1970")
    val timestamp: Long,

    @Schema(description = "Http status code")
    val httpStatusCode: Int,

    @Schema(description = "Error type")
    val errorType: ApiErrorType,

    @Schema(description = "System URL for error")
    val url: String,

    @Schema(description = "Map of validation errors, if any")
    val validationErrors: Map<String, String>? = null
)
