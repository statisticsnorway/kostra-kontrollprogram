package gradletask

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun createYAMLMapper() =
    YAMLFactory()
        .apply {
            configure(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE, false)
            configure(YAMLGenerator.Feature.SPLIT_LINES, false)
        }
        .let { yamlFactory -> ObjectMapper(yamlFactory).registerKotlinModule() }