package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control28MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX

class Control31HarKvalifiseringssumMenManglerVarighetTest : BehaviorSpec({
    val sut = Control31HarKvalifiseringssumMenManglerVarighet()

    Given("valid context") {
        forAll(
            row(
                "with months and amount",
                validKostraRecordInTest
            ),
            row(
                "without months, without amount",
                validKostraRecordInTest.copy(
                    valuesByName =  mapOf(
                        KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                        KVP_STONAD_COL_NAME to " ",
                        *((1..12).map {
                            "${MONTH_PREFIX}$it" to "  "
                        }).toTypedArray()
                    )
                )
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
                "without months, with amount",
                validKostraRecordInTest.copy(
                    valuesByName =  mapOf(
                        KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                        KVP_STONAD_COL_NAME to "1",
                        *((1..12).map {
                            "$MONTH_PREFIX$it" to "  "
                        }).toTypedArray()
                    )
                )
            )
        ) { description, kostraRecord ->

            When(description) {
                val reportEntryList = sut.validate(kostraRecord, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.WARNING
                        it.messageText shouldBe "Deltakeren har fått kvalifiseringsstønad (1) i løpet av året, " +
                                "men mangler utfylling for hvilke måneder stønaden gjelder. " +
                                "Feltet er obligatorisk å fylle ut."
                    }
                }
            }
        }
    }
}) {
    companion object {
        private val validKostraRecordInTest = KostraRecord(
            index = 1,
            valuesByName = mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KVP_STONAD_COL_NAME to "2",
                *((1..12).map {
                    "$MONTH_PREFIX$it" to it.toString().padStart(2, '0')
                }).toTypedArray()
            ),
            fieldDefinitionByName = KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
