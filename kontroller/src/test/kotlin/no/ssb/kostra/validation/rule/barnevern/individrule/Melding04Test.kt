package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.melderTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest

class Melding04Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Melding04(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without melding",
                    individInTest
                ),
                ForAllRowItem(
                    "melding without sluttDato",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest)
                    )
                ),
                ForAllRowItem(
                    "melding with sluttDato in reporting year",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(sluttDato = dateInTest.minusYears(1))
                        )
                    )
                ),
                ForAllRowItem(
                    "melding with sluttDato after reporting year, no konklusjon",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest.copy(sluttDato = dateInTest))
                    )
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
                    )
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
                    )
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
                    expectedErrorMessage = "Melding (${meldingTypeInTest.id}). Konkludert melding mangler melder(e)."
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = meldingTypeInTest.id
        )
    )
})
