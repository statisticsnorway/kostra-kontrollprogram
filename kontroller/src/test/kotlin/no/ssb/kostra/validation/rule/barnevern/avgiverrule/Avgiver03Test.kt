package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest

class Avgiver03Test : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Avgiver03(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "avgiver with organisasjonsnummer",
                kostraAvgiverTypeInTest
            ),

            ForAllRowItem(
                "avgiver with empty organisasjonsnummer",
                kostraAvgiverTypeInTest.copy(organisasjonsnummer = ""),
                expectedErrorMessage = "Filen mangler organisasjonsnummer. Oppgitt organisasjonsnummer er"
            ),
            ForAllRowItem(
                "avgiver with blank organisasjonsnummer",
                kostraAvgiverTypeInTest.copy(organisasjonsnummer = "  "),
                expectedErrorMessage = "Filen mangler organisasjonsnummer. Oppgitt organisasjonsnummer er"
            )
        )
    )
})
