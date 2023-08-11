package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kategoriTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak05Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Tiltak05(),
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
                    "individ with fodselsnummer, age below 7",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(dateInTest, dateInTest.plusYears(1))
                    )
                ),
                ForAllRowItem(
                    "individ with fodselsnummer, age above 7, no tiltak",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            dateInTest.minusYears(9),
                            dateInTest.minusYears(8)
                        )
                    )
                ),
                ForAllRowItem(
                    "individ with fodselsnummer, age above 7, tiltak with kategori#kode different from 4.1",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            dateInTest.minusYears(9),
                            dateInTest.minusYears(8)
                        ),
                        tiltak = mutableListOf(tiltakTypeInTest)
                    )
                ),

                ForAllRowItem(
                    "individ with fodselsnummer, age above 7, tiltak with kategori#kode 4.1",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            dateInTest.minusYears(9),
                            dateInTest.minusYears(8)
                        ),
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                kategori = kategoriTypeInTest.copy(kode = "4.1")
                            )
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Barnet er over 7 år og i barnehage. " +
                            "Barnets alder er"
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
