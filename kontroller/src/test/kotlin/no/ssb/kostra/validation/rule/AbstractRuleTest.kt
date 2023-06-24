package no.ssb.kostra.validation.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class AbstractRuleTest : BehaviorSpec({

    Given("string impl of AbstractRule") {
        val sut = AbstractRule<String>("~ruleName~", Severity.WARNING)

        When("validate strings, expect null") {
            sut.validate("Hello, World!").shouldBeNull()
        }

        When("validate strings with KotlinArguments, expect null") {
            sut.validate("Hello, World!", argumentsInTest).shouldBeNull()
        }
    }
})
