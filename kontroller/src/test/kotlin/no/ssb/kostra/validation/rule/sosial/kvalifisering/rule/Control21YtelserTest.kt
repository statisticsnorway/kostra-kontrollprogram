package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.MUNICIPALITY_ID_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_SOSHJELP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_TYPE_SOSHJ_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion

class Control21YtelserTest : BehaviorSpec({
    val sut = Control21Ytelser()

    Given("valid context") {
        forAll(
            row(
                "ytelseSosialHjelp not checked, random ytelseType",
                kostraRecordInTest(0, 42)
            ),
            row(
                "ytelseSosialHjelp checked, valid ytelseType, 2",
                kostraRecordInTest(1, 2)
            ),
            row(
                "ytelseSosialHjelp checked, valid ytelseType, 3",
                kostraRecordInTest(1, 3)
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
                "ytelseSosialHjelp checked, invalid ytelseType",
                1, 42
            )
        ) { description, ytelseSosialHjelp, ytelseType ->

            When(description) {
                val reportEntryList = sut.validate(kostraRecordInTest(ytelseSosialHjelp, ytelseType), argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldBe "Feltet for 'Hadde deltakeren i løpet av de siste to månedene før " +
                                "registrert søknad ved NAV-kontoret en eller flere av følgende ytelser?' " +
                                "inneholder ugyldige verdier."
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            ytelseSosialHjelp: Int,
            typeYtelse: Int
        ) = KostraRecord(
            1,
            mapOf(
                MUNICIPALITY_ID_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                YTELSE_SOSHJELP_COL_NAME to ytelseSosialHjelp.toString(),
                YTELSE_TYPE_SOSHJ_COL_NAME to typeYtelse.toString()

            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
