package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialFieldDefinitions
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule003KommunenummerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule003Kommunenummer(),
            forAllRows = listOf(
                ForAllRowItem(
                    "record with valid kommunenummer",
                    kostraRecordInTest(argumentsInTest.region.municipalityIdFromRegion()),
                ),
                ForAllRowItem(
                    "record with invalid kommunenummer",
                    kostraRecordInTest("4242"),
                    expectedErrorMessage = "Korrigér kommunenummeret. Fant 4242, forventet 1234.",
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(municipalityId: String) =
            listOf(
                mapOf(
                    KOMMUNE_NR_COL_NAME to municipalityId,
                    PERSON_FODSELSNR_COL_NAME to "~fodselsnr~",
                    SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                    PERSON_JOURNALNR_COL_NAME to "~journalId~"
                ).toKostraRecord(lineNumber = 1, fieldDefinitions = SosialFieldDefinitions.fieldDefinitions)
            )
    }
}
