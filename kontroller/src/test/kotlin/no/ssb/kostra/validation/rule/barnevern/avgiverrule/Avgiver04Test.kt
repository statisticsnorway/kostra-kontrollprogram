package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest

class Avgiver04Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Avgiver04(),
            forAllRows = listOf(
                ForAllRowItem(
                    "avgiver with valid kommunenummer",
                    kostraAvgiverTypeInTest
                ),

                ForAllRowItem(
                    "avgiver with wrong kommunenummer",
                    kostraAvgiverTypeInTest.copy(kommunenummer = "4321"),
                    expectedErrorMessage = "Filen inneholder feil kommunenummer. Forskjellig kommunenummer " +
                            "i skjema og filuttrekk. 4321 : ${kostraAvgiverTypeInTest.kommunenummer}"
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
})
