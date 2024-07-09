package no.ssb.kostra.program

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.DataType.*


class FieldDefinitionTest : BehaviorSpec({

    Given("FieldDefinition with default values") {
        val sut = FieldDefinition(name = "~name~")

        When("FieldDefinition is created") {
            Then("FieldDefinition should be as expected") {
                assertSoftly(sut) {
                    name shouldBe "~name~"
                    description.shouldBeNull()
                    dataType shouldBe INTEGER_TYPE
                    from shouldBe 0
                    codeList shouldBe emptyList()
                    datePattern shouldBe ""
                    mandatory shouldBe false
                    size shouldBe 1
                    to shouldBe 0
                }
            }
        }
    }

    Given("FieldDefinition with all values set") {
        val sut = FieldDefinition(
            name = "~name~",
            dataType = DATE_TYPE,
            from = 2,
            codeList = listOf(Code("1", "one")),
            datePattern = DATE6_PATTERN,
            mandatory = true,
            size = 3
        )

        When("FieldDefinition is created") {
            Then("FieldDefinition should be as expected") {
                assertSoftly(sut) {
                    name shouldBe "~name~"
                    dataType shouldBe DATE_TYPE
                    from shouldBe 2
                    codeList shouldBe listOf(Code("1", "one"))
                    datePattern shouldBe DATE6_PATTERN
                    mandatory shouldBe true
                    size shouldBe 3
                    to shouldBe 4
                }
            }
        }
    }

    Given("FieldDefinition dataType = DATE_TYPE with empty or blank datePattern") {
        forAll(
            row("empty datePattern", ""),
            row("blank datePattern", " ".repeat(3))
        ) { description, datePattern ->
            When(description) {
                val thrown = shouldThrow<IllegalArgumentException> {
                    FieldDefinition(
                        name = "~name~",
                        dataType = DATE_TYPE,
                        datePattern = datePattern
                    )
                }

                Then("exception should be as expected") {
                    thrown.message shouldBe "datePattern cannot be empty or blank when dataType is $DATE_TYPE"
                }
            }
        }
    }
})