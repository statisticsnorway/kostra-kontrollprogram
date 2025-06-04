package no.ssb.kostra.program

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.NoSuchFileException

object FileLoader {
    val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    fun getResourceAsFieldDefinitionList(fileName: String): List<FieldDefinition> =
        getResource<FileDescription>(fileName).fields

    inline fun <reified T> getResource(fileName: String): T =
        this::class.java.classLoader
            .getResourceAsStream(fileName)
            ?.let { inputStream -> InputStreamReader(inputStream) }
            ?.let { inputStreamReader -> BufferedReader(inputStreamReader) }
            ?.let { bufferedReader -> mapper.readValue(bufferedReader.readText()) as T? }
            ?: throw NoSuchFileException("File not found")
}
