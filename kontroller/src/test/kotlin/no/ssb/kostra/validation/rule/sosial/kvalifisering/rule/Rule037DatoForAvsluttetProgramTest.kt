package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.twoDigitReportingYear
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Rule037DatoForAvsluttetProgram.Companion.codesThatRequiresDate

class Rule037DatoForAvsluttetProgramTest :
    BehaviorSpec({
        include(
            validationRuleNoContextTest(
                sut = Rule037DatoForAvsluttetProgram(),
                expectedSeverity = Severity.ERROR,
                *codesThatRequiresDate
                    .map {
                        ForAllRowItem(
                            "status that requires date, $it, start date",
                            kostraRecordInTest(it, startDateInTest),
                        )
                    }.toTypedArray(),
                *codesThatRequiresDate
                    .map {
                        ForAllRowItem(
                            "status that requires date, $it, end date",
                            kostraRecordInTest(it, endDateInTest),
                        )
                    }.toTypedArray(),
                *codesThatRequiresDate
                    .map {
                        ForAllRowItem(
                            "status that requires date $it, end date is blank, expect error",
                            kostraRecordInTest(it, " ".repeat(8)),
                            "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', må fylles ut " +
                                "dersom det er krysset av for svaralternativ [" +
                                "3=Fullført program eller avsluttet etter avtale, " +
                                "4=Program er varig avbrutt på grunn av uteblivelse, " +
                                "5=Program ble avbrutt på grunn av flytting til annen kommune, " +
                                "7=Deltakerens program er avsluttet etter avbrudd] under feltet for 'Hva er status for " +
                                "deltakelsen i kvalifiseringsprogrammet per 31.12.${argumentsInTest.aargang}'?",
                        )
                    }.toTypedArray(),
                *codesThatRequiresDate
                    .map {
                        ForAllRowItem(
                            "status that requires date $it, end date is in previous year, expect error",
                            kostraRecordInTest(it, previousYearDateInTest),
                            "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', må fylles ut " +
                                "dersom det er krysset av for svaralternativ [" +
                                "3=Fullført program eller avsluttet etter avtale, " +
                                "4=Program er varig avbrutt på grunn av uteblivelse, " +
                                "5=Program ble avbrutt på grunn av flytting til annen kommune, " +
                                "7=Deltakerens program er avsluttet etter avbrudd] under feltet for 'Hva er status for " +
                                "deltakelsen i kvalifiseringsprogrammet per 31.12.${argumentsInTest.aargang}'?",
                        )
                    }.toTypedArray(),
                *codesThatRequiresDate
                    .map {
                        ForAllRowItem(
                            "status that requires date $it, end date is in next year, expect error",
                            kostraRecordInTest(it, nextYearDateInTest),
                            "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', må fylles ut " +
                                "dersom det er krysset av for svaralternativ [" +
                                "3=Fullført program eller avsluttet etter avtale, " +
                                "4=Program er varig avbrutt på grunn av uteblivelse, " +
                                "5=Program ble avbrutt på grunn av flytting til annen kommune, " +
                                "7=Deltakerens program er avsluttet etter avbrudd] under feltet for 'Hva er status for " +
                                "deltakelsen i kvalifiseringsprogrammet per 31.12.${argumentsInTest.aargang}'?",
                        )
                    }.toTypedArray(),
                *codesThatRequiresDate
                    .map {
                        ForAllRowItem(
                            "status that requires date $it, end date is invalid, expect error",
                            kostraRecordInTest(it, invalidDateInTest),
                            "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', må fylles ut " +
                                "dersom det er krysset av for svaralternativ [" +
                                "3=Fullført program eller avsluttet etter avtale, " +
                                "4=Program er varig avbrutt på grunn av uteblivelse, " +
                                "5=Program ble avbrutt på grunn av flytting til annen kommune, " +
                                "7=Deltakerens program er avsluttet etter avbrudd] under feltet for 'Hva er status for " +
                                "deltakelsen i kvalifiseringsprogrammet per 31.12.${argumentsInTest.aargang}'?",
                        )
                    }.toTypedArray(),
            ),
        )
    }) {
    companion object {
        private val startDateInTest = "010120$twoDigitReportingYear"
        private val endDateInTest = "311220$twoDigitReportingYear"
        private val previousYearDateInTest = "010120${twoDigitReportingYear - 1}"
        private val nextYearDateInTest = "010120${twoDigitReportingYear + 1}"
        private val invalidDateInTest = "310620$twoDigitReportingYear"

        private fun kostraRecordInTest(
            status: String,
            endDate: String,
        ) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(
                    STATUS_COL_NAME to status,
                    AVSL_DATO_COL_NAME to endDate,
                ),
            ),
        )
    }
}
