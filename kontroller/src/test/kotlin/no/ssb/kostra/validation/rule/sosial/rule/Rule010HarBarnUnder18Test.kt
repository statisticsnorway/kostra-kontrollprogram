package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest

class Rule010HarBarnUnder18Test : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule010HarBarnUnder18(),
            expectedSeverity = Severity.ERROR,
            *(1..2).map {
                ForAllRowItem(
                    "code = $it",
                    kostraRecordInTest(it)
                )
            }.toTypedArray(),
            ForAllRowItem(
                "code = 3",
                kostraRecordInTest(3),
                expectedErrorMessage = "Korrigér forsørgerplikt. Fant '3', forventet én av [1=Ja, 2=Nei]'. " +
                        "Det er ikke krysset av for om deltakeren har barn under 18 år, som deltakeren " +
                        "(eventuelt ektefelle/samboer) har forsørgerplikt for, og som bor i husholdningen " +
                        "ved siste kontakt. Feltet er obligatorisk å fylle ut.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: Int) = listOf(
            mapOf(
                HAR_BARN_UNDER_18_COL_NAME to code.toString(),
                PERSON_FODSELSNR_COL_NAME to "~fodselsnr~",
                SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                PERSON_JOURNALNR_COL_NAME to "~journalId~"
            ).toKostraRecord(lineNumber = 1, fieldDefinitions = KvalifiseringFieldDefinitions.fieldDefinitions)
        )
    }
}
