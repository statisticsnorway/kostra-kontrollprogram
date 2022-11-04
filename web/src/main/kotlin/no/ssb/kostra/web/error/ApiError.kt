package no.ssb.kostra.web.error

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.core.annotation.Introspected

/**
 * Generic class for returning errors to client.
 *
 * @author roar.skinderviken@ssb.no
 * @since 1.0
 */
@Introspected
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class ApiError(
    val timestamp: Long,
    val httpStatusCode: Int,
    val errorType: ApiErrorType,
    val url: String,
    val validationErrors: Map<String, String>? = null
)
