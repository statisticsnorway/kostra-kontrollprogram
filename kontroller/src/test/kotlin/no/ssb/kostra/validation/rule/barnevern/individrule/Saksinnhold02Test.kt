package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.saksinnholdTypeInTest

class Saksinnhold02Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Saksinnhold02(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without melding",
                    individInTest,
                    false
                ),
                ForAllRowItem(
                    "melding without saksinnhold",
                    individInTest.copy(
                        melding = mutableListOf(meldingTypeInTest)
                    ),
                    false
                ),
                ForAllRowItem(
                    "melding with saksinnhold, kode does not require presisering",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                sluttDato = dateInTest,
                                konklusjon = "1",
                                saksinnhold = mutableListOf(saksinnholdTypeInTest)
                            )
                        )
                    ),
                    false
                ),
                ForAllRowItem(
                    "melding with saksinnhold, kode does require presisering",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                sluttDato = dateInTest,
                                konklusjon = "1",
                                saksinnhold = mutableListOf(saksinnholdTypeInTest.copy(kode = "18"))
                            )
                        )
                    ),
                    false
                ),

                ForAllRowItem(
                    "melding with saksinnhold, kode does require presisering",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                sluttDato = dateInTest,
                                konklusjon = "1",
                                saksinnhold = mutableListOf(
                                    saksinnholdTypeInTest.copy(
                                        kode = "18",
                                        presisering = null
                                    )
                                )
                            )
                        )
                    ),
                    true
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Saksinnhold med kode (18) mangler presisering",
            expectedContextId = meldingTypeInTest.id
        )
    )
})
