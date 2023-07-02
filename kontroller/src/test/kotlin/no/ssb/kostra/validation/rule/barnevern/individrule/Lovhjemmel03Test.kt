package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.lovhjemmelTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest
import java.time.LocalDate
import java.time.Year

class Lovhjemmel03Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Lovhjemmel03(),
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
                    "individ with fodselsnummer, age below eighteen, no tiltak",
                    individInTest
                ),
                ForAllRowItem(
                    "age above eighteen, with omsorgstiltak",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            LocalDate.now().minusYears(20),
                            LocalDate.now().minusYears(19)
                        ),
                        tiltak = mutableListOf(tiltakTypeInTest.copy(lovhjemmel = lovhjemmelTypeInTest))
                    )
                ),

                ForAllRowItem(
                    "age above eighteen, no omsorgstiltak",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            Year.now().atDay(1).minusYears(20),
                            Year.now().atDay(1).minusYears(19)
                        ),
                        tiltak = mutableListOf(tiltakTypeInTest)
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Individet er 19 år og skal dermed ikke " +
                            "ha omsorgstiltak"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
