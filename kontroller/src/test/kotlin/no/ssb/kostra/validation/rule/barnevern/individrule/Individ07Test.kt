package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import java.time.LocalDate
import java.time.Year

class Individ07Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Individ07(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = individInTest.id,
            ForAllRowItem(
                "individ without fodselsnummer",
                individInTest.copy(fodselsnummer = null)
            ),
            ForAllRowItem(
                "individ with invalid fodselsnummer",
                individInTest.copy(fodselsnummer = "12345612345")
            ),
            ForAllRowItem(
                "individ with fodselsnummer, age below 25",
                individInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        LocalDate.now().minusYears(1),
                        LocalDate.now()
                    )
                )
            ),
            ForAllRowItem(
                "individ age above 25",
                individInTest.copy(
                    fodselsnummer = generateRandomSSN(
                        Year.now().atDay(1).minusYears(26),
                        Year.now().atDay(1).minusYears(25)
                    )
                ),
                expectedErrorMessage = "Individet er 25 år og skal avsluttes som klient"
            )
        )
    )
})
