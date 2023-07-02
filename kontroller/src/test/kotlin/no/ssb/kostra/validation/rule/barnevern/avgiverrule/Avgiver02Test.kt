package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.BarnevernTestData.kostraAvgiverTypeInTest
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import java.time.Year

class Avgiver02Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Avgiver02(),
            forAllRows = listOf(
                ForAllRowItem(
                    "avgiver with valid version",
                    kostraAvgiverTypeInTest
                ),

                ForAllRowItem(
                    "avgiver with invalid version",
                    kostraAvgiverTypeInTest.copy(versjon = Year.now().value),
                    expectedErrorMessage = "Filen inneholder feil rapporteringsår (${Year.now().value}), " +
                            "forventet ${Year.now().value - 1}."
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )
})
