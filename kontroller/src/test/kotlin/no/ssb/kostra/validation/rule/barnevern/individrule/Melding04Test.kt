package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.melderTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest

class Melding04Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Melding04(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without melding",
                    individInTest,
                    false
                ),
                ForAllRowItem(
                    "melding without sluttDato",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest)
                    ),
                    false
                ),
                ForAllRowItem(
                    "melding with sluttDato in reporting year",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(sluttDato = dateInTest.minusYears(1))
                        )
                    ),
                    false
                ),
                ForAllRowItem(
                    "melding with sluttDato after reporting year, no konklusjon",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest.copy(sluttDato = dateInTest))
                    ),
                    false
                ),
                ForAllRowItem(
                    "melding with sluttDato after reporting year, with konklusjon '42'",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                sluttDato = dateInTest,
                                konklusjon = "42"
                            )
                        )
                    ),
                    false
                ),
                ForAllRowItem(
                    "melding with sluttDato after reporting year, with konklusjon '1'",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                sluttDato = dateInTest,
                                konklusjon = "1",
                                melder = mutableListOf(melderTypeInTest)
                            )
                        )
                    ),
                    false
                ),

                ForAllRowItem(
                    "melding with sluttDato after reporting year, with konklusjon '2'",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                sluttDato = dateInTest,
                                konklusjon = "2"
                            )
                        )
                    ),
                    true
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Melding (${meldingTypeInTest.id}). Konkludert melding mangler melder(e).",
            expectedContextId = meldingTypeInTest.id
        )
    )
})
