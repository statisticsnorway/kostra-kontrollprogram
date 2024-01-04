package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils
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
                    fodselsnummer = RandomUtils.generateRandomSsn(24, LocalDate.now().year)
                )
            ),
            ForAllRowItem(
                "individ with fodselsnummer, age is 25",
                individInTest.copy(
                    fodselsnummer = RandomUtils.generateRandomSsn(25, LocalDate.now().year)
                )
            ),
            ForAllRowItem(
                "individ age above 25",
                individInTest.copy(
                    fodselsnummer = RandomUtils.generateRandomSsn(26, LocalDate.now().year)
                ),
                expectedErrorMessage = "Individet er 25 år og skal avsluttes som klient"
            )
        )
    )
})
