package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule010UtgattFunksjonTest :
    BehaviorSpec({
        include(
            "Content causing warning",
            KostraTestFactory.validationRuleNoArgsTest(
                sut = Rule010UtgattFunksjon(funksjonList = listOf("100", "400")),
                expectedSeverity = Severity.WARNING,
                ForAllRowItem(
                    "wrong skjema",
                    kostraRecordsInTest("0B", "999"),
                ),
                ForAllRowItem(
                    "correct skjema, wrong funksjon, '100'",
                    kostraRecordsInTest("0A", "100"),
                    expectedErrorMessage = "Fant utgått funksjon '100', men godtas midlertidig.",
                ),
                ForAllRowItem(
                    "correct skjema, correct funksjon from list",
                    kostraRecordsInTest("0A", "999"),
                ),
            ),
        )
    }) {
    companion object {
        private fun kostraRecordsInTest(
            skjema: String,
            funksjon: String,
        ) = mapOf(
            RegnskapConstants.FIELD_SKJEMA to skjema,
            RegnskapConstants.FIELD_FUNKSJON to funksjon,
            RegnskapConstants.FIELD_BELOP to "1",
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions).asList()
    }
}
