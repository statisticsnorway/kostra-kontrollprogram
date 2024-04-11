package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule120SummeringBalansePassivaTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule120SummeringBalansePassiva(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                description = "all conditions match",
                context = kostraRecordsInTest("420400", "0B", 2, 31, 0),
                expectedErrorMessage = "Korrigér slik at fila inneholder registrering av passiva/gjeld og egenkapital " +
                        "(0), sum kapittel 31-5990 i balanse.",
            ),
            ForAllRowItem(
                description = "isBalanseRegnskap = false",
                context = kostraRecordsInTest("420400", "0A", 2, 31, 0),
            ),
            ForAllRowItem(
                description = "isPassiva = false",
                context = kostraRecordsInTest("420400", "0B", 2, 10, 0),
            ),
            ForAllRowItem(
                description = "0 < belop",
                context = kostraRecordsInTest("420400", "0B", 2, 31, -1),
            ),
            ForAllRowItem(
                description = "isPassiva = false for quarterly reporting",
                context = kostraRecordsInTest("420400", "0B", 2, 31, 0),
                arguments = kostraArguments("1"),
                expectedErrorMessage = "Korrigér slik at fila inneholder registrering av passiva/gjeld og egenkapital " +
                        "(0), sum kapittel 31-5990 i balanse.",
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
            kapittel: Int,
            belop: Int
        ) = mapOf(
            RegnskapConstants.FIELD_REGION to region,
            FIELD_SKJEMA to skjema,
            RegnskapConstants.FIELD_KONTOKLASSE to "$kontoklasse",
            RegnskapConstants.FIELD_KAPITTEL to "$kapittel",
            FIELD_BELOP to "$belop"
        ).toKostraRecord(1, fieldDefinitions).asList()

        private fun kostraArguments(kvartal: String) = argumentsInTest.copy(kvartal = kvartal)
    }
}


//    Given("context") {
//        val sut = Rule120SummeringBalansePassiva()
//
//        forAll(
//            row("XX", "31  ", "1", false), // feil skjema
//            row("0B", "10  ", "1", false), // feil kapittel
//            row("0B", "31  ", "0", true), // feil belop
//            row("0B", "31  ", "-1", false), // ok
//        ) { skjema, kapittel, belop, expectError ->
//            val kostraRecordList = mapOf(
//                FIELD_SKJEMA to skjema,
//                FIELD_FUNKSJON to kapittel,
//                FIELD_BELOP to belop
//            ).toKostraRecord(1, fieldDefinitions).asList()
//
//            When("Activa is zero for $skjema, $kapittel, $belop") {
//                verifyValidationResult(
//                    validationReportEntries = sut.validate(kostraRecordList, argumentsInTest),
//                    expectError = expectError,
//                    expectedSeverity = Severity.ERROR,
//                    "Korrigér slik at fila inneholder registrering av passiva/gjeld og egenkapital " +
//                            "($belop), sum kapittel 31-5990 i balanse."
//                )
//            }
//        }
//    }
//})