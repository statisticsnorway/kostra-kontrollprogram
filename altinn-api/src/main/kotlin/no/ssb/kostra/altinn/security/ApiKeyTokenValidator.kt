package no.ssb.kostra.altinn.security

import io.micronaut.core.async.publisher.Publishers
import io.micronaut.http.HttpRequest
import io.micronaut.security.token.validator.TokenValidator
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory

@Singleton
class ApiKeyTokenValidator(
    private val apiKeyConfiguration: ApiKeyConfiguration
) : TokenValidator<HttpRequest<*>> {
    private val logger =
        LoggerFactory.getLogger(ApiKeyTokenValidator::class.java)

    override fun validateToken(token: String, request: HttpRequest<*>?): Publisher<Authentication> {
        if (request == null) {
            logger.info("empty request, returning unauthorized response")
            return Publishers.empty();
        }

        if (!request.path.startsWith("/api")) {
            logger.info("invalid path, returning unauthorized response")
            return Publishers.empty();
        }

        if (!apiKeyConfiguration.isValid(token)) {
            logger.info("invalid token, returning unauthorized response")
            return Publishers.empty()
        }

        logger.info("valid token, proceeding with request")
        return Publishers.just(Authentication.build(token))

    }
}
