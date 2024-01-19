package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest

class Undersokelse02bTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Undersokelse02b(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = undersokelseTypeInTest.id,
            ForAllRowItem("individ without undersokelse", individInTest),
            ForAllRowItem(
                "melding without undersokelse",
                individInTest.copy(
                    melding = mutableListOf(meldingTypeInTest)
                )
            ),
            ForAllRowItem(
                "undersokelse without sluttDato",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest
                        )
                    )
                )
            ),
            ForAllRowItem(
                "undersokelse with sluttDato",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                startDato = dateInTest,
                                sluttDato = dateInTest.plusDays(1)
                            )
                        )
                    )
                )
            ),
            ForAllRowItem(
                "undersokelse with sluttDato after reporting year",
                individInTest.copy(
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                startDato = dateInTest,
                                sluttDato = dateInTest.plusYears(1)
                            )
                        )
                    )
                ),
                expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). Undersøkelsens sluttdato " +
                        "(${dateInTest.plusYears(1)}) er ikke i rapporteringsåret (${argumentsInTest.aargang})"
            )
        )
    )
})
