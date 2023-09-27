package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.SharedConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Rule014RegistreringsDato

class Rule001RecordLengthTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule001RecordLength(recordLength),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "reportingYear = currentYear, valid date",
                listOf("0A20233303600                  0248 250      300")),
            ),
            ForAllRowItem(
                "5 year diff between reportingYear and regDato",
                listOf("ERROR "),
                expectedErrorMessage = "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' " +
                        "med verdien (${"0101${KvalifiseringTestUtils.twoDigitReportingYear - 5}"}) enten mangler utfylling, har ugyldig " +
                        "dato eller dato som er eldre enn 4 år fra rapporteringsåret (${RuleTestData.argumentsInTest.aargang}). " +
                        "Feltet er obligatorisk å fylle ut.",
            )
        ))

}) {
    companion object {
        private val expectedRecordLength = 48
        private val recordLength = RegnskapFieldDefinitions.fieldLength
        private val errorRecordString = "This record should error"
        private val validRecordString = "0A20233303600                  0248 250      300"
    }
}