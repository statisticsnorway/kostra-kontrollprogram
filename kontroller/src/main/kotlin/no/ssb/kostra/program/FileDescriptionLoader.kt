package no.ssb.kostra.program

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object FileDescriptionLoader {
    private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    fun getResourceAsFieldDefinitionList(fileName: String): List<FieldDefinition> {
        val fileDescription: FileDescription? = this::class.java.classLoader.getResource(fileName)
            ?.let { mapper.readValue(it.readText()) }

        return when(fileDescription) {
            null -> emptyList()
            else -> fileDescription.fields
        }
    }
}