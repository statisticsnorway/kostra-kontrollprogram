package no.ssb.kostra.web.error

import io.micronaut.core.annotation.Introspected

/**
 * Enum for error types.
 *
 * @author roar.skinderviken@ssb.no
 * @since 1.0
 */
@Introspected
enum class ApiErrorType {

    /**
     * Validation error type.
     */
    VALIDATION_ERROR,

    /**
     * System error type.
     */
    SYSTEM_ERROR;
}