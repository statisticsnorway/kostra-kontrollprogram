package no.ssb.kostra.web.error

import io.micronaut.serde.annotation.Serdeable

/**
 * Enum for error types.
 *
 * @author roar.skinderviken@ssb.no
 * @since 1.0
 */
@Serdeable
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