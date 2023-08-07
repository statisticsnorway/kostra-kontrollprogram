package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ORDINAERTARB_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Rule038FullforteAvsluttedeProgramSituasjon.Companion.qualifyingFieldNames

class Rule038FullforteAvsluttedeProgramSituasjonTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule038FullforteAvsluttedeProgramSituasjon(),
            forAllRows = listOf(
                ForAllRowItem(
                    "statusCode != 3",
                    kostraRecordInTest(2),
                ),
                ForAllRowItem(
                    "statusCode = 3, one valid item selected",
                    kostraRecordInTest(3, "01"),
                ),
                ForAllRowItem(
                    "statusCode = 7, one valid item selected",
                    kostraRecordInTest(7, "01"),
                ),

                ForAllRowItem(
                    "statusCode = 3, no items selected",
                    kostraRecordInTest(3),
                    "Feltet 'Ved fullført program eller program avsluttet etter " +
                            "avtale (gjelder ikke flytting) – hva var deltakerens situasjon umiddelbart etter " +
                            "avslutningen'? Må fylles ut dersom det er krysset av for svaralternativ 3 = " +
                            "Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke " +
                            "flytting) under feltet for 'Hva er status for deltakelsen i " +
                            "kvalifiseringsprogrammet per 31.12.${argumentsInTest.aargang}'?",
                ),
                ForAllRowItem(
                    "statusCode = 7, no items selected",
                    kostraRecordInTest(7),
                    "Feltet 'Ved fullført program eller program avsluttet etter " +
                            "avtale (gjelder ikke flytting) – hva var deltakerens situasjon umiddelbart etter " +
                            "avslutningen'? Må fylles ut dersom det er krysset av for svaralternativ 3 = " +
                            "Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke " +
                            "flytting) under feltet for 'Hva er status for deltakelsen i " +
                            "kvalifiseringsprogrammet per 31.12.${argumentsInTest.aargang}'?",
                ),
                ForAllRowItem(
                    "statusCode = 3, invalid items selected",
                    kostraRecordInTest(3, "02"),
                    "Feltet 'Ved fullført program eller program avsluttet etter " +
                            "avtale (gjelder ikke flytting) – hva var deltakerens situasjon umiddelbart etter " +
                            "avslutningen'? Må fylles ut dersom det er krysset av for svaralternativ 3 = " +
                            "Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke " +
                            "flytting) under feltet for 'Hva er status for deltakelsen i " +
                            "kvalifiseringsprogrammet per 31.12.${argumentsInTest.aargang}'?",
                ),
                ForAllRowItem(
                    "statusCode = 7, invalid items selected",
                    kostraRecordInTest(7, "02"),
                    "Feltet 'Ved fullført program eller program avsluttet etter " +
                            "avtale (gjelder ikke flytting) – hva var deltakerens situasjon umiddelbart etter " +
                            "avslutningen'? Må fylles ut dersom det er krysset av for svaralternativ 3 = " +
                            "Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke " +
                            "flytting) under feltet for 'Hva er status for deltakelsen i " +
                            "kvalifiseringsprogrammet per 31.12.${argumentsInTest.aargang}'?",
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            statusCode: Int,
            avslOrdinaertarb: String = " "
        ) = listOf(kvalifiseringKostraRecordInTest(
            mapOf(
                STATUS_COL_NAME to "$statusCode",
                AVSL_ORDINAERTARB_COL_NAME to avslOrdinaertarb,
                *(qualifyingFieldNames.filter { it != AVSL_ORDINAERTARB_COL_NAME }.map { it to " " }).toTypedArray()
            )
        )
        )
    }
}
