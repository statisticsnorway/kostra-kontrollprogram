package no.ssb.kostra.program

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object FileDescriptionLoader {
    private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    fun getResourceAsFileDescription(fileName: String): FileDescription? =
        this::class.java.classLoader.getResource(fileName)
            ?.let { mapper.readValue(it.readText()) }
}