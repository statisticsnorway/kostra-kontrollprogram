package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest

class Undersokelse02eTest : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Undersokelse02e(),
            forAllRows = listOf(
                ForAllRowItem("individ without melding", individInTest),
                ForAllRowItem(
                    "melding without undersokelse",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest)
                    )
                ),
                ForAllRowItem(
                    "undersokelse with startDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest
                            )
                        )
                    )
                ),

                ForAllRowItem(
                    "undersokelse with startDato before individ startDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    startDato = dateInTest.minusDays(1)
                                )
                            )
                        )
                    ),
                    expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). " +
                            "StartDato (${dateInTest.minusDays(1)}) skal være lik eller " +
                            "etter startdato (${individInTest.startDato}) på individet"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = undersokelseTypeInTest.id
        )
    )
})
