package no.ssb.kostra.program

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe


class FieldDefinitionTest : BehaviorSpec({

    Given("FieldDefinition with default values") {
        val sut = FieldDefinition(name = "~name~")

        When("FieldDefinition is created") {
            Then("FieldDefinition should be as expected") {
                assertSoftly(sut) {
                    number shouldBe 0
                    name shouldBe "~name~"
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