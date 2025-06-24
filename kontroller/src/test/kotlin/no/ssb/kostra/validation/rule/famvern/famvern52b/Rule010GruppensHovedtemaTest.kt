package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule010GruppensHovedtemaTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule010GruppensHovedtema(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("01"),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest("XX"),
                expectedErrorMessage = "Det er ikke fylt ut hva som er hovedtema for behandlingen. " +
                        "Fant 'XX', forventet én av: [01=Samlivskurs, 02=Samlivsbrudd, " +
                        "03=Samarbeid om barn etter brudd, 04=Barn som har opplevd brudd i familien, " +
                        "05=Vold/overgrep, 06=Sinnemestring, 07=Kultur-/Minoritetsspørsmål, 08=Foreldreveiledning, " +
                        "09=Foreldre som har mistet omsorgen for egne barn, 10=Andre alvorlige hendelser, " +
                        "11=Annet, spesifiser].",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(Familievern52bColumnNames.HOVEDI_GR_B_COL_NAME to code)
            )
        )
    }
}