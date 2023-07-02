package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_SOSHJELP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_TYPE_SOSHJ_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control21YtelserTest : BehaviorSpec({
    val sut = Control21Ytelser()

    Given("context") {
        forAll(
            row(
                "ytelseSosialHjelp not checked, random ytelseType",
                0, 42, false
            ),
            row(
                "ytelseSosialHjelp checked, valid ytelseType, 2",
                1, 2, false
            ),
            row(
                "ytelseSosialHjelp checked, valid ytelseType, 3",
                1, 3, false
            ),
            row(
                "ytelseSosialHjelp checked, invalid ytelseType",
                1, 42, true
            )
        ) { description, ytelseSosialHjelp, ytelseType, expectError ->
            val context = kostraRecordInTest(ytelseSosialHjelp, ytelseType)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Feltet for 'Hadde deltakeren i løpet av de siste to månedene før " +
                            "registrert søknad ved NAV-kontoret en eller flere av følgende ytelser?' " +
                            "inneholder ugyldige verdier."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            ytelseSosialHjelp: Int,
            typeYtelse: Int
        ) = kvalifiseringKostraRecordInTest(
            mapOf(
                YTELSE_SOSHJELP_COL_NAME to ytelseSosialHjelp.toString(),
                YTELSE_TYPE_SOSHJ_COL_NAME to typeYtelse.toString()
            )
        )
    }
}
