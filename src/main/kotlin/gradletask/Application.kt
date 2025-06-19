package gradletask

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import no.ssb.kostra.program.extension.toMarkdown
import java.io.File

fun main() {
    createFileDescriptions()
    createFamvernMapping()
}

fun createFileDescriptions() {
    val inputDir =
        File("src/main/resources/file_description_templates")
    val markdownOutputDir = File("kravspesifikasjon")
    markdownOutputDir.mkdirs()
    val fileDescriptionOutputDir = File("kontroller/src/main/resources")
    fileDescriptionOutputDir.mkdirs()
    val mapper = createYAMLMapper()

    inputDir
        .listFiles()
        ?.filter { file ->
            file.name.startsWith("file")
                    && file.extension in listOf("yaml", "yml")
        }
        ?.forEach { yamlFile ->
            try {
                val yamlString = yamlFile.readText()
                val fileDescriptionTemplate: FileDescriptionTemplate =
                    mapper.readValue(
                        yamlString,
                        FileDescriptionTemplate::class.java
                    )
                val fileName =
                    "${yamlFile.nameWithoutExtension}${if (fileDescriptionTemplate.reportingYear == 0) "" else "_" + fileDescriptionTemplate.reportingYear}"
                val fileDescription =
                    fileDescriptionTemplate.toFileDescription()
                val markdown = fileDescription.toMarkdown()

                val yamlOutputFile =
                    File(fileDescriptionOutputDir, "${fileName}.yaml")
                mapper.writeValue(yamlOutputFile, fileDescription)
                println("✅ Wrote: $fileDescriptionOutputDir/${fileName}.yaml")

                val outputFile = File(markdownOutputDir, "${fileName}.md")
                outputFile.writeText(markdown)
                println("✅ Wrote: $markdownOutputDir/${fileName}.md")
            } catch (e: Exception) {
                println("❌ Failed to process ${yamlFile.absolutePath}: ${e.message}")
            }
        }
}

fun createFamvernMapping() {
    val markdownOutputDir = File("kravspesifikasjon")
    markdownOutputDir.mkdirs()
    val yamlOutputDir = File("kontroller/src/main/resources")
    yamlOutputDir.mkdirs()
    val mapper = createYAMLMapper()

    File("src/main/resources/famvern_hierarchy/mapping_familievern_region_fylke_kontor.yaml")
        .let { yamlFile ->
            try {
                val famvernMappingTemplate: FamvernMappingTemplate =
                    mapper.readValue(
                        yamlFile,
                        FamvernMappingTemplate::class.java
                    )
                val famvernHierarchyMapping =
                    famvernMappingTemplate.toFamvernHierarchyMapping()
                val markdown = famvernHierarchyMapping.toMarkdown()

                val yamlOutputFile =
                    File(
                        yamlOutputDir,
                        yamlFile.nameWithoutExtension + "_${famvernMappingTemplate.reportingYear}.yaml"
                    )
                mapper.writeValue(yamlOutputFile, famvernHierarchyMapping)
                println("✅ Wrote: $yamlOutputDir/${yamlFile.nameWithoutExtension}_${famvernMappingTemplate.reportingYear}.yaml")

                val markDownOutputFile =
                    File(
                        markdownOutputDir,
                        yamlFile.nameWithoutExtension + "_${famvernMappingTemplate.reportingYear}.md"
                    )
                markDownOutputFile.writeText(markdown)
                println("✅ Wrote: $markdownOutputDir/${yamlFile.nameWithoutExtension}_${famvernMappingTemplate.reportingYear}.md")
            } catch (e: Exception) {
                println("❌ Failed to process ${yamlFile.absolutePath}: ${e.message}")
            }
        }
}

fun createYAMLMapper() =
    YAMLFactory()
        .apply {
            configure(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE, false)
            configure(YAMLGenerator.Feature.SPLIT_LINES, false)
        }
        .let { yamlFactory -> ObjectMapper(yamlFactory).registerKotlinModule() }