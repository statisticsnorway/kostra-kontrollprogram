package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraPlanTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest

class Individ06Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ06(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ with melding",
                    kostraIndividInTest.copy(melding = mutableListOf(kostraMeldingTypeInTest)),
                    false
                ),
                ForAllRowItem(
                    "individ with plan",
                    kostraIndividInTest.copy(plan = mutableListOf(kostraPlanTypeInTest)),
                    false
                ),
                ForAllRowItem(
                    "individ with tiltak",
                    kostraIndividInTest.copy(tiltak = mutableListOf(kostraTiltakTypeInTest)),
                    false
                ),

                ForAllRowItem(
                    "individ with without content",
                    kostraIndividInTest,
                    true
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Individet har ingen meldinger, planer eller tiltak i løpet av året",
            expectedContextId = kostraIndividInTest.id
        )
    )
})
