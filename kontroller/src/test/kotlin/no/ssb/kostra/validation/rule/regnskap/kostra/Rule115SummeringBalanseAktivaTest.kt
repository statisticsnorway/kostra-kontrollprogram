package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule115SummeringBalanseAktivaTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule115SummeringBalanseAktiva(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                description = "all conditions match",
                context = kostraRecordsInTest("420400", "0B", 2, 10, 0),
                expectedErrorMessage = "Korrigér slik at fila inneholder registrering av aktiva/eiendeler " +
                        "(0), sum kapittel 10-29 i balanse.",
            ),
            ForAllRowItem(
                description = "isBalanseRegnskap = false",
                context = kostraRecordsInTest("420400", "0A", 2, 10, 0),
            ),
            ForAllRowItem(
                description = "isAktiva = false",
                context = kostraRecordsInTest("420400", "0B", 2, 30, 0),
            ),
            ForAllRowItem(
                description = "0 < belop",
                context = kostraRecordsInTest("420400", "0B", 2, 10, 1),
            ),
        )
    )
}) {
    companion object {
        fun kostraRecordsInTest(
            region: String,
            skjema: String,
            kontoklasse: Int,
            kapittel: Int,
            belop: Int
        ) = mapOf(
            RegnskapConstants.FIELD_REGION to region,
            RegnskapConstants.FIELD_SKJEMA to skjema,
            RegnskapConstants.FIELD_KONTOKLASSE to "$kontoklasse",
            RegnskapConstants.FIELD_KAPITTEL to "$kapittel",
            RegnskapConstants.FIELD_BELOP to "$belop"
        ).toKostraRecord(1, fieldDefinitions).asList()
    }
}