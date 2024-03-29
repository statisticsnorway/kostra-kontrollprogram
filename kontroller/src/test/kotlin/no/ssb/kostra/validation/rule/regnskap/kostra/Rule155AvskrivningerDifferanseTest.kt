package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoArgsTest

class Rule155AvskrivningerDifferanseTest : BehaviorSpec({
    include(
        validationRuleNoArgsTest(
            sut = Rule155AvskrivningerDifferanse(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "all conditions match, negative amount",
                listOf(
                    kostraRecordInTest("420400", 1, 100, 590, 1),
                    kostraRecordInTest("420400", 1, 860, 990, -32),
                ),
                expectedErrorMessage = "Korrigér i fila slik at avskrivninger (1) stemmer " +
                        "overens med motpost avskrivninger (-32) (margin på +/- 30')",
            ),
            ForAllRowItem(
                "all conditions match, positive amount",
                listOf(
                    kostraRecordInTest("420400", 1, 100, 590, 32),
                    kostraRecordInTest("420400", 1, 860, 990, -1),
                ),
                expectedErrorMessage = "Korrigér i fila slik at avskrivninger (32) stemmer " +
                        "overens med motpost avskrivninger (-1) (margin på +/- 30')",
            ),
            ForAllRowItem(
                "not it.isOsloBydel() = false",
                listOf(
                    kostraRecordInTest("030101", 1, 100, 590, 1),
                    kostraRecordInTest("030101", 1, 860, 990, -32),
                ),
            ),
            ForAllRowItem(
                "isBevilgningDriftRegnskap = false",
                listOf(
                    kostraRecordInTest("420400", 0, 100, 590, 1),
                    kostraRecordInTest("420400", 0, 860, 990, -32),
                ),
            ),

            ForAllRowItem(
                "funksjon #1 mismatch",
                listOf(
                    kostraRecordInTest("420400", 1, 99, 590, 1),
                    kostraRecordInTest("420400", 1, 860, 990, -30),
                ),
            ),
            ForAllRowItem(
                "art #1 mismatch",
                listOf(
                    kostraRecordInTest("420400", 1, 100, 591, 1),
                    kostraRecordInTest("420400", 1, 860, 990, -30),
                ),
            ),

            ForAllRowItem(
                "funksjon #2 mismatch",
                listOf(
                    kostraRecordInTest("420400", 1, 100, 590, 1),
                    kostraRecordInTest("420400", 1, 861, 990, -30),
                ),
            ),
            ForAllRowItem(
                "art #2 mismatch",
                listOf(
                    kostraRecordInTest("420400", 1, 100, 590, 1),
                    kostraRecordInTest("420400", 1, 860, 991, -30),
                ),
            ),

            ForAllRowItem(
                "sum within margin #1",
                listOf(
                    kostraRecordInTest("420400", 1, 100, 590, 31),
                    kostraRecordInTest("420400", 1, 860, 990, -1),
                ),
            ),
            ForAllRowItem(
                "sum within margin #2",
                listOf(
                    kostraRecordInTest("420400", 1, 100, 590, 1),
                    kostraRecordInTest("420400", 1, 860, 990, -31),
                ),
            )
        )
    )
}) {
    companion object {
        fun kostraRecordInTest(
            region: String,
            kontoklasse: Int,
            funksjon: Int,
            art: Int,
            belop: Int
        ) = mapOf(
            FIELD_REGION to region,
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to "$kontoklasse",
            FIELD_FUNKSJON to "$funksjon",
            FIELD_ART to "$art",
            FIELD_BELOP to "$belop"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions)
    }
}