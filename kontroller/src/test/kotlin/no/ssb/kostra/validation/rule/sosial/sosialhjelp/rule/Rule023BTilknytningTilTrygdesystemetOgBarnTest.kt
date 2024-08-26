package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule023BTilknytningTilTrygdesystemetOgBarnTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule023BTilknytningTilTrygdesystemetOgBarn(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "trygdesitCode = 00, harBarn = 0, antallBarn = 0",
                kostraRecordInTest("00", "0", "0"),
            ),
            ForAllRowItem(
                "trygdesitCode = 13, harBarn = 1, antallBarn = 1",
                kostraRecordInTest("13", "1", "1"),
            ),
            ForAllRowItem(
                "trygdesitCode = 13, harBarn = 0, antallBarn = 0",
                kostraRecordInTest("13", "0", "0"),
                expectedErrorMessage = "Mottakeren mottar barnetrygd, men det er ikke oppgitt barn under 18 år i husholdningen.",
            ),
            ForAllRowItem(
                "trygdesitCode = 13, harBarn = 1, antallBarn = 0",
                kostraRecordInTest("13", "1", "0"),
                expectedErrorMessage = "Mottakeren mottar barnetrygd, men det er ikke oppgitt barn under 18 år i husholdningen.",
            ),
            ForAllRowItem(
                "trygdesitCode = 13, harBarn = 1, antallBarn = blank",
                kostraRecordInTest("13", "1", " "),
                expectedErrorMessage = "Mottakeren mottar barnetrygd, men det er ikke oppgitt barn under 18 år i husholdningen.",
            ),
            ForAllRowItem(
                "trygdesitCode = 13, harBarn = 2, antallBarn = 0",
                kostraRecordInTest("13", "2", "0"),
                expectedErrorMessage = "Mottakeren mottar barnetrygd, men det er ikke oppgitt barn under 18 år i husholdningen.",
            ),
            ForAllRowItem(
                "trygdesitCode = 13, harBarn = 2, antallBarn = blank",
                kostraRecordInTest("13", "2", " "),
                expectedErrorMessage = "Mottakeren mottar barnetrygd, men det er ikke oppgitt barn under 18 år i husholdningen.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            trygdesitCode: String,
            harBarn: String,
            antBarn: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                SosialhjelpColumnNames.TRYGDESIT_COL_NAME to trygdesitCode,
                SosialhjelpColumnNames.HAR_BARN_UNDER_18_COL_NAME to harBarn,
                SosialhjelpColumnNames.ANT_BARN_UNDER_18_COL_NAME to antBarn
            )
        )
    }
}