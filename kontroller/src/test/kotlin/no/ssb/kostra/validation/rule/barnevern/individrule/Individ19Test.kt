package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomDuf
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.barnevern.BarnevernTestFactory.barnevernValidationRuleTest
import no.ssb.kostra.validation.rule.barnevern.ForAllRowItem
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import java.time.Year

class Individ19Test : BehaviorSpec({
    include(
        barnevernValidationRuleTest(
            sut = Individ19(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without DUF-nummer",
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
                    "individ with invalid DUF-nummer", individInTest.copy(
                        fodselsnummer = null,
                        duFnummer = generateRandomDuf(Year.now().value - 1, Year.now().value - 1).take(10)
                    ),
                    true
                )
            ),
            expectedSeverity = Severity.WARNING,
            expectedErrorMessage = "Individet har ufullstendig DUF-nummer. Korriger DUF-nummer.",
            expectedContextId = individInTest.id
        )
    )
})
