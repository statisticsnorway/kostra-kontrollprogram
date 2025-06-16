package no.ssb.kostra.program.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FileDescription

class FileDescriptionExtensionKtTest : BehaviorSpec({

    Given("a FileDescription") {
        val sut = FileDescription(
            title = "~title~",
            reportingYear = 2025,
            description = "~description~",
            fields = listOf(
                FieldDefinition(
                    name = "~name~",
                    description = "~description~",
                    from = 1,
                    size = 1
                )
            )

        )

        Then("generated markdown should be as expected") {
            val expectedMarkdown = """# Filbeskrivelse: ~title~ (2025)

~description~

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra-Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `~name~` | ~description~ | 1 | 1-1 | INTEGER_TYPE |  |  |  |
"""
            sut.toMarkdown() shouldBe expectedMarkdown
        }
    }
})