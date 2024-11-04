package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
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

class Rule010FunksjonTest : BehaviorSpec({
    include(
        "Content causing errors",
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule010Funksjon(funksjonList = listOf("100", "400")),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "wrong skjema 0B",
                kostraRecordsInTest("0B", " ","999")
            ),
            ForAllRowItem(
                "correct skjema 0A, wrong funksjon, '999'",
                kostraRecordsInTest("0A", " ", "999"),
                expectedErrorMessage = "Fant ugyldig funksjon '999'. Korrigér funksjon til en av '100, 400'"
            ),
            ForAllRowItem(
                "correct skjema 0A, correct funksjon from list",
                kostraRecordsInTest("0A", " ", "100")
            ),
            ForAllRowItem(
                "wrong skjema 0Y",
                kostraRecordsInTest("0Y", " ","999")
            ),
            ForAllRowItem(
                "correct skjema 0X, wrong funksjon, '999'",
                kostraRecordsInTest("0X", " ", "999"),
                expectedErrorMessage = "Fant ugyldig funksjon '999'. Korrigér funksjon til en av '100, 400'"
            ),
            ForAllRowItem(
                "correct skjema 0X, correct funksjon from list",
                kostraRecordsInTest("0X", " ", "100")
            ),
        ),
    )

    include(
        "Content causing warning",
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule010Funksjon(funksjonList = listOf("100", "400")),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "wrong skjema",
                kostraRecordsInTest("0B", "1","999")
            ),
            ForAllRowItem(
                "correct skjema, wrong funksjon, '999'",
                kostraRecordsInTest("0A", "1", "999"),
                expectedErrorMessage = "Fant ugyldig funksjon '999'. Korrigér funksjon til en av '100, 400'"
            ),
            ForAllRowItem(
                "correct skjema, correct funksjon from list",
                kostraRecordsInTest("0A", "1", "100")
            ),
        ),
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            skjema: String,
            kvartal: String,
            funksjon: String,
        ) = mapOf(
            FIELD_SKJEMA to skjema,
            FIELD_KVARTAL to kvartal,
            FIELD_FUNKSJON to funksjon,
            FIELD_BELOP to "0"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions).asList()
    }
}
