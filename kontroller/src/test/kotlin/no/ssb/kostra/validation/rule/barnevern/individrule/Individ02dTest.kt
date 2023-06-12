package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kostraIndividInTest

class Individ02dTest : BehaviorSpec({
    val sut = Individ02d()

    Given("valid context") {
        forAll(
            row("individ without endDate", kostraIndividInTest),
            row("individ with endDate, avslutta3112 = 2", kostraIndividInTest.copy(sluttDato = dateInTest.plusDays(1))),
            row(
                "individ with endDate, avslutta3112 = 1", kostraIndividInTest.copy(
                    sluttDato = dateInTest.plusDays(1),
                    avslutta3112 = KOSTRA_IS_CLOSED_TRUE
                )
            ),
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        val invalidContext = kostraIndividInTest.copy(avslutta3112 = KOSTRA_IS_CLOSED_TRUE)

        When("validate") {
            val reportEntryList = sut.validate(invalidContext, argumentsInTest)

            Then("expect non-null result") {
                reportEntryList.shouldNotBeNull()
                reportEntryList.size shouldBe 1

                assertSoftly(reportEntryList.first()) {
                    it.severity shouldBe Severity.ERROR
                    it.contextId shouldBe invalidContext.id
                    it.messageText shouldBe  "Individet er avsluttet hos barnevernet og skal dermed være avsluttet. " +
                            "Sluttdato er ${invalidContext.sluttDato}. Kode for avsluttet er '$KOSTRA_IS_CLOSED_TRUE'."
                }
            }
        }
    }
})
