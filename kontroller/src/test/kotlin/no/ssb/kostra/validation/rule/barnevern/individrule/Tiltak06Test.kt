package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kategoriTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak06Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Tiltak06(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without fodselsnummer",
                    individInTest.copy(fodselsnummer = null)
                ),
                ForAllRowItem(
                    "individ with invalid fodselsnummer",
                    individInTest.copy(fodselsnummer = "12345612345")
                ),
                ForAllRowItem(
                    "individ with fodselsnummer, age below 11",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            dateInTest.minusYears(1),
                            dateInTest
                        )
                    )
                ),
                ForAllRowItem(
                    "individ with fodselsnummer, age above 11, no tiltak",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            dateInTest.minusYears(12),
                            dateInTest.minusYears(11)
                        )
                    )
                ),
                ForAllRowItem(
                    "individ with fodselsnummer, age above 11, tiltak with kategori#kode different from 4.2",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            dateInTest.minusYears(12),
                            dateInTest.minusYears(11)
                        ),
                        tiltak = mutableListOf(tiltakTypeInTest)
                    )
                ),

                ForAllRowItem(
                    "individ with fodselsnummer, age above 11, tiltak with kategori#kode equal to 4.2",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            dateInTest.minusYears(13),
                            dateInTest.minusYears(12)
                        ),
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                kategori = kategoriTypeInTest.copy(kode = "4.2")
                            )
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Barnet er over 11 år og i SFO. " +
                            "Barnets alder er"
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
