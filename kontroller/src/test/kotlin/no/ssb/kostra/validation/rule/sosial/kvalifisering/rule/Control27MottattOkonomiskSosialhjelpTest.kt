package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_HUSBANK_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_KOMMBOS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_ENGANG_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_PGM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_SUP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.MUNICIPALITY_ID_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion

class Control27MottattOkonomiskSosialhjelpTest : BehaviorSpec({
    val sut = Control27MottattOkonomiskSosialhjelp()

    Given("valid context") {
        forAll(
            row(
                "valid kvpMedAStonad, 1",
                kostraRecordInTest(1, 4, false)
            ),
            row(
                "valid kvpMedAStonad, 2",
                kostraRecordInTest(2, 0, true)
            ),
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
/*
            row(
                "invalid kvpMedAStonad", 1, 1, true,
                "Svaralternativer for feltet \"Har deltakeren i 2022 i løpet av perioden med kvalifiseringsstønad " +
                        "mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte?\" har ugyldige " +
                        "koder. Feltet er obligatorisk å fylle ut. Det er mottatt støtte."
            ),
*/
            row(
                "invalid kvpMedAStonad", 2, 1, false,
                "Svaralternativer for feltet \"Har deltakeren i 2022 i løpet av perioden med kvalifiseringsstønad " +
                        "mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte?\" har ugyldige " +
                        "koder. Feltet er obligatorisk å fylle ut. Det er IKKE mottatt støtte."
            )

        ) { description, kvpMedAStonad, kvpMedKommBos, useEmptyValues, expectedError ->

            When(description) {
                val reportEntryList = sut.validate(
                    kostraRecordInTest(
                        kvpMedAStonad,
                        kvpMedKommBos,
                        useEmptyValues
                    ), argumentsInTest
                )

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldStartWith  expectedError
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            kvpMedAStonad: Int,
            kvpMedKommBos: Int = 4,
            useEmptyValues: Boolean
        ) = KostraRecord(
            1,
            mapOf(
                MUNICIPALITY_ID_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KVP_MED_ASTONAD_COL_NAME to kvpMedAStonad.toString(),

                KVP_MED_KOMMBOS_COL_NAME to kvpMedKommBos.toString(),
                KVP_MED_HUSBANK_COL_NAME to if (useEmptyValues) "0" else "5",
                KVP_MED_SOSHJ_ENGANG_COL_NAME to if (useEmptyValues) " " else "9",
                KVP_MED_SOSHJ_PGM_COL_NAME to if (useEmptyValues) "0" else "8",
                KVP_MED_SOSHJ_SUP_COL_NAME to if (useEmptyValues) " " else "7"
            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
