package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control37DatoForAvsluttetProgram.Companion.codesThatRequiresDate

class Control37DatoForAvsluttetProgramTest : BehaviorSpec({
    val sut = Control37DatoForAvsluttetProgram()

    Given("valid context") {
        forAll(
            *codesThatRequiresDate.map {
                row(
                    "status that requires date, $it",
                    kostraRecordInTest(it, "01".repeat(3))
                )
            }.toTypedArray(),
            row(
                "status that disallows date",
                kostraRecordInTest("1", " ".repeat(6))
            )
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        forAll(
            *codesThatRequiresDate.map {
                row(
                    "status that requires date, $it",
                    kostraRecordInTest(it, " ".repeat(6)),
                    "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', må fylles ut " +
                            "dersom det er krysset av for svaralternativ [3=Deltakeren har fullført program eller " +
                            "avsluttet program etter avtale (gjelder ikke flytting), 4=Deltakerens program er varig " +
                            "avbrutt på grunn av uteblivelse (gjelder ikke flytting), 5=Deltakerens program ble " +
                            "avbrutt på grunn av flytting til annen kommune] under feltet for 'Hva er status for " +
                            "deltakelsen i kvalifiseringsprogrammet per 31.12.2022'?"
                )
            }.toTypedArray(),
            row(
                "status that disallows date",
                kostraRecordInTest("1", "01".repeat(3)),
                "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', fant (010101), skal være blankt " +
                        "dersom det er krysset av for svaralternativ [1=Deltakeren er fortsatt i program (skjema er " +
                        "ferdig utfylt), 2=Deltakeren er i permisjon fra program (skjemaet er ferdig utfylt), 6=Kun " +
                        "for Oslos bydeler: Deltakeren flyttet til annen bydel før programperioden var over] under " +
                        "feltet for 'Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12.2022'?"
            )
        ) { description, kostraRecord, expectedError ->

            When(description) {
                val reportEntryList = sut.validate(kostraRecord, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldStartWith expectedError
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            status: String,
            endDate: String
        ) = KostraRecord(
            valuesByName = mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                STATUS_COL_NAME to status,
                AVSL_DATO_COL_NAME to endDate
            ),
            fieldDefinitionByName = fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
