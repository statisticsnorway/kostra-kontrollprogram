package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_VIKTIGSTE_INNTEKT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils
import java.time.Year

class Rule039FullforteAvsluttedeProgramInntektkildeTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule039FullforteAvsluttedeProgramInntektkilde(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "status != 3",
                kostraRecordInTest("1", " "),
            ),
            ForAllRowItem(
                "status = 3, income code defined",
                kostraRecordInTest("3", "01"),
            ),
            ForAllRowItem(
                "status = 3, no income code defined",
                kostraRecordInTest("3", "  "),
                "Feltet 'Hva var deltakerens <b>viktigste</b> inntektskilde umiddelbart etter avslutningen? " +
                        "Må fylles ut dersom det er krysset av for svaralternativ " +
                        "3 = Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke flytting) eller " +
                        "7 = Deltakeren program er avsluttet etter avbrudd under feltet for " +
                        "'Hva er status for deltakelsen i kvalifiseringsprogrammet per per 31.12.${(Year.now().value - 1)}'?",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            status: String,
            inntekt: String
        ) = listOf(
            KvalifiseringTestUtils.kvalifiseringKostraRecordInTest(
                mapOf(
                    STATUS_COL_NAME to status,
                    AVSL_VIKTIGSTE_INNTEKT_COL_NAME to inntekt,
                )
            )
        )
    }
}
