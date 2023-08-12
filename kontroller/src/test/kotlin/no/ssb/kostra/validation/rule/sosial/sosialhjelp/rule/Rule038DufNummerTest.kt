package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule038DufNummerTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule038DufNummer(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "fnr = random generated, dufnr = blank",
                kostraRecordInTest(fnr, ""),
            ),
            ForAllRowItem(
                "fnr = blank, dufnr = random generated",
                kostraRecordInTest("", dufnr),
            ),
            ForAllRowItem(
                "fnr = blank, dufnr = blank",
                kostraRecordInTest("", ""),
                expectedErrorMessage = "Det er ikke oppgitt fødselsnummer/d-nummer på sosialhjelpsmottakeren eller " +
                        "fødselsnummeret/d-nummeret inneholder feil. Oppgi ett 12-sifret DUF-nummer.",
            )
        )
    )
}) {
    companion object {
        internal val fnr = RandomUtils.generateRandomSsn(
            RuleTestData.argumentsInTest.aargang.toInt() - 50,
            RuleTestData.argumentsInTest.aargang.toInt() - 20
        ).also { println(it) }

        internal val dufnr = RandomUtils.generateRandomDuf(
            RuleTestData.argumentsInTest.aargang.toInt() - 50,
            RuleTestData.argumentsInTest.aargang.toInt() - 20
        ).also { println(it) }

        private fun kostraRecordInTest(
            fnr: String,
            dufnr: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME to fnr,
                SosialhjelpColumnNames.PERSON_DUF_COL_NAME to dufnr
            )
        )
    }
}