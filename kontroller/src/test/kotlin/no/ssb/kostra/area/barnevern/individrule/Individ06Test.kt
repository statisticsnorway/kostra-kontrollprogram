package no.ssb.kostra.area.barnevern.individrule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraIndividInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraMeldingTypeInTes
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraPlanTypeInTest
import no.ssb.kostra.area.barnevern.individrule.IndividRuleTestData.kostraTiltakTypeInTest
import no.ssb.kostra.validation.report.Severity

class Individ06Test : BehaviorSpec({
    val sut = Individ06()

    Given("valid context") {
        forAll(
            row("individ with melding", kostraIndividInTest.copy(melding = mutableListOf(kostraMeldingTypeInTes))),
            row("individ with plan", kostraIndividInTest.copy(plan = mutableListOf(kostraPlanTypeInTest))),
            row("individ with tiltak", kostraIndividInTest.copy(tiltak = mutableListOf(kostraTiltakTypeInTest))),
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, IndividRuleTestData.argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        val invalidContext = kostraIndividInTest

        When("validate") {
            val reportEntryList = sut.validate(invalidContext, IndividRuleTestData.argumentsInTest)

            Then("expect non-null result") {
                reportEntryList.shouldNotBeNull()
                reportEntryList.size shouldBe 1

                assertSoftly(reportEntryList.first()) {
                    it.severity shouldBe Severity.ERROR
                    it.journalId shouldBe invalidContext.journalnummer
                    it.contextId shouldBe invalidContext.id
                    it.messageText shouldBe "Individet har ingen meldinger, planer eller tiltak i løpet av året"
                }
            }
        }
    }
})
