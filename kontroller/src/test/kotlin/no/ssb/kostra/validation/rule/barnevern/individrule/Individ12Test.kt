package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest

class Individ12Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Individ12(),
            expectedSeverity = Severity.WARNING,
            expectedContextId = individInTest.id,
            ForAllRowItem(
                "individ with valid fodselsnummer",
                individInTest
            ),
            ForAllRowItem(
                "individ with 99999 fodselsnummer",
                individInTest.copy(fodselsnummer = "02011099999")
            ),
            ForAllRowItem(
                "individ without fodselsnummer",
                individInTest.copy(fodselsnummer = null),
                expectedErrorMessage = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
            ),
            ForAllRowItem(
                "individ with invalid fodselsnummer",
                individInTest.copy(fodselsnummer = "12345612345"),
                expectedErrorMessage = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
            )
        )
    )
})
