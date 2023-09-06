package no.ssb.kostra.area.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.KotlinArguments

class RegnskapConstantsTest : BehaviorSpec({
    Given("mappingDuplicates") {
        val kotlinArguments = KotlinArguments(
            skjema = "S",
            aargang = "YYYY",
            kvartal = "K",
            region = "RRRR",
            navn = "NNNNN",
            orgnr = "987654321",
            foretaknr = "876543210",
            harVedlegg = true,
            isRunAsExternalProcess = false,
            inputFileContent = "record1"
        )

        When("skjema = 0A") {
            val sut = kotlinArguments.copy(
                skjema = "0A"
            )

            val result = RegnskapConstants.mappingDuplicates(sut)

            Then("result should formatted as expected") {
                result.toString() shouldBe (listOf(
                    RegnskapConstants.FIELD_KONTOKLASSE,
                    RegnskapConstants.FIELD_FUNKSJON,
                    RegnskapConstants.FIELD_ART
                ) to listOf(
                    RegnskapConstants.TITLE_KONTOKLASSE,
                    RegnskapConstants.TITLE_FUNKSJON,
                    RegnskapConstants.TITLE_ART
                )).toString()
            }
        }

        When("skjema = 0B") {
            val sut = kotlinArguments.copy(
                skjema = "0B"
            )

            val result = RegnskapConstants.mappingDuplicates(sut)

            Then("result should formatted as expected") {
                result.toString() shouldBe (listOf(
                    RegnskapConstants.FIELD_KONTOKLASSE,
                    RegnskapConstants.FIELD_KAPITTEL,
                    RegnskapConstants.FIELD_SEKTOR
                ) to listOf(
                    RegnskapConstants.TITLE_KONTOKLASSE,
                    RegnskapConstants.TITLE_KAPITTEL,
                    RegnskapConstants.TITLE_SEKTOR
                )).toString()
            }
        }

        When("skjema = 0X") {
            val sut = kotlinArguments.copy(
                skjema = "0X"
            )

            val result = RegnskapConstants.mappingDuplicates(sut)

            Then("result should formatted as expected") {
                result.toString() shouldBe (listOf(
                    RegnskapConstants.FIELD_ORGNR,
                    RegnskapConstants.FIELD_KONTOKLASSE,
                    RegnskapConstants.FIELD_KAPITTEL,
                    RegnskapConstants.FIELD_SEKTOR
                ) to listOf(
                    RegnskapConstants.TITLE_ORGNR,
                    RegnskapConstants.TITLE_KONTOKLASSE,
                    RegnskapConstants.TITLE_KAPITTEL,
                    RegnskapConstants.TITLE_SEKTOR
                )).toString()
            }
        }

        When("skjema = SS") {
            val sut = kotlinArguments.copy(
                skjema = "SS"
            )

            val result = RegnskapConstants.mappingDuplicates(sut)

            Then("result should formatted as expected") {
                result.toString() shouldBe (emptyList<String>() to emptyList<String>()).toString()
            }
        }
    }

})