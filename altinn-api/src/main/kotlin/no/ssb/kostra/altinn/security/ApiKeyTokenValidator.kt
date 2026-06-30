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
        if (request == null || !request.path.startsWith("/api")) {
            logger.info("validateToken invalid request (${request.toString()}), returning unauthorized response")
            return Publishers.empty();
        }

        if (apiKeyConfiguration.isSecurityDisabled()){
            logger.info("apiKeyConfiguration.isSecurityDisabled == true, skipping API key validation")
            return Publishers.just(Authentication.build("anonymous"))
        }

        if (apiKeyConfiguration.isValid(token)) {
            logger.info("apiKeyConfiguration.isValid(token) == true, proceeding with request")
            return Publishers.just(Authentication.build(token))
        } else {
            logger.info("apiKeyConfiguration.isValid($token) == false, returning unauthorized response")
            return Publishers.empty()
        }
    }
}
