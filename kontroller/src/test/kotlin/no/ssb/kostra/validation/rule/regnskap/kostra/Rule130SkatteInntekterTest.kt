package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule130SkatteInntekterTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule130SkatteInntekter(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                description = "all conditions match",
                context = kostraRecordsInTest("420400", "0A", 1, 800, 870, 0),
                expectedErrorMessage = "Korrigér slik at fila inneholder skatteinntekter (0).",
            ),
            ForAllRowItem(
                description = "isOsloBydel() = true",
                context = kostraRecordsInTest("030114", "0A", 1, 800, 870, 0),
            ),
            ForAllRowItem(
                description = "isLongyearbyen() = true",
                context = kostraRecordsInTest("211100", "0A", 1, 800, 870, 0),
            ),
            ForAllRowItem(
                description = "isRegional() = false",
                context = kostraRecordsInTest("420400", "0I", 1, 800, 870, 0),
            ),
            ForAllRowItem(
                description = "isBevilgningDriftRegnskap = false",
                context = kostraRecordsInTest("420400", "0A", 0, 800, 870, 0),
            ),
            ForAllRowItem(
                description = "belop < 0",
                context = kostraRecordsInTest("420400", "0A", 1, 800, 870, -1),
            ),
            ForAllRowItem(
                description = "belop = 0 for quarterly reporting",
                context = kostraRecordsInTest("420400", "0A", 1, 800, 870, 0),
                arguments = argumentsInTest.copy(kvartal = "1"),
                expectedErrorMessage = "Korrigér slik at fila inneholder skatteinntekter (0).",
                expectedSeverity = Severity.WARNING
            ),
        )
    )
}) {
    companion object {
        fun kostraRecordsInTest(
            region: String,
            skjema: String,
            kontoklasse: Int,
            funksjon: Int,
            art: Int,
            belop: Int
        ) = mapOf(
            FIELD_REGION to region,
            FIELD_SKJEMA to skjema,
            FIELD_KONTOKLASSE to "$kontoklasse",
            FIELD_FUNKSJON to "$funksjon",
            FIELD_ART to "$art",
            FIELD_BELOP to "$belop"
        ).toKostraRecord(1, fieldDefinitions).asList()
    }
}