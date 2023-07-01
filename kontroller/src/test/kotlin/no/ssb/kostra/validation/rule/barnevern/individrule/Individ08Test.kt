package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest
import java.time.LocalDate

class Individ08Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ08(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without fodselsnummer",
                    kostraIndividInTest.copy(fodselsnummer = null),
                    false
                ),

                ForAllRowItem(
                    "individ with invalid fodselsnummer",
                    kostraIndividInTest.copy(fodselsnummer = "12345612345"),
                    false
                ),
                ForAllRowItem(
                    "individ with fodselsnummer, age below 18",
                    kostraIndividInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            LocalDate.now().minusYears(17),
                            LocalDate.now().minusYears(16)
                        )
                    ),
                    false
                ),
                ForAllRowItem(
                    "individ with fodselsnummer, age above 18 with measure",
                    kostraIndividInTest.copy(
                        tiltak = mutableListOf(kostraTiltakTypeInTest),
                        fodselsnummer = generateRandomSSN(
                            LocalDate.now().minusYears(20),
                            LocalDate.now().minusYears(19)
                        )
                    ),
                    false
                ),

                ForAllRowItem(
                    "individ with fodselsnummer, age above 18 without measure",
                    kostraIndividInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            LocalDate.now().minusYears(20),
                            LocalDate.now().minusYears(19)
                        )
                    ),
                    true
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedErrorMessage = "Individet er over 18 år og skal dermed ha tiltak",
            expectedContextId = kostraIndividInTest.id
        )
    )
})
