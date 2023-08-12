package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest

class Individ02aTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Individ02a(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = individInTest.id,
            ForAllRowItem(
                "individ without endDate",
                individInTest
            ),
            ForAllRowItem(
                "individ with valid endDate",
                individInTest.copy(sluttDato = dateInTest.plusDays(1))
            ),
            ForAllRowItem(
                "individ with invalid startdato",
                individInTest.copy(sluttDato = dateInTest.minusDays(1)),
                expectedErrorMessage = "Individets startdato ($dateInTest) er etter " +
                        "sluttdato (${dateInTest.minusDays(1)})"
            )
        )
    )
})
