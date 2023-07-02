package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest
import java.time.LocalDate

class Undersokelse08Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Undersokelse08(),
            forAllRows = listOf(
                ForAllRowItem("individ without melding", individInTest),
                ForAllRowItem(
                    "melding without undersokelse",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest)
                    )
                ),
                ForAllRowItem(
                    "undersokelse with startDato before June with sluttDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    startDato = LocalDate.of(argumentsInTest.aargang.toInt(), 5, 1),
                                    sluttDato = LocalDate.of(argumentsInTest.aargang.toInt(), 8, 1)
                                )
                            )
                        )
                    )
                ),
                ForAllRowItem(
                    "undersokelse with startDato after June, without sluttDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    startDato = LocalDate.of(argumentsInTest.aargang.toInt(), 7, 1)
                                )
                            )
                        )
                    )
                ),

                ForAllRowItem(
                    "undersokelse with startDato before June without sluttDato",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                undersokelse = undersokelseTypeInTest.copy(
                                    startDato = LocalDate.of(argumentsInTest.aargang.toInt(), 5, 1)
                                )
                            )
                        )
                    ),
                    expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). Undersøkelsen " +
                            "startet ${LocalDate.of(argumentsInTest.aargang.toInt(), 5, 1)} " +
                            "og skal konkluderes da den har pågått i mer enn 6 måneder"
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedContextId = undersokelseTypeInTest.id
        )
    )
})
