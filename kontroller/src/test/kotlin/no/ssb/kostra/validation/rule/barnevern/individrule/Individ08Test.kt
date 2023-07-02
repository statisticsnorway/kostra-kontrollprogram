package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest
import java.time.LocalDate

class Individ08Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ08(),
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
                    "individ with fodselsnummer, age below 18",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            LocalDate.now().minusYears(17),
                            LocalDate.now().minusYears(16)
                        )
                    )
                ),
                ForAllRowItem(
                    "individ with fodselsnummer, age above 18 with measure",
                    individInTest.copy(
                        tiltak = mutableListOf(tiltakTypeInTest),
                        fodselsnummer = generateRandomSSN(
                            LocalDate.now().minusYears(20),
                            LocalDate.now().minusYears(19)
                        )
                    )
                ),

                ForAllRowItem(
                    "individ with fodselsnummer, age above 18 without measure",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            LocalDate.now().minusYears(20),
                            LocalDate.now().minusYears(19)
                        )
                    ),
                    expectedErrorMessage = "Individet er over 18 år og skal dermed ha tiltak"
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedContextId = individInTest.id
        )
    )
})
