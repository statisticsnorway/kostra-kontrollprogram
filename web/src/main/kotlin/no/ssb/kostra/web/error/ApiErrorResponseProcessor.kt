package no.ssb.kostra.web.error

import io.micronaut.http.HttpResponseFactory
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import java.time.Instant

//@Produces
//@Singleton
class ApiErrorResponseProcessor : ErrorResponseProcessor<ApiError> {

    override fun processResponse(
        errorContext: ErrorContext,
        baseResponse: MutableHttpResponse<*>
    ): MutableHttpResponse<ApiError> =
        HttpResponseFactory.INSTANCE.status<ApiError>(baseResponse.status).body(
            ApiError(
                timestamp = Instant.now().toEpochMilli(),
                httpStatusCode = baseResponse.code(),
                errorType = ApiErrorType.SYSTEM_ERROR,
                url = errorContext.request.path
            )
        )
}
