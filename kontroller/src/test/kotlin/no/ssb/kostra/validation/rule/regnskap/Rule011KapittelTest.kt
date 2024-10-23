package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KVARTAL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule011KapittelTest : BehaviorSpec({
    include(
        "Content causing errors",
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule011Kapittel(kapittelList = listOf("100", "400")),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "wrong skjema",
                kostraRecordsInTest("0A", " ","999")
            ),
            ForAllRowItem(
                "correct skjema, wrong sektor, '999'",
                kostraRecordsInTest("0B", " ", "999"),
                expectedErrorMessage = "Fant ugyldig kapittel '999'. Korrigér kapittel til en av '100, 400'"
            ),
            ForAllRowItem(
                "correct skjema, correct kapittel from list",
                kostraRecordsInTest("0B", " ", "100")
            ),
        ),
    )

    include(
        "Content causing warning",
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule011Kapittel(kapittelList = listOf("100", "400")),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "wrong skjema",
                kostraRecordsInTest("0A", "1","999")
            ),
            ForAllRowItem(
                "correct skjema, wrong kapittel, '999'",
                kostraRecordsInTest("0B", "1", "999"),
                expectedErrorMessage = "Fant ugyldig kapittel '999'. Korrigér kapittel til en av '100, 400'"
            ),
            ForAllRowItem(
                "correct skjema, correct kapittel from list",
                kostraRecordsInTest("0B", "1", "100")
            ),
        ),
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            skjema: String,
            kvartal: String,
            kapittel: String,
        ) = mapOf(
            FIELD_SKJEMA to skjema,
            FIELD_KVARTAL to kvartal,
            FIELD_KAPITTEL to kapittel,
            FIELD_BELOP to "0"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions).asList()
    }
}
