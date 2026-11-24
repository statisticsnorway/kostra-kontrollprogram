package no.ssb.kostra.area.regnskap

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_REGIONALE
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNT_TYPE_DRIFT
import no.ssb.kostra.area.regnskap.RegnskapConstants.DEFAULT_MISSING_VALUE
import no.ssb.kostra.program.KotlinArguments

class RegnskapConstantsTest :
    BehaviorSpec({
        Given("getRegnskapTypeBySkjema") {
            data class Case(
                val description: String,
                val skjema: String,
                val expectedResult: List<String>,
            )

            listOf(
                Case("desc 0A", "0A", listOf(ACCOUNTING_TYPE_BEVILGNING, ACCOUNTING_TYPE_REGIONALE)),
                Case("desc 0AK1", "0AK1", listOf(ACCOUNTING_TYPE_BEVILGNING)),
            ).forEach { (description, skjema, expectedResult) ->
                When(description) {
                    val result = RegnskapConstants.getRegnskapTypeBySkjema(skjema)

                    Then("result should be as expected") {
                        assertSoftly(result) {
                            result shouldBe expectedResult
                        }
                    }
                }
            }
        }

        Given("mappingDuplicates") {
            val kotlinArguments =
                KotlinArguments(
                    skjema = "S",
                    aargang = "YYYY",
                    kvartal = "K",
                    region = "RRRR",
                    navn = "NNNNN",
                    orgnr = "987654321",
                    harVedlegg = true,
                    isRunAsExternalProcess = false,
                    inputFileContent = "record1",
                )

            When("skjema = 0A") {
                val sut =
                    kotlinArguments.copy(
                        skjema = "0A",
                    )

                val result = RegnskapConstants.mappingDuplicates(sut)

                Then("result should formatted as expected") {
                    result.toString() shouldBe
                        (
                            listOf(
                                RegnskapConstants.FIELD_KONTOKLASSE,
                                RegnskapConstants.FIELD_FUNKSJON,
                                RegnskapConstants.FIELD_ART,
                            ) to
                                listOf(
                                    RegnskapConstants.TITLE_KONTOKLASSE,
                                    RegnskapConstants.TITLE_FUNKSJON,
                                    RegnskapConstants.TITLE_ART,
                                )
                        ).toString()
                }
            }

            When("skjema = 0AK1") {
                val sut =
                    kotlinArguments.copy(
                        skjema = "0AK1",
                        kvartal = "1",
                    )

                val result = RegnskapConstants.mappingDuplicates(sut)

                Then("result should formatted as expected") {
                    result.toString() shouldBe
                        (
                            listOf(
                                RegnskapConstants.FIELD_KONTOKLASSE,
                                RegnskapConstants.FIELD_FUNKSJON,
                                RegnskapConstants.FIELD_ART,
                            ) to
                                listOf(
                                    RegnskapConstants.TITLE_KONTOKLASSE,
                                    RegnskapConstants.TITLE_FUNKSJON,
                                    RegnskapConstants.TITLE_ART,
                                )
                        ).toString()
                }
            }

            When("skjema = 0B") {
                val sut =
                    kotlinArguments.copy(
                        skjema = "0B",
                    )

                val result = RegnskapConstants.mappingDuplicates(sut)

                Then("result should formatted as expected") {
                    result.toString() shouldBe
                        (
                            listOf(
                                RegnskapConstants.FIELD_KONTOKLASSE,
                                RegnskapConstants.FIELD_KAPITTEL,
                                RegnskapConstants.FIELD_SEKTOR,
                            ) to
                                listOf(
                                    RegnskapConstants.TITLE_KONTOKLASSE,
                                    RegnskapConstants.TITLE_KAPITTEL,
                                    RegnskapConstants.TITLE_SEKTOR,
                                )
                        ).toString()
                }
            }

            When("skjema = 0BK1") {
                val sut =
                    kotlinArguments.copy(
                        skjema = "0BK1",
                        kvartal = "1",
                    )

                val result = RegnskapConstants.mappingDuplicates(sut)

                Then("result should formatted as expected") {
                    result.toString() shouldBe
                        (
                            listOf(
                                RegnskapConstants.FIELD_KONTOKLASSE,
                                RegnskapConstants.FIELD_KAPITTEL,
                                RegnskapConstants.FIELD_SEKTOR,
                            ) to
                                listOf(
                                    RegnskapConstants.TITLE_KONTOKLASSE,
                                    RegnskapConstants.TITLE_KAPITTEL,
                                    RegnskapConstants.TITLE_SEKTOR,
                                )
                        ).toString()
                }
            }

            When("skjema = 0X") {
                val sut =
                    kotlinArguments.copy(
                        skjema = "0X",
                    )

                val result = RegnskapConstants.mappingDuplicates(sut)

                Then("result should formatted as expected") {
                    result.toString() shouldBe
                        (
                            listOf(
                                RegnskapConstants.FIELD_ORGNR,
                                RegnskapConstants.FIELD_KONTOKLASSE,
                                RegnskapConstants.FIELD_KAPITTEL,
                                RegnskapConstants.FIELD_SEKTOR,
                            ) to
                                listOf(
                                    RegnskapConstants.TITLE_ORGNR,
                                    RegnskapConstants.TITLE_KONTOKLASSE,
                                    RegnskapConstants.TITLE_KAPITTEL,
                                    RegnskapConstants.TITLE_SEKTOR,
                                )
                        ).toString()
                }
            }

            When("skjema = SS") {
                val sut =
                    kotlinArguments.copy(
                        skjema = "SS",
                    )

                val result = RegnskapConstants.mappingDuplicates(sut)

                Then("result should formatted as expected") {
                    result.toString() shouldBe (emptyList<String>() to emptyList<String>()).toString()
                }
            }
        }

        Given("getKontoTypeBySkjemaAndKontoklasse") {
            data class Case(
                val description: String,
                val skjema: String,
                val kontoklasse: String,
                val expectedResult: String,
            )

            listOf(
                Case("0A & 1 -> BEV", "0A", "1", ACCOUNT_TYPE_DRIFT),
                Case("0A & 3 -> MISSING", "0A", "3", DEFAULT_MISSING_VALUE),
            ).forEach { (description, skjema, kontoklasse, expectedResult) ->

                When(description) {
                    val result = RegnskapConstants.getKontoTypeBySkjemaAndKontoklasse(skjema, kontoklasse)

                    Then("result should be as expected") {
                        result shouldBe expectedResult
                    }
                }
            }
        }
    })
