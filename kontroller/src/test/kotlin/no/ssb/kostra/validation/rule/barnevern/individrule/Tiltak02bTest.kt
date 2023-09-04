package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest as kostraIndividInTest1

class Tiltak02bTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Tiltak02b(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id,
            ForAllRowItem(
                "individ without tiltak",
                kostraIndividInTest1
            ),
            ForAllRowItem(
                "tiltak without sluttDato",
                kostraIndividInTest1.copy(tiltak = mutableListOf(tiltakTypeInTest))
            ),
            ForAllRowItem(
                "tiltak with sluttDato in reporting year",
                kostraIndividInTest1.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(sluttDato = dateInTest.minusYears(1))
                    )
                )
            ),
            ForAllRowItem(
                "tiltak with sluttDato before startDato",
                kostraIndividInTest1.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(sluttDato = dateInTest.minusDays(1))
                    )
                ),
                expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Sluttdato " +
                        "(${dateInTest.minusDays(1)}) er ikke i rapporteringsåret " +
                        "(${argumentsInTest.aargang})"
            )
        )
    )
})
