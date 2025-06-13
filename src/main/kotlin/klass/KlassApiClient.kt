package klass

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import no.ssb.kostra.program.Code
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL


object KlassApiClient {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
    private const val API_URL =
        "https://data.ssb.no/api/klass/v1/classifications"

    private fun fetch(requestUrl: String, defaultResponse: String): String {
        val response = StringBuilder()

        try {
            val url: URL = URI.create(requestUrl).toURL()
            val connection: HttpURLConnection =
                (url.openConnection() as HttpURLConnection)
                    .apply { this.requestMethod = "GET" }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
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

    fun fetchCodes(
        classificationId: String,
        year: String
    ): List<Code> {
        val requestUrl =
            "$API_URL/${classificationId}/codesAt?date=${year}-12-31&language=nb"

        return objectMapper.readValue(
            fetch(requestUrl, "{codes:}"),
            KlassCodes::class.java
        ).toCodeList()
    }

    fun fetchCorrespondence(
        sourceClassificationId: String,
        targetClassificationId: String,
        year: String
    ): List<Pair<Code, Code>> {
        val requestUrl =
            "$API_URL/${sourceClassificationId}/correspondsAt?targetClassificationId=${targetClassificationId}&date=${year}-12-31&language=nb"

        return objectMapper.readValue(
            fetch(requestUrl, "{correspondenceItems:}"),
            KlassCorrespondence::class.java
        ).toCodePairList()
    }

    fun writeObjectAsYamlStringToFileAtPath(value: Any, path: String) {
        return YAMLFactory()
            .apply {
                configure(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE, false)
                configure(YAMLGenerator.Feature.SPLIT_LINES, false)
            }
            .let { yamlFactory -> ObjectMapper(yamlFactory).registerKotlinModule() }
            .writeValueAsString(value)
            .let { yamlString -> File(path).writeText(yamlString) }
    }

}