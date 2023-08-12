package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.testutil.RandomUtils.generateRandomDuf
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import java.time.Year

class Individ03Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Individ03(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = individInTest.id,
            ForAllRowItem(
                "individ with valid fodselsnummer",
                individInTest
            ),
            ForAllRowItem(
                "individ with valid DUF-nummer", individInTest.copy(
                    fodselsnummer = null,
                    duFnummer = generateRandomDuf(Year.now().value - 1, Year.now().value - 1)
                )
            ),
            ForAllRowItem(
                "individ with invalid fodselsnummer",
                individInTest.copy(fodselsnummer = "~fodselsnummer~"),
                expectedErrorMessage = "Feil i fødselsnummer. Kan ikke identifisere individet."
            ),
            ForAllRowItem(
                "individ with invalid DUF-nummer",
                individInTest.copy(
                    fodselsnummer = null,
                    duFnummer = "~duFnummer~"
                ),
                expectedErrorMessage = "DUF-nummer mangler. Kan ikke identifisere individet."
            ),
            ForAllRowItem(
                "individ with neither SSN nor DUF-nummer",
                individInTest.copy(
                    fodselsnummer = null,
                    duFnummer = null
                ),
                expectedErrorMessage = "Fødselsnummer og DUF-nummer mangler. Kan ikke identifisere individet."
            )
        )
    )
})
