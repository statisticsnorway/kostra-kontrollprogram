package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.planTypeInTest

class Plan02dTest : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Plan02d(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = planTypeInTest.id,
            ForAllRowItem(
                "individ with avslutta3112 = '2'",
                individInTest
            ),
            ForAllRowItem(
                "avslutta3112 = '1', no plan", individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE
                )
            ),
            ForAllRowItem(
                "avslutta3112 = '1', plan with sluttDato",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = dateInTest.minusYears(1).plusDays(1),
                    plan = mutableListOf(
                        planTypeInTest.copy(
                            sluttDato = dateInTest.minusYears(1).plusDays(1)
                        )
                    )
                )
            ),
            ForAllRowItem(
                "avslutta3112 = '1', plan without sluttDato",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = dateInTest.minusYears(1).plusDays(1),
                    plan = mutableListOf(
                        planTypeInTest.copy(
                            sluttDato = null
                        )
                    )
                ),
                expectedErrorMessage = "Plan (${planTypeInTest.id}). Individet er avsluttet hos barnevernet og " +
                        "dets planer skal dermed være avsluttet. Sluttdato er uoppgitt"
            ),
            ForAllRowItem(
                "avslutta3112 = '1', plan with valid sluttDato, context without sluttDato",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = null,
                    plan = mutableListOf(
                        planTypeInTest.copy(
                            sluttDato = dateInTest.minusYears(1).plusDays(1)
                        )
                    )
                ),
                expectedErrorMessage = "Plan (${planTypeInTest.id}). Individet er avsluttet hos barnevernet og " +
                        "dets planer skal dermed være avsluttet. " +
                        "Sluttdato er ${dateInTest.minusYears(1).plusDays(1)}"
            ),
            ForAllRowItem(
                "avslutta3112 = '1', plan with sluttDato after reporting year",
                individInTest.copy(
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE,
                    sluttDato = dateInTest.minusYears(1).plusDays(1),
                    plan = mutableListOf(
                        planTypeInTest.copy(
                            sluttDato = dateInTest
                        )
                    )
                ),
                expectedErrorMessage = "Plan (${planTypeInTest.id}). Individet er avsluttet hos barnevernet og " +
                        "dets planer skal dermed være avsluttet. Sluttdato er $dateInTest"
            )
        )
    )
})
