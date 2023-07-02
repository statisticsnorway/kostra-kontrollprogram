package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.planTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Individ06Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ06(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ with melding",
                    individInTest.copy(melding = mutableListOf(meldingTypeInTest))
                ),
                ForAllRowItem(
                    "individ with plan",
                    individInTest.copy(plan = mutableListOf(planTypeInTest))
                ),
                ForAllRowItem(
                    "individ with tiltak",
                    individInTest.copy(tiltak = mutableListOf(tiltakTypeInTest))
                ),

                ForAllRowItem(
                    "individ with without content",
                    individInTest,
                    expectedErrorMessage = "Individet har ingen meldinger, planer eller tiltak i løpet av året"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = individInTest.id
        )
    )
})
