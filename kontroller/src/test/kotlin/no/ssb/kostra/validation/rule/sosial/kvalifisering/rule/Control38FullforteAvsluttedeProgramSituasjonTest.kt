package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ORDINAERTARB_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control38FullforteAvsluttedeProgramSituasjon.Companion.qualifyingFields

class Control38FullforteAvsluttedeProgramSituasjonTest : BehaviorSpec({
    val sut = Control38FullforteAvsluttedeProgramSituasjon()

    Given("valid context") {
        forAll(
            row(
                "no qualifying fields matches",
                kostraRecordInTest("1", " ")
            ),
            row(
                "one qualifying fields matches, status != 3",
                kostraRecordInTest("1", "1")
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
            row(
                "one qualifying fields matches, status = 3",
                kostraRecordInTest("3", "01")
            )
        ) { description, kostraRecord ->

            When(description) {
                val reportEntryList = sut.validate(kostraRecord, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldStartWith "Feltet 'Ved fullført program eller program avsluttet etter " +
                                "avtale (gjelder ikke flytting) – hva var deltakerens situasjon umiddelbart etter " +
                                "avslutningen'? Må fylles ut dersom det er krysset av for svaralternativ 3 = " +
                                "Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke " +
                                "flytting) under feltet for 'Hva er status for deltakelsen i " +
                                "kvalifiseringsprogrammet per 31.12.2022'?"
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            status: String,
            avslOrdinaertarb: String
        ) = KostraRecord(
            index = 1,
            valuesByName = mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                STATUS_COL_NAME to status,
                AVSL_ORDINAERTARB_COL_NAME to avslOrdinaertarb,
                *(qualifyingFields.filter { it != AVSL_ORDINAERTARB_COL_NAME }.map { it to " " }).toTypedArray()
            ),
            fieldDefinitionByName = fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
