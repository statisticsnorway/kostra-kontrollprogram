package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest

class Undersokelse02dTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Undersokelse02d(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = undersokelseTypeInTest.id,
            ForAllRowItem("individ with avslutta3112 = '2'", individInTest),
            ForAllRowItem(
                "avslutta3112 = '1', no melding",
                individInTest.copy(avslutta3112 = KOSTRA_IS_CLOSED_TRUE)
            ),
            ForAllRowItem(
                "avslutta3112 = '1', melding without undersokelse",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = dateInTest.minusYears(1).plusDays(1),
                    melding = mutableListOf(meldingTypeInTest)
                )
            ),
            ForAllRowItem(
                "avslutta3112 = '1', melding with undersokelse with valid sluttDato",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = dateInTest.minusYears(1).plusDays(1),
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                sluttDato = dateInTest.minusYears(1).plusDays(1)
                            )
                        )
                    )
                )
            ),
            ForAllRowItem(
                "avslutta3112 = '1', undersokelse without sluttdato",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = dateInTest.minusYears(1).plusDays(1),
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                sluttDato = null
                            )
                        )
                    )
                ),
                expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). Individet er avsluttet hos " +
                        "barnevernet og dets undersøkelser skal dermed være avsluttet. Sluttdato er uoppgitt"
            ),
            ForAllRowItem(
                "avslutta3112 = '1', undersokelse with valid sluttDato, context without sluttDato",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = null,
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                sluttDato = dateInTest
                            )
                        )
                    )
                ),
                expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). Individet er avsluttet hos " +
                        "barnevernet og dets undersøkelser skal dermed være avsluttet. Sluttdato er $dateInTest"
            ),
            ForAllRowItem(
                "avslutta3112 = '1', undersokelse with sluttDato after reporting year",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = dateInTest,
                    melding = mutableListOf(
                        meldingTypeInTest.copy(
                            undersokelse = undersokelseTypeInTest.copy(
                                sluttDato = dateInTest.plusYears(1)
                            )
                        )
                    )
                ),
                expectedErrorMessage = "Undersøkelse (${undersokelseTypeInTest.id}). Individet er avsluttet hos " +
                        "barnevernet og dets undersøkelser skal dermed være avsluttet. Sluttdato er ${dateInTest.plusYears(1)}"
            )
        )
    )
})
