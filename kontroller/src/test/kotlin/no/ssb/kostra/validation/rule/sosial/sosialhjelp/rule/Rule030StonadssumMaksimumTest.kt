package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.LAAN_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule030StonadssumMaksimumTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule030StonadssumMaksimum(),
            forAllRows = listOf(
                ForAllRowItem(
                    "bidrag = 1000, laan = 0",
                    kostraRecordInTest("1000", "0"),
                ),
                ForAllRowItem(
                    "bidrag = 600000, laan = 0",
                    kostraRecordInTest("600000", "0"),
                    expectedErrorMessage = "Det samlede stønadsbeløpet (summen (600000) av bidrag (600000) og lån (0)) " +
                            "som mottakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås " +
                            "kontrollgrense på kr. (600000),-.",
                ),
                ForAllRowItem(
                    "bidrag = 0, laan = 600000",
                    kostraRecordInTest("0", "600000"),
                    expectedErrorMessage = "Det samlede stønadsbeløpet (summen (600000) av bidrag (0) og lån (600000)) " +
                            "som mottakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås " +
                            "kontrollgrense på kr. (600000),-.",
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