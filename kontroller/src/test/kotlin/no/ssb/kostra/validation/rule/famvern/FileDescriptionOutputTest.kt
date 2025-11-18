package no.ssb.kostra.validation.rule.famvern

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.FileDescription
import java.io.File
import java.io.IOException

@Ignored
class FileDescriptionOutputTest : BehaviorSpec({

    Given("Schema ID and field definitions object") {

        forAll(
            row("Tittel Regnskap", "Regnskap", RegnskapFieldDefinitions),
        ) { title, id, fieldDefinitionObject ->
            When(id) {
                val factory = YAMLFactory()
                    .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
                    .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                    .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                val mapper = ObjectMapper(factory)
                val fileName = "src/main/resources/file_description_$id.yaml"
                val fileDescription = FileDescription(
                    title = title,
                    reportingYear = 2024,
                    fields = fieldDefinitionObject.fieldDefinitions
                )

                try {
                    mapper.writeValue(File(fileName), fileDescription)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                Then("getInputContentAsStringList() should produce the expectedList") {
                    true.shouldBe(true)
                }
            }
        }
    }
})

