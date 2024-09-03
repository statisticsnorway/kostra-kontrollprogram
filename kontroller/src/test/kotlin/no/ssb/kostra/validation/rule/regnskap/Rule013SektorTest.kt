package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule013SektorTest : BehaviorSpec({
    include(
        "Content",
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule013Sektor(sektorList = listOf("100", "400")),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "wrong skjema",
                kostraRecordsInTest("0A", "999")
            ),
            ForAllRowItem(
                "correct skjema, wrong sektor",
                kostraRecordsInTest("0B", "999"),
                expectedErrorMessage = "Fant ugyldig sektor '999'. Korrigér sektor til en av '100, 400'"
            ),
            ForAllRowItem(
                "correct skjema, correct sektor from list",
                kostraRecordsInTest("0B", "100")
            ),
        ),
    )

    include(
        "Blank content",
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule013Sektor(sektorList = listOf("   ")),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "wrong skjema",
                kostraRecordsInTest("0A", "888")
            ),
            ForAllRowItem(
                "correct skjema, wrong sektor",
                kostraRecordsInTest("0B", "888"),
                expectedErrorMessage = "Fant ugyldig sektor '888'. Posisjoner for sektorkoder skal være blanke"
            ),
            ForAllRowItem(
                "correct skjema, correct blank sektor from empty list",
                kostraRecordsInTest("0B", "   ")
            ),
        ),
    )

    include(
        "No content",
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule013Sektor(sektorList = emptyList()),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "wrong skjema",
                kostraRecordsInTest("0A", "888")
            ),
            ForAllRowItem(
                "correct skjema, wrong sektor",
                kostraRecordsInTest("0B", "888")
            ),
            ForAllRowItem(
                "correct skjema, correct blank sektor from empty list",
                kostraRecordsInTest("0B", "   ")
            ),
        ),
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            skjema: String,
            sektor: String,
        ) = mapOf(
            FIELD_SKJEMA to skjema,
            FIELD_SEKTOR to sektor,
            FIELD_BELOP to "0"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions).asList()
    }
}
