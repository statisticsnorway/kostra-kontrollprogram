package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BEGYNT_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.twoDigitReportingYear

class Rule016BegyntDatoTest :
    BehaviorSpec({
        include(
            validationRuleNoContextTest(
                sut = Rule016BegyntDato(),
                expectedSeverity = Severity.ERROR,
                ForAllRowItem(
                    "reportingYear = currentYear, valid date",
                    kostraRecordInTest(begyntDateString = "010120$twoDigitReportingYear"),
                ),
                ForAllRowItem(
                    "5 year diff between reportingYear and vedtakDato",
                    kostraRecordInTest(begyntDateString = "010120${twoDigitReportingYear - 5}"),
                    expectedErrorMessage =
                        "Feltet for 'Hvilken dato begynte deltakeren i program? " +
                            "(iverksettelse)' med verdien (${"010120${twoDigitReportingYear - 5}"}) enten mangler " +
                            "utfylling, har ugyldig dato eller dato som er eldre enn 4 år fra " +
                            "rapporteringsåret (${argumentsInTest.aargang}). Feltet er obligatorisk å fylle ut.",
                ),
                ForAllRowItem(
                    "vedtakDato is in the future",
                    kostraRecordInTest(begyntDateString = "010120${twoDigitReportingYear + 1}"),
                    expectedErrorMessage =
                        "Feltet for 'Hvilken dato begynte deltakeren i program? " +
                            "(iverksettelse)' med verdien (${"010120${twoDigitReportingYear + 1}"}) enten mangler " +
                            "utfylling, har ugyldig dato eller dato som er eldre enn 4 år fra " +
                            "rapporteringsåret (${argumentsInTest.aargang}). Feltet er obligatorisk å fylle ut.",
                ),
                ForAllRowItem(
                    "invalid begyntDato",
                    kostraRecordInTest(begyntDateString = "a".repeat(8)),
                    expectedErrorMessage =
                        "Feltet for 'Hvilken dato begynte deltakeren i program? " +
                            "(iverksettelse)' med verdien (${"a".repeat(8)}) enten mangler utfylling, har " +
                            "ugyldig dato eller dato som er eldre enn 4 år fra rapporteringsåret " +
                            "(${argumentsInTest.aargang}). Feltet er obligatorisk å fylle ut.",
                ),
                ForAllRowItem(
                    "For Oslo, 5 year diff between reportingYear and vedtakDato",
                    kostraRecordInTest(
                        kommunenr = OSLO_MUNICIPALITY_ID,
                        begyntDateString = "010120${twoDigitReportingYear - 5}",
                    ),
                ),
                ForAllRowItem(
                    "For Oslo, invalid begyntDato",
                    kostraRecordInTest(
                        kommunenr = OSLO_MUNICIPALITY_ID,
                        begyntDateString = "a".repeat(8),
                    ),
                ),
            ),
        )
    }) {
    companion object {
        private fun kostraRecordInTest(
            kommunenr: String = argumentsInTest.region.municipalityIdFromRegion(),
            begyntDateString: String,
        ) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(KOMMUNE_NR_COL_NAME to kommunenr, BEGYNT_DATO_COL_NAME to begyntDateString),
            ),
        )
    }
}
