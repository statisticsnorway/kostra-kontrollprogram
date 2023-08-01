package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule022TilknytningTilTrygdesystemetOgAlderTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule022TilknytningTilTrygdesystemetOgAlder(),
            forAllRows = listOf(
                ForAllRowItem(
                    "trygdesitCode = 00, fodselsNummer = 00",
                    kostraRecordInTest(
                        "00",
                        RandomUtils.generateRandomSsn(age = 0, year = RuleTestData.argumentsInTest.aargang.toInt())
                    )
                ),
                ForAllRowItem(
                    "trygdesitCode = 07, alder = 63",
                    kostraRecordInTest(
                        "07",
                        RandomUtils.generateRandomSsn(age = 63, year = RuleTestData.argumentsInTest.aargang.toInt())
                    )
                ),
                ForAllRowItem(
                    "trygdesitCode = 07, alder = 62",
                    kostraRecordInTest(
                        "07",
                        RandomUtils.generateRandomSsn(age = 62, year = RuleTestData.argumentsInTest.aargang.toInt())
                    ),
                    expectedErrorMessage = "Mottakeren (62 år) er 62 år eller yngre og mottar alderspensjon."
                ),
                ForAllRowItem(
                    "trygdesitCode = 07, alder = -1 (pga. feil dato-del i fnr)",
                    kostraRecordInTest(
                        "07", "32138800000"
                    ),
                    expectedErrorMessage = "Mottakeren (-1 år) er 62 år eller yngre og mottar alderspensjon."
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            trygdesitCode: String,
            fnr: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                TRYGDESIT_COL_NAME to trygdesitCode,
                PERSON_FODSELSNR_COL_NAME to fnr
            )
        )
    }
}