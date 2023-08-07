package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule023TilknytningTilTrygdesystemetOgBarnTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule023TilknytningTilTrygdesystemetOgBarn(),
            forAllRows = listOf(
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
            ),
            expectedSeverity = Severity.ERROR
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