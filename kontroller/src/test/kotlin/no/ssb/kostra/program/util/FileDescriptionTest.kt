package no.ssb.kostra.program.util

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FileDescription

class FileDescriptionTest : BehaviorSpec({
    Given("paths to file descriptions and expected counts") {
        forAll(
            row("using default values", FileDescription(), "", 0, "", emptyList<FieldDefinition>()),
            row(
                "all values are set", FileDescription("title", 2023, "description", emptyList()),
                "title",
                2023,
                "description",
                emptyList()
            ),
        ) { description, fileDescription, expectedTitle, expectedReportingYear, expectedDescription, expectedFieldsList ->
            When(description) {
                Then("File description title should be as expected") {
                    fileDescription.title shouldBe expectedTitle
                }

                Then("File description reporting year should be as expected") {
                    fileDescription.reportingYear shouldBe expectedReportingYear
                }

                Then("File description description should be as expected") {
                    fileDescription.description shouldBe expectedDescription
                }

                Then("File description fields should be as expected") {
                    fileDescription.fields shouldBe expectedFieldsList
                }
            }
        }
    }
})