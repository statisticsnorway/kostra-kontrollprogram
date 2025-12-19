package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule030StonadssumMaksimumTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule030StonadssumMaksimum(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "bidrag = 1000000, laan = 0",
                kostraRecordInTest("1000000", "0"),
            ),
            ForAllRowItem(
                "bidrag = 1000000, laan = 1",
                kostraRecordInTest("1000000", "1"),
                expectedErrorMessage = "Det samlede stønadsbeløpet (summen (1000001) av bidrag (1000000) og lån (1)) " +
                        "som mottakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås " +
                        "kontrollgrense på kr. (1000000),-.",
            ),
            ForAllRowItem(
                "bidrag = 1, laan = 1000000",
                kostraRecordInTest("1", "1000000"),
                expectedErrorMessage = "Det samlede stønadsbeløpet (summen (1000001) av bidrag (1) og lån (1000000)) " +
                        "som mottakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås " +
                        "kontrollgrense på kr. (1000000),-.",
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