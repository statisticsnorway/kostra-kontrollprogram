package gradletask

import gradletask.extensions.toChangeLogMarkdown
import gradletask.extensions.toFileDescriptionTemplate
import gradletask.extensions.toMarkdown
import gradletask.program.FileDescription
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.zipWithNext

abstract class GenerateResourcesAndDocsTask : DefaultTask() {

    @get:InputDirectory
    abstract val inputBaseDir: DirectoryProperty

    @get:OutputDirectory
    abstract val specsOutputDir: DirectoryProperty

    @get:OutputDirectory
    abstract val resourcesOutputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        // call your existing generation logic here
        println("Generating markdown from: ${inputBaseDir.get().asFile}")
        println("Writing specs and docs to: ${specsOutputDir.get().asFile}")
        println("Writing resources to: ${resourcesOutputDir.get().asFile}")

        createFileDescriptions()
        createFamvernMapping()
    }

    fun createFileDescriptions() {
        val inputDir = inputBaseDir.dir("file_description_templates").get().asFile
        val specsOutputDir = specsOutputDir.get().asFile
        val resourcesOutputDir = resourcesOutputDir.get().asFile

        specsOutputDir.mkdirs()
        resourcesOutputDir.mkdirs()

        val mapper = createYAMLMapper()

        val forms = inputDir
            .listFiles()
            ?.filter { file ->
                file.name.startsWith("file_description_")
                        && file.extension == "yaml"
            }
            ?.mapNotNull { file ->
                file
                    .nameWithoutExtension
                    .substringAfter("file_description_")
                    .let { formIdAndYear ->
                        if (formIdAndYear.contains("_")) {
                            formIdAndYear.substringBefore("_") to file
                        } else {
                            formIdAndYear to file
                        }
                    }
            }
            ?.groupBy(
                keySelector = { it.first },      // group by formId
                valueTransform = { it.second }   // keep (filename)
            )
            ?.mapValues { (_, list) ->
                list.sorted()       // sort by filename / year ascending
            }

        // write YAML and markdown versions of the file descriptions to their respective directories
        forms?.entries
            ?.forEach { (formId, list) ->
                println("\uD83D\uDE80 Processing file descriptions for $formId")
                list.forEach { yamlFile ->
                    try {
                        val yamlString = yamlFile.readText()
                        val fileDescriptionTemplate: FileDescriptionTemplate =
                            mapper.readValue(
                                yamlString,
                                FileDescriptionTemplate::class.java
                            )
                        val fileName = yamlFile.nameWithoutExtension
                        val fileDescription =
                            fileDescriptionTemplate.toFileDescription()
                        val markdown = fileDescription.toMarkdown()

                        val resourcesYamlOutputFile =
                            File(resourcesOutputDir, "${fileName}.yaml")
                        mapper.writeValue(resourcesYamlOutputFile, fileDescription)
                        println("✅ Wrote: $resourcesOutputDir/${fileName}.yaml")

                        val specsYamlOutputFile =
                            File(specsOutputDir, "${fileName}.yaml")
                        mapper.writeValue(specsYamlOutputFile, fileDescription)
                        println("✅ Wrote: $specsOutputDir/${fileName}.yaml")

                        val outputFile = File(specsOutputDir, "${fileName}.md")
                        outputFile.writeText(markdown)
                        println("✅ Wrote: $specsOutputDir/${fileName}.md")
                    } catch (e: Exception) {
                        println("❌ Failed to process ${yamlFile.absolutePath}: ${e.message}")
                    }
                }
            }

        // write markdown changelogs between versions of the file descriptions
        forms?.entries
            ?.filter { (_, list) -> list.size > 1 }
            ?.forEach { (formId, list) ->
                println("\uD83D\uDE80 Processing changelogs for $formId")
                list
                    .map { yamlFile ->
                        try {
                            yamlFile
                                .readText()
                                .toFileDescriptionTemplate()
                                .toFileDescription()
                        } catch (e: Exception) {
                            println("❌ Failed to process ${yamlFile.absolutePath}: ${e.message}")
                        }

                    }
                    .zipWithNext()
                    .forEach { (a, b ) ->
                        val fileDescriptionA = a as FileDescription
                        val fileDescriptionB = b as FileDescription
                        val markdown = (fileDescriptionA to fileDescriptionB).toChangeLogMarkdown()

                        val fileName = "changelog_for_${fileDescriptionA.id}_from_${fileDescriptionA.reportingYear}_to_${fileDescriptionB.reportingYear}.md"
                        val outputFile = File(specsOutputDir, fileName)
                        outputFile.writeText(markdown)
                        println("✅ Wrote: $specsOutputDir/${fileName}")

                    }
            }
    }

    fun createFamvernMapping() {
        val inputDir = inputBaseDir.dir("famvern_hierarchy").get().asFile
        val specsOutputDir = specsOutputDir.get().asFile
        val resourcesOutputDir = resourcesOutputDir.get().asFile

        specsOutputDir.mkdirs()
        resourcesOutputDir.mkdirs()

        val mapper = createYAMLMapper()

        inputDir
            .listFiles()
            ?.filter { file ->
                file.name.startsWith("mapping_familievern_region_fylke_kontor")
                        && file.extension in listOf("yaml", "yml")
            }
            ?.forEach { yamlFile ->
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
                            resourcesOutputDir,
                            yamlFile.name
                        )
                    mapper.writeValue(yamlOutputFile, famvernHierarchyMapping)
                    println("✅ Wrote: $resourcesOutputDir/${yamlFile.name}")

                    val markDownOutputFile =
                        File(
                            specsOutputDir,
                            yamlFile.nameWithoutExtension + ".md"
                        )
                    markDownOutputFile.writeText(markdown)
                    println("✅ Wrote: $specsOutputDir/${yamlFile.nameWithoutExtension}.md")
                } catch (e: Exception) {
                    println("❌ Failed to process ${yamlFile.absolutePath}: ${e.message}")
                }
            }
    }

}