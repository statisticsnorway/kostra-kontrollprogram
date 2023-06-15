package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion

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
                kostraRecordInTest(0, "1106")
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
                "invalid kvpKomm and kommnrKvpKomm",
                1,
                "4242"
            )
        ) { description, kvpKomm, kommnrKvpKomm ->

            When(description) {
                val reportEntryList = sut.validate(
                    kostraRecordInTest(kvpKomm, kommnrKvpKomm), RuleTestData.argumentsInTest
                )

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldBe "Det er svart ' =Uoppgitt' på om deltakeren kommer fra " +
                                "kvalifiseringsprogram i annen kommune, men kommunenummer ('$kommnrKvpKomm') mangler eller " +
                                "er ugyldig. Feltet er obligatorisk å fylle ut."
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
                KvalifiseringColumnNames.MUNICIPALITY_ID_COL_NAME to RuleTestData.argumentsInTest.region.municipalityIdFromRegion(),
                KvalifiseringColumnNames.KVP_KOMM_COL_NAME to kvpKomm.toString(),
                KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME to kommnrKvpKomm

            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
