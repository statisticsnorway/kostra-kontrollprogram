package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.REG_DATO_COL_NAME
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.twoDigitReportingYear

class Rule014RegistreringsDatoTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule014RegistreringsDato(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "reportingYear = currentYear, valid date",
                kostraRecordInTest(regDatoString = "0101${twoDigitReportingYear}"),
            ),
            ForAllRowItem(
                "5 year diff between reportingYear and regDato",
                kostraRecordInTest(regDatoString = "0101${twoDigitReportingYear - 5}"),
                expectedErrorMessage = "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' " +
                        "med verdien (${"0101${twoDigitReportingYear - 5}"}) enten mangler utfylling, har ugyldig " +
                        "dato eller dato som er eldre enn 4 år fra rapporteringsåret (${argumentsInTest.aargang}). " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
            ForAllRowItem(
                "invalid regDato",
                kostraRecordInTest(regDatoString = "a".repeat(6)),
                expectedErrorMessage = "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' " +
                        "med verdien (${"a".repeat(6)}) enten mangler utfylling, har ugyldig dato eller dato " +
                        "som er eldre enn 4 år fra rapporteringsåret (${argumentsInTest.aargang}). " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
            ForAllRowItem(
                "For Oslo, 5 year diff between reportingYear and regDato",
                kostraRecordInTest(kommunenr = OSLO_MUNICIPALITY_ID, regDatoString = "0101${twoDigitReportingYear - 5}")
            ),
            ForAllRowItem(
                "For Oslo, invalid regDato",
                kostraRecordInTest(kommunenr = OSLO_MUNICIPALITY_ID, regDatoString = "a".repeat(6))
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            kommunenr: String = argumentsInTest.region.municipalityIdFromRegion(),
            regDatoString: String
        ) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(KOMMUNE_NR_COL_NAME to kommunenr, REG_DATO_COL_NAME to regDatoString)
            )
        )
    }
}
