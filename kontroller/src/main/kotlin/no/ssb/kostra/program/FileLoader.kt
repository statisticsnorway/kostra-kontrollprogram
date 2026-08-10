package no.ssb.kostra.program

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import no.ssb.kostra.program.extension.buildFieldDefinitions
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.NoSuchFileException

object FileLoader {
    val mapper: ObjectMapper =
        ObjectMapper(
            YAMLFactory()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .disable(YAMLGenerator.Feature.SPLIT_LINES)
        )
            .registerKotlinModule()                 // 👈 REQUIRED for Kotlin data classes
            .registerModule(JavaTimeModule())      // 👈 REQUIRED for LocalDate / LocalDateTime
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getResourceAsFieldDefinitionList(fileName: String): List<FieldDefinition> =
        getResource<FileDescription>(fileName).fields.buildFieldDefinitions()

    inline fun <reified T> getResource(fileName: String): T =
        this::class.java.classLoader.getResourceAsStream(fileName)
            ?.let { inputStream -> InputStreamReader(inputStream) }
            ?.let { inputStreamReader -> BufferedReader(inputStreamReader) }
            ?.let { bufferedReader -> mapper.readValue(bufferedReader.readText()) as T? }
            ?: throw NoSuchFileException("File not found")
}