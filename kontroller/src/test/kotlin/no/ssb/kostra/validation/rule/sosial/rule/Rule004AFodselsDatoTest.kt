package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule004AFodselsDatoTest :
    BehaviorSpec({
        include(
            KostraTestFactory.validationRuleNoContextTest(
                sut = Rule004AFodselsDato(),
                expectedSeverity = Severity.ERROR,
                ForAllRowItem(
                    "record with valid birthdate",
                    kostraRecordInTest("31122025"),
                ),
                ForAllRowItem(
                    "record with non-numeric birthdate",
                    kostraRecordInTest("XXXXXXXX"),
                    expectedErrorMessage =
                        "Det er ikke oppgitt fødselsdato eller fødselsdato er ugyldig. " +
                            "Korriger fødselsdato til en gyldig dato. " +
                            "Fant (XXXXXXXX), forventet (DDMMÅÅÅÅ)",
                ),
                ForAllRowItem(
                    "record with invalid birthdate",
                    kostraRecordInTest("32132025"),
                    expectedErrorMessage =
                        "Det er ikke oppgitt fødselsdato eller fødselsdato er ugyldig. " +
                            "Korriger fødselsdato til en gyldig dato. " +
                            "Fant (32132025), forventet (DDMMÅÅÅÅ)",
                ),
                ForAllRowItem(
                    "record with missing birthdate",
                    kostraRecordInTest("        "),
                    expectedErrorMessage =
                        "Det er ikke oppgitt fødselsdato eller fødselsdato er ugyldig. " +
                            "Korriger fødselsdato til en gyldig dato. " +
                            "Fant (        ), forventet (DDMMÅÅÅÅ)",
                ),
            ),
        )
    }) {
    companion object {
        private fun kostraRecordInTest(birthdate: String) =
            listOf(
                mapOf(
                    KvalifiseringColumnNames.FODSELSDATO_COL_NAME to birthdate,
                    KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME to "~fodselsnr~",
                    KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                    KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME to "~journalId~",
                ).toKostraRecord(lineNumber = 1, fieldDefinitions = KvalifiseringFieldDefinitions.fieldDefinitions),
            )
    }
}
