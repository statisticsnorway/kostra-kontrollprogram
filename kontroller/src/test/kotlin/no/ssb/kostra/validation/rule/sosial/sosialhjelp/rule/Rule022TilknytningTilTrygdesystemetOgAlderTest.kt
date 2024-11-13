package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule022TilknytningTilTrygdesystemetOgAlderTest :
    BehaviorSpec({
        include(
            validationRuleNoContextTest(
                sut = Rule022TilknytningTilTrygdesystemetOgAlder(),
                expectedSeverity = Severity.ERROR,
                ForAllRowItem(
                    "trygdesitCode = 00, fodselsNummer = 00",
                    kostraRecordInTest(
                        "00",
                        RandomUtils.generateRandomSsn(age = 0, year = RuleTestData.argumentsInTest.aargang.toInt()),
                    ),
                ),
                ForAllRowItem(
                    "trygdesitCode = 07, alder = 60",
                    kostraRecordInTest(
                        "07",
                        RandomUtils.generateRandomSsn(age = 60, year = RuleTestData.argumentsInTest.aargang.toInt()),
                    ),
                ),
                ForAllRowItem(
                    "trygdesitCode = 07, alder = 59",
                    kostraRecordInTest(
                        "07",
                        RandomUtils.generateRandomSsn(age = 59, year = RuleTestData.argumentsInTest.aargang.toInt()),
                    ),
                    expectedErrorMessage = "Mottakeren (59 år) er yngre enn 60 år og mottar alderspensjon.",
                ),
                ForAllRowItem(
                    "trygdesitCode = 07, alder = -1 (pga. feil dato-del i fnr)",
                    kostraRecordInTest(
                        "07",
                        "32138800000",
                    ),
                    expectedErrorMessage = "Mottakeren (-1 år) er yngre enn 60 år og mottar alderspensjon.",
                ),
            ),
        )
    }) {
    companion object {
        private fun kostraRecordInTest(
            trygdesitCode: String,
            fnr: String,
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                TRYGDESIT_COL_NAME to trygdesitCode,
                PERSON_FODSELSNR_COL_NAME to fnr,
            ),
        )
    }
}
