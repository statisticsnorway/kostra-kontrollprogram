package no.ssb.kostra.validation.rule.regnskap.kostra.extensions

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KostraRecord

class KostraRecordRegnskapExtensionsTest : BehaviorSpec({
    Given("isBevilgningRegnskap()") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 2, name = RegnskapConstants.FIELD_SKJEMA)
        ).associateBy { it.name }

        forAll(
            row("0A", true),
            row("0C", true),
            row("0I", true),
            row("0K", true),
            row("0M", true),
            row("0P", true),
            row("0F", true),
            row("0X", false),
            row("0B", false),
            row("0D", false),
            row("0J", false),
            row("0L", false),
            row("0N", false),
            row("0Q", false),
            row("0G", false),
            row("0Y", false)
        ) { skjema, expectedResult ->
            When("For $skjema") {
                val kostraRecord = KostraRecord(
                    index = 0,
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema
                    )
                )

                Then("expected result should be equal to $expectedResult") {
                    kostraRecord.isBevilgningRegnskap().shouldBeEqual(expectedResult)
                }
            }
        }
    }

    Given("isBevilgningDriftRegnskap()") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 2, name = RegnskapConstants.FIELD_SKJEMA),
            FieldDefinition(from = 3, to = 3, name = RegnskapConstants.FIELD_KONTOKLASSE)
        ).associateBy { it.name }

        forAll(
            row("0A", "0", false),
            row("0A", "1", true),
            row("0C", "0", false),
            row("0C", "1", true),
            row("0I", "3", true),
            row("0I", "4", false),
            row("0K", "3", true),
            row("0K", "4", false),
            row("0M", "3", true),
            row("0M", "4", false),
            row("0P", "3", true),
            row("0P", "4", false),
            row("0F", "3", true),
            row("0F", "4", false),
            row("0X", " ", false),
            row("0B", "2", false),
            row("0D", "2", false),
            row("0J", "5", false),
            row("0L", "5", false),
            row("0N", "5", false),
            row("0Q", "5", false),
            row("0G", "5", false),
            row("0Y", " ", false)
        ) { skjema, kontoklasse, expectedResult ->
            When("For skjema $skjema, kontoklasse $kontoklasse") {
                val kostraRecord = KostraRecord(
                    index = 0,
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema,
                        RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse
                    )
                )

                Then("expected result should be equal to $expectedResult") {
                    kostraRecord.isBevilgningDriftRegnskap().shouldBeEqual(expectedResult)
                }
            }
        }
    }

    Given("isBevilgningInvesteringRegnskap()") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 2, name = RegnskapConstants.FIELD_SKJEMA),
            FieldDefinition(from = 3, to = 3, name = RegnskapConstants.FIELD_KONTOKLASSE)
        ).associateBy { it.name }

        forAll(
            row("0A", "0", true),
            row("0A", "1", false),
            row("0C", "0", true),
            row("0C", "1", false),
            row("0I", "3", false),
            row("0I", "4", true),
            row("0K", "3", false),
            row("0K", "4", true),
            row("0M", "3", false),
            row("0M", "4", true),
            row("0P", "3", false),
            row("0P", "4", true),
            row("0F", "3", false),
            row("0F", "4", true),
            row("0X", " ", false),
            row("0B", "2", false),
            row("0D", "2", false),
            row("0J", "5", false),
            row("0L", "5", false),
            row("0N", "5", false),
            row("0Q", "5", false),
            row("0G", "5", false),
            row("0Y", " ", false)
        ) { skjema, kontoklasse, expectedResult ->
            When("For skjema $skjema, kontoklasse $kontoklasse") {
                val kostraRecord = KostraRecord(
                    index = 0,
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema,
                        RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse
                    )
                )

                Then("expected result should be equal to $expectedResult") {
                    kostraRecord.isBevilgningInvesteringRegnskap().shouldBeEqual(expectedResult)
                }
            }
        }
    }

    Given("isBalanseRegnskap()") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 2, name = RegnskapConstants.FIELD_SKJEMA)
        ).associateBy { it.name }

        forAll(
            row("0A", false),
            row("0C", false),
            row("0I", false),
            row("0K", false),
            row("0M", false),
            row("0P", false),
            row("0F", false),
            row("0X", false),
            row("0B", true),
            row("0D", true),
            row("0J", true),
            row("0L", true),
            row("0N", true),
            row("0Q", true),
            row("0G", true),
            row("0Y", true)
        ) { skjema, expectedResult ->
            When("For $skjema") {
                val kostraRecord = KostraRecord(
                    index = 0,
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema
                    )
                )

                Then("expected result should be equal to $expectedResult") {
                    kostraRecord.isBalanseRegnskap().shouldBeEqual(expectedResult)
                }
            }
        }
    }

    Given("isResultatRegnskap()") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 2, name = RegnskapConstants.FIELD_SKJEMA)
        ).associateBy { it.name }

        forAll(
            row("0A", false),
            row("0C", false),
            row("0I", false),
            row("0K", false),
            row("0M", false),
            row("0P", false),
            row("0F", false),
            row("0X", true),
            row("0B", false),
            row("0D", false),
            row("0J", false),
            row("0L", false),
            row("0N", false),
            row("0Q", false),
            row("0G", false),
            row("0Y", false)
        ) { skjema, expectedResult ->
            When("For $skjema") {
                val kostraRecord = KostraRecord(
                    index = 0,
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema
                    )
                )

                Then("expected result should be equal to $expectedResult") {
                    kostraRecord.isResultatRegnskap().shouldBeEqual(expectedResult)
                }
            }
        }
    }

    Given("isRegional()") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 2, name = RegnskapConstants.FIELD_SKJEMA)
        ).associateBy { it.name }

        forAll(
            row("0A", true),
            row("0C", true),
            row("0I", false),
            row("0K", false),
            row("0M", true),
            row("0P", true),
            row("0F", false),
            row("0X", false),
            row("0B", false),
            row("0D", false),
            row("0J", false),
            row("0L", false),
            row("0N", false),
            row("0Q", false),
            row("0G", false),
            row("0Y", false)
        ) { skjema, expectedResult ->
            When("For $skjema") {
                val kostraRecord = KostraRecord(
                    index = 0,
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema
                    )
                )

                Then("expected result should be equal to $expectedResult") {
                    kostraRecord.isRegional().shouldBeEqual(expectedResult)
                }
            }
        }
    }

    Given("isUtgift()") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 3, name = FIELD_ART)
        ).associateBy { it.name }

        forAll(
            row("010", true),
            row("590", true),
            row("600", false),
            row("990", false),
        ) { art, expectedResult ->
            When("For $art") {
                val kostraRecord = KostraRecord(
                    index = 0,
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        FIELD_ART to art
                    )
                )

                Then("expected result should be equal to $expectedResult") {
                    kostraRecord.isUtgift().shouldBeEqual(expectedResult)
                }
            }
        }
    }

    Given("isInntekt()") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 3, name = FIELD_ART)
        ).associateBy { it.name }

        forAll(
            row("010", false),
            row("590", false),
            row("600", true),
            row("990", true),
        ) { art, expectedResult ->
            When("For $art") {
                val kostraRecord = KostraRecord(
                    index = 0,
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        FIELD_ART to art
                    )
                )

                Then("expected result should be equal to $expectedResult") {
                    kostraRecord.isInntekt().shouldBeEqual(expectedResult)
                }
            }
        }
    }

    Given("isOslo()") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 3, name = RegnskapConstants.FIELD_FUNKSJON)
        ).associateBy { it.name }

        forAll(
            row("030100", true),
            row("030101", false),
            row("420400", false),
            row("030000", false),
        ) { region, expectedResult ->
            When("For $region") {
                val kostraRecord = KostraRecord(
                    index = 0,
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_REGION to region
                    )
                )

                Then("expected result should be equal to $expectedResult") {
                    kostraRecord.isOslo().shouldBeEqual(expectedResult)
                }
            }
        }
    }

    Given("isOsloBydel()") {
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 3, name = RegnskapConstants.FIELD_FUNKSJON)
        ).associateBy { it.name }

        forAll(
            row("030101", true),
            row("030100", false),
            row("030000", false),
            row("420400", false),
        ) { region, expectedResult ->

            val sut = KostraRecord(
                index = 0,
                fieldDefinitionByName = fieldDefinitionsByName,
                valuesByName = mapOf(
                    RegnskapConstants.FIELD_REGION to region
                )
            )

            When("isOsloBydel $region") {
                val isOsloBydel = sut.isOsloBydel()

                Then("expected result should be equal to $expectedResult") {
                    isOsloBydel shouldBe expectedResult
                }
            }
        }
    }
})
