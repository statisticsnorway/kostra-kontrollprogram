package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule022TrygdeSituasjonGyldigeKoderTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule022TrygdeSituasjonGyldigeKoder(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "trygdesitCode = 01",
                kostraRecordInTest("01"),
            ),
            ForAllRowItem(
                "trygdesitCode = XX",
                kostraRecordInTest("XX"),
                expectedErrorMessage = "Mottakerens trygdesituasjon ved siste kontakt med sosial-/NAV-kontoret skal oppgis. " +
                        "Fant '(XX)', forventet én av '([01=Sykepenger, " +
                        "02=Dagpenger, 04=Uføretrygd, 05=Overgangsstønad, " +
                        "06=Omstillingsstønad/tidl. Etterlattepensjon, " +
                        "07=Alderspensjon, 09=Supplerende stønad (kort botid), " +
                        "10=Annen trygd, 11=Arbeidsavklaringspenger, "+
                        "12=Har ingen trygd/pensjon, 13=Barnetrygd])",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            trygdesitCode: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                TRYGDESIT_COL_NAME to trygdesitCode
            )
        )
    }
}