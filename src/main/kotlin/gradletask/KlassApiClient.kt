package gradletask

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.inject.Singleton
import no.ssb.kostra.program.Code
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL


@Singleton
class KlassApiClient : KlassClient {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
    private val apiUrl =
        "https://data.ssb.no/api/klass/v1/classifications"

    private fun fetch(requestUrl: String, defaultResponse: String): String {
        val response = StringBuilder()

        try {
            val url: URL = URI.create(requestUrl).toURL()
            val connection: HttpURLConnection =
                (url.openConnection() as HttpURLConnection)
                    .apply { this.requestMethod = "GET" }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader =
                    BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                reader.close()
            } else {
                response.append(defaultResponse)
            }

            connection.disconnect()
            return response.toString()
        } catch (e: Exception) {
            response.append(defaultResponse)
        }
        return response.toString()
    }

    override fun fetchCodes(
        classificationId: String,
        year: String
    ): List<Code> {
        val requestUrl =
            "$apiUrl/${classificationId}/codesAt?date=${year}-12-31&language=nb"

        return objectMapper.readValue(
            fetch(requestUrl, "{codes:}"),
            KlassCodes::class.java
        ).toCodeList()
    }

    override fun fetchCorrespondence(
        sourceClassificationId: String,
        targetClassificationId: String,
        year: String
    ): List<Pair<Code, Code>> {
        val requestUrl =
            "$apiUrl/${sourceClassificationId}/correspondsAt?targetClassificationId=${targetClassificationId}&date=${year}-12-31&language=nb"

        return objectMapper.readValue(
            fetch(requestUrl, "{correspondenceItems:}"),
            KlassCorrespondence::class.java
        ).toCodePairList()
    }
}