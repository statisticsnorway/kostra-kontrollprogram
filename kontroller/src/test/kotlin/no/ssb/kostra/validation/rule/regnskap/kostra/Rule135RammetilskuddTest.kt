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

class Rule135RammetilskuddTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule135Rammetilskudd(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                description = "all conditions match",
                context = kostraRecordsInTest("420400", "0A", 1, 840, 800, 0),
                expectedErrorMessage = "Korrigér slik at fila inneholder rammetilskudd (0).",
            ),
            ForAllRowItem(
                description = "isOsloBydel() = true",
                context = kostraRecordsInTest("030114", "0A", 1, 840, 800, 0),
            ),
            ForAllRowItem(
                description = "isLongyearbyen() = true",
                context = kostraRecordsInTest("211100", "0A", 1, 840, 800, 0),
            ),
            ForAllRowItem(
                description = "exception for Frøya municipality",
                context = kostraRecordsInTest("501400", "0A", 1, 840, 800, 0),
            ),
            ForAllRowItem(
                description = "isRegional() = false",
                context = kostraRecordsInTest("420400", "0I", 1, 840, 800, 0),
            ),
            ForAllRowItem(
                description = "isBevilgningDriftRegnskap = false",
                context = kostraRecordsInTest("420400", "0A", 0, 840, 800, 0),
            ),
            ForAllRowItem(
                description = "isInntekt = false",
                context = kostraRecordsInTest("420400", "0A", 1, 840, 800, 0),
                expectedErrorMessage = "Korrigér slik at fila inneholder rammetilskudd (0).",
            ),
            ForAllRowItem(
                description = "belop < 0",
                context = kostraRecordsInTest("420400", "0A", 1, 840, 800, -1),
            ),
            ForAllRowItem(
                description = "isInntekt = false for quarterly reporting",
                context = kostraRecordsInTest("420400", "0A", 1, 840, 800, 0),
                arguments = kostraArguments("1"),
                expectedErrorMessage = "Korrigér slik at fila inneholder rammetilskudd (0).",
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

        private fun kostraArguments(kvartal: String) = argumentsInTest.copy(kvartal = kvartal)
    }
}

//    Given("context") {
//        val sut = Rule135Rammetilskudd()
//
//        forAll(
//            row(
//                "matches region, isRegional (0A), isBevilgningDriftRegnskap (0A, 1), funksjon (840 ), art (800), belop (0)",
//                "420400", "0A", "1", "840 ", "800", "0", true
//            ),
//            row(
//                "region does not match, Oslo",
//                "030101", "0A", "1", "840 ", "800", "0", false
//            ),
//            row(
//                "region does not match, Longyearbyen",
//                "211100", "0A", "1", "840 ", "800", "0", false
//            ),
//            row(
//                "isRegional does not match",
//                "420400", "0I", "1", "840 ", "800", "0", false
//            ),
//            row(
//                "isBevilgningDriftRegnskap does not match",
//                "420400", "0A", "0", "840 ", "800", "0", false
//            ),
//            row(
//                "funksjon does not match",
//                "420400", "0A", "1", "841 ", "800", "0", true
//            ),
//            row(
//                "art does not match",
//                "420400", "0A", "1", "840 ", "801", "0", true
//            ),
//            row(
//                "belop does not match",
//                "420400", "0A", "1", "840 ", "800", "-1", false
//            ),
//            row(
//                "exception for Frøya municipality, belop does not match",
//                "501400", "0A", "1", "840 ", "800", "1", false
//            ),
//            row(
//                "exception for Frøya municipality, belop does not match",
//                "501400", "0A", "1", "840 ", "800", "-1", false
//            )
//        ) { description, region, skjema, kontoklasse, funksjon, art, belop, expectError ->
//            val kostraRecordList = mapOf(
//                FIELD_REGION to region,
//                FIELD_SKJEMA to skjema,
//                FIELD_KONTOKLASSE to kontoklasse,
//                FIELD_FUNKSJON to funksjon,
//                FIELD_ART to art,
//                FIELD_BELOP to belop
//            ).toKostraRecord(1, fieldDefinitions).asList()
//
//            When("$description for $region, $skjema, $kontoklasse, $funksjon, $art, $belop -> $expectError") {
//                verifyValidationResult(
//                    validationReportEntries = sut.validate(kostraRecordList, argumentsInTest),
//                    expectError = expectError,
//                    expectedSeverity = Severity.ERROR,
//                    "Korrigér slik at fila inneholder rammetilskudd ($belop)."
//                )
//            }
//        }
//    }
//})