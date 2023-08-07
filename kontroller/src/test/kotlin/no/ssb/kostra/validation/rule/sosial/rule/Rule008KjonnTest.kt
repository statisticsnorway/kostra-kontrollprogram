package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule008KjonnTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule008Kjonn(),
            forAllRows = listOf(
                ForAllRowItem(
                    "record with kjonn = MANN",
                    kostraRecordInTest("1"),
                ),
                ForAllRowItem(
                    "record with kjonn = KVINNE",
                    kostraRecordInTest("2"),
                ),
                ForAllRowItem(
                    "record with empty kjonn",
                    kostraRecordInTest(""),
                    expectedErrorMessage = "Korrigér kjønn. Fant '', forventet én av [1=Mann, 2=Kvinne]. Mottakerens kjønn er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut.",
                ),
                ForAllRowItem(
                    "record with invalid kjonn",
                    kostraRecordInTest("42"),
                    expectedErrorMessage = "Korrigér kjønn. Fant '42', forventet én av [1=Mann, 2=Kvinne]. Mottakerens kjønn er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut.",
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(genderId: String) = listOf(
            mapOf(
                KvalifiseringColumnNames.KJONN_COL_NAME to genderId,
                KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME to "~fodselsnr~",
                KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME to "~journalId~"
            ).toKostraRecord(lineNumber = 1, fieldDefinitions = fieldDefinitions)
        )
    }
}