package no.ssb.kostra.program

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.BufferedReader
import java.io.InputStreamReader


object FileDescriptionLoader {
    private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    fun getResourceAsFieldDefinitionList(fileName: String): List<FieldDefinition> =
        getResourceAsFileDescription(fileName).fields


    fun getResourceAsFileDescription(fileName: String): FileDescription =
        this::class.java.classLoader.getResourceAsStream(fileName)
            ?.let { inputStream -> InputStreamReader(inputStream) }
            ?.let { inputStreamReader -> BufferedReader(inputStreamReader) }
            ?.let { bufferedReader -> mapper.readValue(bufferedReader.readText()) as FileDescription? }
            ?: FileDescription()

}
