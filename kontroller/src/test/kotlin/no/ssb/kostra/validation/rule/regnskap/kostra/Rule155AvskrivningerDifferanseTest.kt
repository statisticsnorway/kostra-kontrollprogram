package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecords

class Rule155AvskrivningerDifferanseTest : BehaviorSpec({
    Given("context") {
        val sut = Rule155AvskrivningerDifferanse()

        forAll(
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0C",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0C",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0C",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0C",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0I",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0I",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0I",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0I",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0K",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0K",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0K",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0K",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0P",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0P",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0P",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0P",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "860 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            )
        ) { recordList, expectError ->
            val kostraRecordList = recordList.toKostraRecords()
            val avskrivninger = kostraRecordList[0].fieldAsIntOrDefault(FIELD_BELOP)
            val motpostAvskrivninger = kostraRecordList[1].fieldAsIntOrDefault(FIELD_BELOP)

            When("$recordList") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at avskrivninger ($avskrivninger) stemmer " +
                            "overens med motpost avskrivninger ($motpostAvskrivninger) (margin på +/- 30')"
                )
            }
        }
    }
})