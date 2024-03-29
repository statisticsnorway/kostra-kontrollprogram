package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule031StonadssumMinimumTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule031StonadssumMinimum(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "bidrag = 1000, laan = 0",
                kostraRecordInTest("1000", "0"),
            ),
            ForAllRowItem(
                "bidrag = 200, laan = 0",
                kostraRecordInTest("200", "0"),
                expectedErrorMessage = "Det samlede stønadsbeløpet (summen (200) av bidrag (200) og lån (0)) " +
                        "som mottakeren har fått i løpet av rapporteringsåret er lik/lavere enn Statistisk sentralbyrås " +
                        "kontrollgrense på kr. (200),-.",
            ),
            ForAllRowItem(
                "bidrag = 0, laan = 200",
                kostraRecordInTest("0", "200"),
                expectedErrorMessage = "Det samlede stønadsbeløpet (summen (200) av bidrag (0) og lån (200)) " +
                        "som mottakeren har fått i løpet av rapporteringsåret er lik/lavere enn Statistisk sentralbyrås " +
                        "kontrollgrense på kr. (200),-.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            bidrag: String,
            laan: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                BIDRAG_COL_NAME to bidrag,
                LAAN_COL_NAME to laan
            )
        )
    }
}