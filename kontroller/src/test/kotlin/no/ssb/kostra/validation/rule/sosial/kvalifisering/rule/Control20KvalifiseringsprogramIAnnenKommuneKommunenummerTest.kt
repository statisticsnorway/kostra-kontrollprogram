package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.program.extension.municipalityIdFromRegion

class Control20KvalifiseringsprogramIAnnenKommuneKommunenummerTest : BehaviorSpec({
    val sut = Control20KvalifiseringsprogramIAnnenKommuneKommunenummer()

    Given("valid context") {
        forAll(
            row(
                "valid kvpKomm and kommnrKvpKomm",
                kostraRecordInTest(1, "1106")
            ),
            row(
                "valid kvpKomm and kommnrKvpKomm #2",
                kostraRecordInTest(2, "1106")
            ),
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, RuleTestData.argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        forAll(
            row(
                "invalid kommnrKvpKomm",
                1,
                "4242",
                "Det er svart '1=Ja' på om deltakeren kommer fra " +
                        "kvalifiseringsprogram i annen kommune, men kommunenummer ('4242') mangler eller " +
                        "er ugyldig. Feltet er obligatorisk å fylle ut."
            ),
            row(
                "empty kommnrKvpKomm",
                1,
                " ",
                "Det er svart '1=Ja' på om deltakeren kommer fra " +
                        "kvalifiseringsprogram i annen kommune, men kommunenummer (' ') mangler eller " +
                        "er ugyldig. Feltet er obligatorisk å fylle ut."
            ),
        ) { description, kvpKomm, kommnrKvpKomm, expectedError ->

            When(description) {
                val reportEntryList = sut.validate(
                    kostraRecordInTest(kvpKomm, kommnrKvpKomm), RuleTestData.argumentsInTest
                )

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldBe expectedError
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            kvpKomm: Int,
            kommnrKvpKomm: String
        ) = KostraRecord(
            1,
            mapOf(
                KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME to RuleTestData.argumentsInTest.region.municipalityIdFromRegion(),
                KVP_KOMM_COL_NAME to kvpKomm.toString(),
                KOMMNR_KVP_KOMM_COL_NAME to kommnrKvpKomm
            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
