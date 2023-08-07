package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.LAAN_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule031StonadssumMinimumTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule031StonadssumMinimum(),
            forAllRows = listOf(
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
                ),
            ),
            expectedSeverity = Severity.ERROR
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