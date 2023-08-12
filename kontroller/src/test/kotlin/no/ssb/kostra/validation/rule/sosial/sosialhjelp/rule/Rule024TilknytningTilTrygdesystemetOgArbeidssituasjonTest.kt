package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule024TilknytningTilTrygdesystemetOgArbeidssituasjonTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule024TilknytningTilTrygdesystemetOgArbeidssituasjon(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "trygdesitCode = 00, arbsitCode = 00",
                kostraRecordInTest("00", "00"),
            ),
            ForAllRowItem(
                "trygdesitCode = 04, arbsitCode = 01",
                kostraRecordInTest("04", "01"),
                expectedErrorMessage = "Mottakeren mottar trygd (04=Uføretrygd), men det er oppgitt ugyldig kode (01=Arbeid, heltid) på arbeidssituasjon.",
            ),
            ForAllRowItem(
                "trygdesitCode = 04, arbsitCode = 02",
                kostraRecordInTest("04", "02"),
            ),
            ForAllRowItem(
                "trygdesitCode = 04, arbsitCode = XX",
                kostraRecordInTest("04", "XX"),
                expectedErrorMessage = "Mottakeren mottar trygd (04=Uføretrygd), men det er oppgitt ugyldig kode (XX=Ukjent) på arbeidssituasjon.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            trygdesitCode: String,
            arbsitCode: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                TRYGDESIT_COL_NAME to trygdesitCode,
                ARBSIT_COL_NAME to arbsitCode
            )
        )
    }
}