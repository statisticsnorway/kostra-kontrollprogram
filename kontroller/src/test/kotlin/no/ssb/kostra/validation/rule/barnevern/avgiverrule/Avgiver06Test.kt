package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest

class Avgiver06Test : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Avgiver06(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "avgiver with kommunenavn",
                kostraAvgiverTypeInTest
            ),
            ForAllRowItem(
                "avgiver with empty kommunenavn",
                kostraAvgiverTypeInTest.copy(kommunenavn = ""),
                expectedErrorMessage = "Filen mangler kommunenavn."
            ),
            ForAllRowItem(
                "avgiver with blank kommunenavn",
                kostraAvgiverTypeInTest.copy(kommunenavn = "   "),
                expectedErrorMessage = "Filen mangler kommunenavn."
            )
        )
    )
})
