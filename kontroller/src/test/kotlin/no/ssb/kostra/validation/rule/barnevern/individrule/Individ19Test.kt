package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomDuf
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import java.time.Year

class Individ19Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Individ19(),
            expectedSeverity = Severity.WARNING,
            expectedContextId = individInTest.id,
            ForAllRowItem(
                "individ without DUF-nummer",
                individInTest
            ),
            ForAllRowItem(
                "individ with valid DUF-nummer", individInTest.copy(
                    fodselsnummer = null,
                    duFnummer = generateRandomDuf(Year.now().value - 1, Year.now().value - 1)
                )
            ),
            ForAllRowItem(
                "individ with invalid DUF-nummer", individInTest.copy(
                    fodselsnummer = null,
                    duFnummer = generateRandomDuf(Year.now().value - 1, Year.now().value - 1).take(10)
                ),
                expectedErrorMessage = "Individet har ufullstendig DUF-nummer. Korriger DUF-nummer."
            )
        )
    )
})
