package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.twoDigitReportingYear
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control37DatoForAvsluttetProgram.Companion.codesThatRequiresDate

class Control37DatoForAvsluttetProgramTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control37DatoForAvsluttetProgram(),
            forAllRows = listOf(
                *codesThatRequiresDate.map {
                    ForAllRowItem(
                        "status that requires date, $it",
                        kostraRecordInTest(it, endDateInTest)
                    )
                }.toTypedArray(),
                ForAllRowItem(
                    "status that disallows date",
                    kostraRecordInTest("1", " ".repeat(6))
                ),
                *codesThatRequiresDate.map {
                    ForAllRowItem(
                        "status that requires date $it, expect error",
                        kostraRecordInTest(it, " ".repeat(6)),
                        "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', må fylles ut " +
                                "dersom det er krysset av for svaralternativ [3=Deltakeren har fullført program eller " +
                                "avsluttet program etter avtale (gjelder ikke flytting), 4=Deltakerens program er varig " +
                                "avbrutt på grunn av uteblivelse (gjelder ikke flytting), 5=Deltakerens program ble " +
                                "avbrutt på grunn av flytting til annen kommune] under feltet for 'Hva er status for " +
                                "deltakelsen i kvalifiseringsprogrammet per 31.12.${argumentsInTest.aargang}'?",
                    )
                }.toTypedArray(),
                ForAllRowItem(
                    "status that disallows date, expect error",
                    kostraRecordInTest("1", endDateInTest),
                    "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', fant " +
                            "($endDateInTest), skal være blankt dersom det er krysset av for svaralternativ " +
                            "[1=Deltakeren er fortsatt i program (skjema er ferdig utfylt), 2=Deltakeren er i " +
                            "permisjon fra program (skjemaet er ferdig utfylt), 6=Kun for Oslos bydeler: " +
                            "Deltakeren flyttet til annen bydel før programperioden var over] under feltet for " +
                            "'Hva er status for deltakelsen i kvalifiseringsprogrammet " +
                            "per 31.12.${argumentsInTest.aargang}'?"
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private val endDateInTest = "0101$twoDigitReportingYear"

        private fun kostraRecordInTest(
            status: String,
            endDate: String
        ) = kvalifiseringKostraRecordInTest(
            mapOf(
                STATUS_COL_NAME to status,
                AVSL_DATO_COL_NAME to endDate
            )
        )
    }
}
