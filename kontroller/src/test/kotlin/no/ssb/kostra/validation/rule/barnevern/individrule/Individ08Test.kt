package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.testutil.RandomUtils.generateRandomSsn
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest
import java.time.LocalDate

class Individ08Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Individ08(),
            expectedSeverity = Severity.WARNING,
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
                "individ with fodselsnummer, age below 18",
                individInTest.copy(
                    fodselsnummer = generateRandomSsn(18, LocalDate.now().year)
                )
            ),
            ForAllRowItem(
                "individ with fodselsnummer, age above 18 with measure",
                individInTest.copy(
                    tiltak = mutableListOf(tiltakTypeInTest),
                    fodselsnummer = generateRandomSsn(19, LocalDate.now().year)
                )
            ),
            ForAllRowItem(
                "individ with fodselsnummer, age above 18 without measure",
                individInTest.copy(
                    fodselsnummer = generateRandomSsn(19, LocalDate.now().year)
                ),
                expectedErrorMessage = "Individet er over 18 år og skal dermed ha tiltak"
            )
        )
    )
})
