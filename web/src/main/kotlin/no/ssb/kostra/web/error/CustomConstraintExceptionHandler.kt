package no.ssb.kostra.web.error

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import io.micronaut.validation.exceptions.ConstraintExceptionHandler
import io.micronaut.validation.validator.ValidatorConfiguration
import jakarta.inject.Singleton
import jakarta.validation.ConstraintViolationException
import java.time.Instant

/**
 * Custom exception handler for validation exceptions.
 *
 * @author roar.skinderviken@ssb.no
 * @since 1.0
 */
@Produces
@Singleton
@Replaces(ConstraintExceptionHandler::class)
@Requires(classes = [ConstraintViolationException::class, ConstraintExceptionHandler::class])
class CustomConstraintExceptionHandler(
    responseProcessor: ErrorResponseProcessor<*>,
    validatorConfiguration: ValidatorConfiguration
) : ConstraintExceptionHandler(responseProcessor, validatorConfiguration) {

    override fun handle(request: HttpRequest<*>, exception: ConstraintViolationException): HttpResponse<*> =
        HttpResponse.badRequest(
            ApiError(
                timestamp = Instant.now().toEpochMilli(),
                httpStatusCode = HttpStatus.BAD_REQUEST.code,
                errorType = ApiErrorType.VALIDATION_ERROR,
                url = request.path,
                validationErrors = exception.constraintViolations
                    .associate { (it.propertyPath.last().name ?: FALLBACK_PROPERTY_PATH) to it.message }
            ))

    companion object {
        const val FALLBACK_PROPERTY_PATH = "ROOT"
    }
}
