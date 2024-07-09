package no.ssb.kostra.program

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import no.ssb.kostra.program.extension.buildFieldDefinitions
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.NoSuchFileException


object FileDescriptionLoader {
    private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    fun getResourceAsFieldDefinitionList(fileName: String): List<FieldDefinition> =
        getResourceAsFileDescription(fileName).fields.buildFieldDefinitions()


    fun getResourceAsFileDescription(fileName: String): FileDescription =
        this::class.java.classLoader.getResourceAsStream(fileName)
            ?.let { inputStream -> InputStreamReader(inputStream) }
            ?.let { inputStreamReader -> BufferedReader(inputStreamReader) }
            ?.let { bufferedReader -> mapper.readValue(bufferedReader.readText()) as FileDescription? }
            ?: throw NoSuchFileException("File description not found")

}
