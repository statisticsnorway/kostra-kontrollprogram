package no.ssb.kostra.program

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class FieldDescriptionTest : StringSpec({

    "test default values" {
        val fieldDescription = FieldDescription(name = "TestField")

        fieldDescription.name shouldBe "TestField"
        fieldDescription.description.shouldBeNull()
        fieldDescription.dataType shouldBe DataType.INTEGER_TYPE
        fieldDescription.codeList.shouldBeEmpty()
        fieldDescription.datePattern shouldBe ""
        fieldDescription.mandatory.shouldBeFalse()
        fieldDescription.size shouldBe 1
    }

    "test custom values" {
        val fieldDescription = FieldDescription(
            name = "CustomField",
            description = "This is a custom field",
            dataType = DataType.STRING_TYPE,
            codeList = listOf(Code("A", "Code A")),
            datePattern = "yyyy-MM-dd",
            mandatory = true,
            size = 5
        )

        fieldDescription.name shouldBe "CustomField"
        fieldDescription.description shouldBe "This is a custom field"
        fieldDescription.dataType shouldBe DataType.STRING_TYPE
        fieldDescription.codeList.size shouldBe 1
        fieldDescription.codeList[0].value shouldBe "Code A"
        fieldDescription.datePattern shouldBe "yyyy-MM-dd"
        fieldDescription.mandatory.shouldBeTrue()
        fieldDescription.size shouldBe 5
    }
})