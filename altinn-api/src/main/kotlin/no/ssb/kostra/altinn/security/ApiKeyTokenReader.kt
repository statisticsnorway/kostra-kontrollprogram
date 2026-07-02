package no.ssb.kostra.altinn.security

import io.micronaut.security.token.reader.HttpHeaderTokenReader
import jakarta.inject.Singleton


@Singleton
class ApiKeyTokenReader : HttpHeaderTokenReader() {
    override fun getPrefix(): String = ""

    // Define the custom header name for the API Key
    override fun getHeaderName(): String = API_KEY_HEADER

    companion object {
        const val API_KEY_HEADER = "X-API-KEY"
    }
}
