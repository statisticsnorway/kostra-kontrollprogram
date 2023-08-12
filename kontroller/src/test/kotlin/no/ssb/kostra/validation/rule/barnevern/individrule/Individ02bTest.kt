package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest

class Individ02bTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Individ02b(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = individInTest.id,
            ForAllRowItem(
                "individ without sluttDato",
                individInTest
            ),
            ForAllRowItem(
                "individ with valid sluttDato",
                individInTest.copy(sluttDato = dateInTest.plusDays(1))
            ),
            ForAllRowItem(
                "individ with invalid sluttDato",
                individInTest.copy(sluttDato = dateInTest.minusYears(2)),
                expectedErrorMessage = "Individets sluttdato (${dateInTest.minusYears(2)}) er " +
                        "før forrige telletidspunkt"
            )
        )
    )
})
