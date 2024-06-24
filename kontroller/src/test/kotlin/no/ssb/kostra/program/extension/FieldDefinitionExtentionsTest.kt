package no.ssb.kostra.program.extension

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.FieldDescription

class FieldDefinitionExtentionsTest : StringSpec({

    "should convert FieldDescription to FieldDefinition with correct 'from' value" {
        val fieldDescription = FieldDescription(name = "TestField", size = 3)
        val fieldDefinition = fieldDescription.toFieldDefinition(from = 5)

        fieldDefinition.name shouldBe "TestField"
        fieldDefinition.description shouldBe fieldDescription.description
        fieldDefinition.dataType shouldBe fieldDescription.dataType
        fieldDefinition.from shouldBe 5
        fieldDefinition.codeList shouldBe fieldDescription.codeList
        fieldDefinition.datePattern shouldBe fieldDescription.datePattern
        fieldDefinition.mandatory shouldBe fieldDescription.mandatory
        fieldDefinition.size shouldBe fieldDescription.size
    }

    "should build a list of FieldDefinitions from a list of FieldDescriptions" {
        val fieldDescriptions = listOf(
            FieldDescription(name = "Field1", size = 3),
            FieldDescription(name = "Field2", size = 2),
            FieldDescription(name = "Field3", size = 4)
        )

        val fieldDefinitions = fieldDescriptions.buildFieldDefinitions()

        fieldDefinitions shouldHaveSize 3
        fieldDefinitions[0].from shouldBe 1
        fieldDefinitions[1].from shouldBe 4
        fieldDefinitions[2].from shouldBe 6

        fieldDefinitions[0].size shouldBe 3
        fieldDefinitions[1].size shouldBe 2
        fieldDefinitions[2].size shouldBe 4
    }
})