package no.ssb.kostra.validation.rule.regnskap.kostra.extensions

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord


class KostraRecordRegnskapExtensionsTest : BehaviorSpec({
    Given("isBevilgningRegnskap()") {
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
            val kostraRecord = mapOf(FIELD_SKJEMA to skjema).toKostraRecord(1, fieldDefinitions)

            Then("For $skjema expected result should be equal to $expectedResult") {
                kostraRecord.isBevilgningRegnskap().shouldBeEqual(expectedResult)
            }
        }
    }

    Given("isBevilgningDriftRegnskap()") {
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
            val kostraRecord = mapOf(
                FIELD_SKJEMA to skjema,
                FIELD_KONTOKLASSE to kontoklasse
            ).toKostraRecord(1, fieldDefinitions)

            Then("For skjema $skjema, kontoklasse $kontoklasse expected result should be equal to $expectedResult") {
                kostraRecord.isBevilgningDriftRegnskap().shouldBeEqual(expectedResult)
            }
        }
    }

    Given("isBevilgningInvesteringRegnskap()") {
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
            val kostraRecord = mapOf(
                FIELD_SKJEMA to skjema,
                FIELD_KONTOKLASSE to kontoklasse
            ).toKostraRecord(1, fieldDefinitions)

            Then("For skjema $skjema, kontoklasse $kontoklasse expected result should be equal to $expectedResult") {
                kostraRecord.isBevilgningInvesteringRegnskap().shouldBeEqual(expectedResult)
            }
        }
    }

    Given("isBalanseRegnskap()") {
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
            val kostraRecord = mapOf(FIELD_SKJEMA to skjema).toKostraRecord(1, fieldDefinitions)

            Then("$skjema expected result should be equal to $expectedResult") {
                kostraRecord.isBalanseRegnskap().shouldBeEqual(expectedResult)
            }
        }
    }

    Given("isResultatRegnskap()") {
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
            val kostraRecord = mapOf(FIELD_SKJEMA to skjema).toKostraRecord(1, fieldDefinitions)

            Then("$skjema expected result should be equal to $expectedResult") {
                kostraRecord.isResultatRegnskap().shouldBeEqual(expectedResult)
            }
        }
    }

    Given("isRegional()") {
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
            val kostraRecord = mapOf(FIELD_SKJEMA to skjema).toKostraRecord(1, fieldDefinitions)

            Then("$skjema expected result should be equal to $expectedResult") {
                kostraRecord.isRegional().shouldBeEqual(expectedResult)
            }
        }
    }

    Given("isInntekt()") {
        forAll(
            row("599", false),
            row("1000", false), // not space for 4 chars in code, but needed for coverage
            row("600", true),
            row("999", true)
        ) { art, expectedResult ->
            val kostraRecord = mapOf(FIELD_ART to art).toKostraRecord(1, fieldDefinitions)

            Then("$art, expected result should be equal to $expectedResult") {
                kostraRecord.isInntekt().shouldBe(expectedResult)
            }
        }
    }

    Given("isUtgift") {
        forAll(
            row("009", false),
            row("600", false),
            row("010", true),
            row("599", true)
        ) { art, expectedResult ->
            val kostraRecord = mapOf(FIELD_ART to art).toKostraRecord(1, fieldDefinitions)

            Then("$art, expected result should be equal to $expectedResult") {
                kostraRecord.isUtgift().shouldBe(expectedResult)
            }
        }
    }

    Given("isAktiva") {
        forAll(
            row("09  ", false),
            row("10  ", true),
            row("29  ", true),
            row("30  ", false)
        ) { kapittel, expectedResult ->
            val kostraRecord = mapOf(FIELD_FUNKSJON_KAPITTEL to kapittel).toKostraRecord(1, fieldDefinitions)

            Then("$kapittel, expected result should be equal to $expectedResult") {
                kostraRecord.isAktiva().shouldBe(expectedResult)
            }
        }
    }

    Given("isPassiva") {
        forAll(
            row("30  ", false),
            row("31  ", true),
            row("5999", true),
            row("6000", false)
        ) { kapittel, expectedResult ->
            val kostraRecord = mapOf(FIELD_FUNKSJON_KAPITTEL to kapittel).toKostraRecord(1, fieldDefinitions)

            Then("$kapittel, expected result should be equal to $expectedResult") {
                kostraRecord.isPassiva().shouldBe(expectedResult)
            }
        }
    }

    Given("isOsloInternRegnskap") {
        forAll(
            row("402200", "0A", false),
            row("402200", "0M", false),
            row("030100", "0A", true),
            row("030100", "0M", true),
            row("030100", "0X", false)
        ) { region, skjema, expectedResult ->
            val kostraRecord = mapOf(
                FIELD_SKJEMA to skjema,
                FIELD_REGION to region
            ).toKostraRecord(1, fieldDefinitions)

            Then("$region $skjema, expected result should be equal to $expectedResult") {
                kostraRecord.isOsloInternRegnskap().shouldBe(expectedResult)
            }
        }
    }

    Given("isOslo()") {
        forAll(
            row("030100", true),
            row("030101", false),
            row("420400", false),
            row("030000", false),
        ) { region, expectedResult ->
            val kostraRecord = mapOf(FIELD_REGION to region).toKostraRecord(1, fieldDefinitions)

            Then("$region expected result should be equal to $expectedResult") {
                kostraRecord.isOslo().shouldBeEqual(expectedResult)
            }
        }
    }

    Given("isOsloBydel()") {
        forAll(
            row("030101", true),
            row("030100", false),
            row("030000", false),
            row("420400", false),
        ) { region, expectedResult ->
            val sut = mapOf(FIELD_REGION to region).toKostraRecord(1, fieldDefinitions)

            When("isOsloBydel $region, expect $expectedResult") {
                sut.isOsloBydel().shouldBe(expectedResult)
            }
        }
    }
})
