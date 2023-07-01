package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.lovhjemmelTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest
import java.time.LocalDate
import java.time.Year

class Lovhjemmel03Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Lovhjemmel03(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without fodselsnummer",
                    individInTest.copy(fodselsnummer = null),
                    false
                ),
                ForAllRowItem(
                    "individ with invalid fodselsnummer",
                    individInTest.copy(fodselsnummer = "12345612345"),
                    false
                ),
                ForAllRowItem(
                    "individ with fodselsnummer, age below eighteen, no tiltak",
                    individInTest,
                    false
                ),
                ForAllRowItem(
                    "age above eighteen, with omsorgstiltak",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            LocalDate.now().minusYears(20),
                            LocalDate.now().minusYears(19)
                        ),
                        tiltak = mutableListOf(tiltakTypeInTest.copy(lovhjemmel = lovhjemmelTypeInTest))
                    ),
                    false
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
                    true
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Individet er 19 år og skal dermed ikke " +
                    "ha omsorgstiltak",
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
