package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.melderTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.Melder02.Companion.ANDRE_OFFENTLIGE_INSTANSER

class Melder02Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Melder02(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without melding",
                    individInTest,
                    false
                ),
                ForAllRowItem(
                    "individ with melding without melder",
                    individInTest.copy(melding = mutableListOf(meldingTypeInTest)),
                    false
                ),
                ForAllRowItem(
                    "individ with melding with melder, kode different from 22",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                melder = mutableListOf(melderTypeInTest)
                            )
                        )
                    ),
                    false
                ),
                ForAllRowItem(
                    "individ with melding with melder, kode = 22",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                melder = mutableListOf(
                                    melderTypeInTest.copy(kode = ANDRE_OFFENTLIGE_INSTANSER)
                                )
                            )
                        )
                    ),
                    false
                ),

                ForAllRowItem(
                    "individ with melding with melder, kode = 22, presisering missing",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                melder = mutableListOf(
                                    melderTypeInTest.copy(
                                        kode = ANDRE_OFFENTLIGE_INSTANSER,
                                        presisering = null
                                    )
                                )
                            )
                        )
                    ),
                    true
                ),
                ForAllRowItem(
                    "individ with melding with melder, kode = 22, presisering empty",
                    individInTest.copy(
                        melding = mutableListOf(
                            meldingTypeInTest.copy(
                                melder = mutableListOf(
                                    melderTypeInTest.copy(
                                        kode = ANDRE_OFFENTLIGE_INSTANSER,
                                        presisering = ""
                                    )
                                )
                            )
                        )
                    ),
                    true
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "Melder med kode ($ANDRE_OFFENTLIGE_INSTANSER) mangler presisering",
            expectedContextId = meldingTypeInTest.id
        )
    )
})
