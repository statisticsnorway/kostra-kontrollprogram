package no.ssb.kostra.web.security

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@Filter("/**")
@Requires(property = "api-keys.allowed")
@Singleton
class ApiKeyFilter(
    private val apiKeyConfiguration: ApiKeyConfiguration
) : HttpServerFilter {

    override fun doFilter(
        request: HttpRequest<*>,
        chain: ServerFilterChain
    ): Publisher<MutableHttpResponse<*>> {
        if (apiKeyConfiguration.isSecurityDisabled) {
            return chain.proceed(request)
        }

        val providedKey = request.headers["X-API-Key"]

        return if (apiKeyConfiguration.isValid(providedKey)) {
            chain.proceed(request)
        } else {
            Mono.just(
                HttpResponse.unauthorized<Any>()
            )
        }
    }
}