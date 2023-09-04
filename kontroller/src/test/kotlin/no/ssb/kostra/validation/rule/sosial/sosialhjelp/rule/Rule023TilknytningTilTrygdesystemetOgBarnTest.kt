package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule023TilknytningTilTrygdesystemetOgBarnTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule023TilknytningTilTrygdesystemetOgBarn(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "trygdesitCode = 00, harBarn = 0, antallBarn = 0",
                kostraRecordInTest("00", "0", "0"),
            ),
            ForAllRowItem(
                "trygdesitCode = 05, harBarn = 1, antallBarn = 1",
                kostraRecordInTest("05", "1", "1"),
            ),
            ForAllRowItem(
                "trygdesitCode = 05, harBarn = 0, antallBarn = 0",
                kostraRecordInTest("05", "0", "0"),
                expectedErrorMessage = "Mottakeren mottar overgangsstønad, men det er ikke oppgitt barn under 18 år i husholdningen.",
            ),
            ForAllRowItem(
                "trygdesitCode = 05, harBarn = 1, antallBarn = 0",
                kostraRecordInTest("05", "1", "0"),
                expectedErrorMessage = "Mottakeren mottar overgangsstønad, men det er ikke oppgitt barn under 18 år i husholdningen.",
            ),
            ForAllRowItem(
                "trygdesitCode = 05, harBarn = 1, antallBarn = blank",
                kostraRecordInTest("05", "1", " "),
                expectedErrorMessage = "Mottakeren mottar overgangsstønad, men det er ikke oppgitt barn under 18 år i husholdningen.",
            ),
            ForAllRowItem(
                "trygdesitCode = 05, harBarn = 2, antallBarn = 0",
                kostraRecordInTest("05", "2", "0"),
                expectedErrorMessage = "Mottakeren mottar overgangsstønad, men det er ikke oppgitt barn under 18 år i husholdningen.",
            ),
            ForAllRowItem(
                "trygdesitCode = 05, harBarn = 2, antallBarn = blank",
                kostraRecordInTest("05", "2", " "),
                expectedErrorMessage = "Mottakeren mottar overgangsstønad, men det er ikke oppgitt barn under 18 år i husholdningen.",
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
                TRYGDESIT_COL_NAME to trygdesitCode,
                HAR_BARN_UNDER_18_COL_NAME to harBarn,
                ANT_BARN_UNDER_18_COL_NAME to antBarn
            )
        )
    }
}