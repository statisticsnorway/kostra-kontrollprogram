package no.ssb.kostra.area.famvern

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class FamilievernConstantsTest :
    BehaviorSpec({
        Given("a FamvernHierarchyMapping") {
            val sut = FamilievernConstants.FamvernHierarchyMapping(
                title = "~title~",
                description = "~description~",
                year = 2025,
                mappings = listOf(FamilievernConstants.FamvernHierarchyKontorFylkeRegionMapping(
                    region = "123456",
                    regionName = "~region name~",
                    fylke = "7890",
                    fylkeName = "~county name~",
                    kontor = "012",
                    kontorName = "~office name~"
                ))
            )

            Then("generated markdown should be as expected"){
                val expectedMarkdown = """# ~title~ (2025)

~description~

## Koblinger

| Regionnummer | Regionnavn | Fylkenummer | Fylkenavn | Kontornummer | Kontornavn |
|--------------|------------|-------------|-----------|--------------|------------|
| `123456` | ~region name~ | `7890` | ~county name~ | `012` | ~office name~ |
"""
                sut.toMarkdown() shouldBe expectedMarkdown
            }
        }
    })
