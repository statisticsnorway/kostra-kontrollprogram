package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.Melding02c.Companion.HENLAGT

class Melding02cTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Melding02c(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without sluttDato",
                    individInTest
                ),
                ForAllRowItem(
                    "individ with sluttDato, without melding",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1)
                    )
                ),
                ForAllRowItem(
                    "melding without sluttDato",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        melding = mutableListOf(meldingTypeInTest)
                    )
                ),
                ForAllRowItem(
                    "melding with sluttDato equal to individ",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        melding = mutableListOf(
                            meldingTypeInTest.copy(sluttDato = dateInTest.plusDays(1))
                        )
                    )
                ),
                ForAllRowItem(
                    "melding with sluttDato  after individ sluttDato, henlagt",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                sluttDato = dateInTest.plusDays(2),
                                konklusjon = HENLAGT
                            )
                        )
                    )
                ),

                ForAllRowItem(
                    "melding with sluttDato after individ sluttDato",
                    individInTest.copy(
                        sluttDato = dateInTest.plusDays(1),
                        melding = mutableListOf(meldingTypeInTest.copy(sluttDato = dateInTest.plusDays(2)))
                    ),
                    expectedErrorMessage = "Melding (${meldingTypeInTest.id}). Meldingens sluttdato " +
                            "(${dateInTest.plusDays(2)}) er etter individets sluttdato " +
                            "(${dateInTest.plusDays(1)})"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = meldingTypeInTest.id
        )
    )
})
