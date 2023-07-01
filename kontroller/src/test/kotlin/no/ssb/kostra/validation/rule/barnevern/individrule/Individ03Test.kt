package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomDuf
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import java.time.Year

class Individ03Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ03(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ with valid fodselsnummer",
                    individInTest,
                    false
                ),
                ForAllRowItem(
                    "individ with valid DUF-nummer", individInTest.copy(
                        fodselsnummer = null,
                        duFnummer = generateRandomDuf(Year.now().value - 1, Year.now().value - 1)
                    ),
                    false
                ),

                ForAllRowItem(
                    "individ with invalid fodselsnummer",
                    individInTest.copy(fodselsnummer = "~fodselsnummer~"),
                    true,
                    "Feil i fødselsnummer. Kan ikke identifisere individet."
                ),
                ForAllRowItem(
                    "individ with invalid DUF-nummer",
                    individInTest.copy(
                        fodselsnummer = null,
                        duFnummer = "~duFnummer~"
                    ),
                    true,
                    "DUF-nummer mangler. Kan ikke identifisere individet."
                ),
                ForAllRowItem(
                    "individ with neither SSN nor DUF-nummer",
                    individInTest.copy(
                        fodselsnummer = null,
                        duFnummer = null
                    ),
                    true,
                    "Fødselsnummer og DUF-nummer mangler. Kan ikke identifisere individet."
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedErrorMessage = "N/A",
            expectedContextId = individInTest.id
        )
    )
})
