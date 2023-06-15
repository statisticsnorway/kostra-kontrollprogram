package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.MUNICIPALITY_ID_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.REG_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion

class Control14RegDatoTest : BehaviorSpec({
    val sut = Control14RegDato()

    Given("valid context") {
        forAll(
            row(
                "reportingYear = currentYear, valid date",
                kostraRecordInTest(22, "010122")
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
                "4 year diff between reportingYear and regDato",
                22,
                "010116"
            ),
            row(
                "invalid regDato",
                22,
                "a".repeat(6)
            )
        ) { description, reportingYear, regDato ->

            When(description) {
                val reportEntryList = sut.validate(
                    kostraRecordInTest(reportingYear, regDato), argumentsInTest
                )

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldBe "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' " +
                                "med verdien ($regDato) enten mangler utfylling, har ugyldig dato eller dato som er " +
                                "eldre enn 4 år fra rapporteringsåret (22). Feltet er obligatorisk å fylle ut."
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            reportingYear: Int,
            regDatoString: String
        ) = KostraRecord(
            1,
            mapOf(
                MUNICIPALITY_ID_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                VERSION_COL_NAME to reportingYear.toString(),
                REG_DATO_COL_NAME to regDatoString

            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
