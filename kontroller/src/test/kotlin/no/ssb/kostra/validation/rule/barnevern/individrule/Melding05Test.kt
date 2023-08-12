package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.saksinnholdTypeInTest

class Melding05Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Melding05(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = meldingTypeInTest.id,
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
                            saksinnhold = mutableListOf(saksinnholdTypeInTest)
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
                expectedErrorMessage = "Melding (${meldingTypeInTest.id}). Konkludert melding mangler saksinnhold."
            )
        )
    )
})
