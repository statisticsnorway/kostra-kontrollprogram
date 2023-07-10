package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData

class Control022TilknytningTilTrygdesystemetOgAlderTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Control022TilknytningTilTrygdesystemetOgAlder(),
            forAllRows = listOf(
                ForAllRowItem(
                    "trygdesitCode = 00, fodselsNummer = 00",
                    kostraRecordInTest("00", 0, RuleTestData.argumentsInTest.aargang.toInt())
                ),
                ForAllRowItem(
                    "trygdesitCode = 07, alder = 63",
                    kostraRecordInTest("07", 63, RuleTestData.argumentsInTest.aargang.toInt())
                ),
                ForAllRowItem(
                    "trygdesitCode = 07, alder = 62",
                    kostraRecordInTest("07", 62, RuleTestData.argumentsInTest.aargang.toInt()),
                    expectedErrorMessage = "Mottakeren (62 år) er 62 år eller yngre og mottar alderspensjon."
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            trygdesitCode: String,
            alder: Int,
            year: Int
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                SosialColumnNames.TRYGDESIT_COL_NAME to trygdesitCode,
                SosialColumnNames.PERSON_FODSELSNR_COL_NAME to RandomUtils.generateRandomSsn(year, alder)
            )
        )
    }
}