package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VKLO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule024BTilknytningTilTrygdesystemetOgArbeidssituasjonTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule024BTilknytningTilTrygdesystemetOgArbeidssituasjon(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "vkloCode = 0, trygdesitCode = 00, arbsitCode = 00",
                kostraRecordInTest("0", "00", "00"),
            ),
            ForAllRowItem(
                "vkloCode = 3, trygdesitCode = 01, arbsitCode = 00",
                kostraRecordInTest("3", "01", "00"),
            ),
            ForAllRowItem(
                "vkloCode = 3, trygdesitCode = 11, arbsitCode = 01",
                kostraRecordInTest("3", "11", "01"),
            ),
            ForAllRowItem(
                "vkloCode = 3, trygdesitCode = 11, arbsitCode = 08",
                kostraRecordInTest("3", "11", "08"),
                expectedErrorMessage = "Mottakeren mottar trygd (11=Arbeidsavklaringspenger), men det er oppgitt ugyldig kode (08=Arbeidsledig, men ikke registrert hos NAV) på arbeidssituasjon.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            vkloCode: String,
            trygdesitCode: String,
            arbsitCode: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                VKLO_COL_NAME to vkloCode,
                TRYGDESIT_COL_NAME to trygdesitCode,
                ARBSIT_COL_NAME to arbsitCode
            )
        )
    }
}