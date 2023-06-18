package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.area.sosial.extension.municipalityIdFromRegion

class Control26MottattStotteTest : BehaviorSpec({
    val sut = Control26MottattStotte()

    Given("valid context") {
        forAll(
            row(
                "valid kvpMedAStonad, 1",
                kostraRecordInTest(1)
            ),
            row(
                "valid kvpMedAStonad, 2",
                kostraRecordInTest(2)
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
                "invalid kvpMedAStonad",
                42
            )
        ) { description, kvpMedAStonad ->

            When(description) {
                val reportEntryList = sut.validate(kostraRecordInTest(kvpMedAStonad), argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldBe "Feltet for 'Har deltakeren i 2022 i løpet av perioden med " +
                                "kvalifiseringsstønad også mottatt  økonomisk sosialhjelp, kommunal bostøtte eller " +
                                "Husbankens bostøtte?', er ikke utfylt eller feil kode (42) er benyttet. Feltet er " +
                                "obligatorisk å fylle ut."
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(kvpMedAStonad: Int) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KVP_MED_ASTONAD_COL_NAME to kvpMedAStonad.toString()
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
