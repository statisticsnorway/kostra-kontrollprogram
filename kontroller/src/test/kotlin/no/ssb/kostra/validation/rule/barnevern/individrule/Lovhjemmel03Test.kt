package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.lovhjemmelTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Lovhjemmel03Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Lovhjemmel03(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id,
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
                    fodselsnummer = RandomUtils.generateRandomSsn(19, argumentsInTest.aargang.toInt()),
                    tiltak = mutableListOf(tiltakTypeInTest.copy(lovhjemmel = lovhjemmelTypeInTest))
                )
            ),
            ForAllRowItem(
                "age above eighteen, no omsorgstiltak",
                individInTest.copy(
                    fodselsnummer = RandomUtils.generateRandomSsn(19, argumentsInTest.aargang.toInt()),
                    tiltak = mutableListOf(tiltakTypeInTest)
                ),
                expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Individet er 19 år og skal dermed ikke " +
                        "ha omsorgstiltak"
            )
        )
    )
})
