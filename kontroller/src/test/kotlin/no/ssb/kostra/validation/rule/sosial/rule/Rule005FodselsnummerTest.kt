package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import java.time.LocalDate

class Rule005FodselsnummerTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule005Fodselsnummer(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "record with valid fodselsnummer",
                kostraRecordInTest(generateRandomSSN(LocalDate.now().minusYears(1), LocalDate.now())),
            ),
            ForAllRowItem(
                "record with 'adapted' fodselsnummer, male",
                kostraRecordInTest("01010100100"),
            ),
            ForAllRowItem(
                "record with 'adapted' fodselsnummer, female",
                kostraRecordInTest("01010100200"),
            ),
            ForAllRowItem(
                "record with invalid fodselsnummer",
                kostraRecordInTest("42"),
                expectedErrorMessage = "Det er ikke oppgitt fødselsnummer/d-nummer på deltakeren",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(individId: String) =
            listOf(
                mapOf(
                    PERSON_FODSELSNR_COL_NAME to individId,
                    SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                    PERSON_JOURNALNR_COL_NAME to "~journalId~"
                ).toKostraRecord(lineNumber = 1, fieldDefinitions = KvalifiseringFieldDefinitions.fieldDefinitions)
            )
    }
}
