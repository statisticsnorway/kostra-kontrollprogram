package no.ssb.kostra.program.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FileDescription

class FileDescriptionExtensionKtTest : BehaviorSpec({

    Given("a FileDescription") {
        forAll(
            row(
                "Description has line breaks",
                "line\nbreak",
                """# Filbeskrivelse: ~title~ (2025)

~description~

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `~name~` | line<br/>break | 1 | 1‑1 | INTEGER_TYPE |  |  |  |
"""
            ),
            row(
                "Description is 300 characters or longer",
                " ".repeat(300),
                """# Filbeskrivelse: ~title~ (2025)

~description~

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `~name~` | ${" ".repeat(300) + "..."} | 1 | 1‑1 | INTEGER_TYPE |  |  |  |
"""
            ),
            row(
                "Description is null", null, """# Filbeskrivelse: ~title~ (2025)

~description~

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `~name~` |  | 1 | 1‑1 | INTEGER_TYPE |  |  |  |
"""
            ),

            ) { description, descriptionContent, expectedMarkdown ->
            When(description) {
                val sut = FileDescription(
                    title = "~title~",
                    reportingYear = 2025,
                    description = "~description~",
                    fields = listOf(
                        FieldDefinition(
                            name = "~name~",
                            description = descriptionContent,
                            from = 1,
                            size = 1
                        )
                    )
                )

                Then("generated markdown should be as expected") {
                    sut.toMarkdown() shouldBe expectedMarkdown
                }
            }
        }

        forAll(
            row(
                "Has mandatory field",
                true,
                """# Filbeskrivelse: ~title~ (2025)

~description~

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `~name~` | ~description~ | 1 | 1‑1 | INTEGER_TYPE | ☑️ |  |  |
"""
            ),
            row(
                "Has not mandatory field",
                false,
                """# Filbeskrivelse: ~title~ (2025)

~description~

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `~name~` | ~description~ | 1 | 1‑1 | INTEGER_TYPE |  |  |  |
"""
            ),
        ) { description, isMandatory, expectedMarkdown ->
            When(description) {
                val sut = FileDescription(
                    title = "~title~",
                    reportingYear = 2025,
                    description = "~description~",
                    fields = listOf(
                        FieldDefinition(
                            name = "~name~",
                            description = "~description~",
                            from = 1,
                            size = 1,
                            mandatory = isMandatory
                        )
                    )
                )

                Then("generated markdown should be as expected") {
                    sut.toMarkdown() shouldBe expectedMarkdown
                }
            }
        }
    }
})