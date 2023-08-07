package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_SOSHJELP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_TYPE_SOSHJ_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Rule021YtelserTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule021Ytelser(),
            forAllRows = listOf(
                ForAllRowItem(
                    "ytelseSosialHjelp not checked, random ytelseType",
                    kostraRecordInTest(0, 42),
                ),
                ForAllRowItem(
                    "ytelseSosialHjelp checked, valid ytelseType, 2",
                    kostraRecordInTest(1, 2),
                ),
                ForAllRowItem(
                    "ytelseSosialHjelp checked, valid ytelseType, 3",
                    kostraRecordInTest(1, 3),
                ),
                ForAllRowItem(
                    "ytelseSosialHjelp checked, invalid ytelseType",
                    kostraRecordInTest(1, 42),
                    "Feltet for 'Hadde deltakeren i løpet av de siste to månedene før " +
                            "registrert søknad ved NAV-kontoret en eller flere av følgende ytelser?' " +
                            "inneholder ugyldige verdier.",
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            ytelseSosialHjelp: Int,
            typeYtelse: Int
        ) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(
                    YTELSE_SOSHJELP_COL_NAME to ytelseSosialHjelp.toString(),
                    YTELSE_TYPE_SOSHJ_COL_NAME to typeYtelse.toString()
                )
            )
        )
    }
}
