package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VKLO_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule024BTilknytningTilTrygdesystemetOgArbeidssituasjonTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule024BTilknytningTilTrygdesystemetOgArbeidssituasjon(),
            forAllRows = listOf(
                ForAllRowItem(
                    "vkloCode = 0, trygdesitCode = 00, arbsitCode = 00",
                    kostraRecordInTest("0", "00", "00")
                ),
                ForAllRowItem(
                    "vkloCode = 3, trygdesitCode = 01, arbsitCode = 00",
                    kostraRecordInTest("3", "01", "00")
                ),
                ForAllRowItem(
                    "vkloCode = 3, trygdesitCode = 11, arbsitCode = 01",
                    kostraRecordInTest("3", "11", "01")
                ),
                ForAllRowItem(
                    "vkloCode = 3, trygdesitCode = 11, arbsitCode = 08",
                    kostraRecordInTest("3", "11", "08"),
                    expectedErrorMessage = "Mottakeren mottar trygd (11=Arbeidsavklaringspenger), men det er oppgitt ugyldig kode (08=Arbeidsledig, men ikke registrert hos NAV) på arbeidssituasjon."
                ),
            ),
            expectedSeverity = Severity.ERROR
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