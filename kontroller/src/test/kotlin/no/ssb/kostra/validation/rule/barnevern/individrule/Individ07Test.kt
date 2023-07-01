package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import java.time.LocalDate
import java.time.Year

class Individ07Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ07(),
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
                    "individ with fodselsnummer, age below 25",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            LocalDate.now().minusYears(1),
                            LocalDate.now()
                        )
                    ),
                    false
                ),
                ForAllRowItem(
                    "individ age above 25",
                    individInTest.copy(
                        fodselsnummer = generateRandomSSN(
                            Year.now().atDay(1).minusYears(26),
                            Year.now().atDay(1).minusYears(25)
                        )
                    ),
                    true
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Individet er 25 år og skal avsluttes som klient",
            expectedContextId = individInTest.id
        )
    )
})
