package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.barnevern.individrule.Individ01a.Companion.maxDate
import no.ssb.kostra.validation.rule.barnevern.individrule.Individ01a.Companion.minDate
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.flyttingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.meldingTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.planTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.undersokelseTypeInTest
import java.time.LocalDate

class Individ01aTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleWithArgsTest(
            sut = Individ01a(),
            expectedSeverity = Severity.ERROR,
            expectedContextId = individInTest.id,
            ForAllRowItem(
                "Sak with valid StartDato, expect no errors",
                individInTest(populateNullableDates = false)
            ),
            ForAllRowItem(
                "Sak with invalid StartDato, SluttDato",
                individInTest(invalidMinDate, invalidMaxDate),
                INDIVID_KOSTRA_ID to errorStr("Individ startdato", invalidMinDate),
                INDIVID_KOSTRA_ID to errorStr("Individ sluttdato", invalidMaxDate)
            ),
            ForAllRowItem(
                "Melding with invalid non-nullable dates",
                individInTest().copy(melding = mutableListOf(meldingInTest(populateNullableDates = false))),
                KOSTRA_MELDING_ID to errorStr("Melding startdato", invalidMinDate),
            ),
            ForAllRowItem(
                "Melding with invalid dates, all",
                individInTest().copy(melding = mutableListOf(meldingInTest())),
                KOSTRA_MELDING_ID to errorStr("Melding startdato", invalidMinDate),
                KOSTRA_MELDING_ID to errorStr("Melding sluttdato", invalidMaxDate),
                KOSTRA_UNDERSOKELSE_ID to errorStr("Undersokelse startdato", invalidMinDate),
                KOSTRA_UNDERSOKELSE_ID to errorStr("Undersokelse sluttdato", invalidMaxDate),

                ),
            ForAllRowItem(
                "Plan with invalid non-nullable dates",
                individInTest().copy(plan = mutableListOf(planInTest(populateNullableDates = false))),
                KOSTRA_PLAN_ID to errorStr("Plan startdato", invalidMinDate),
            ),
            ForAllRowItem(
                "Plan with invalid dates, all",
                individInTest().copy(plan = mutableListOf(planInTest())),
                KOSTRA_PLAN_ID to errorStr("Plan startdato", invalidMinDate),
                KOSTRA_PLAN_ID to errorStr("Plan sluttdato", invalidMaxDate),
                KOSTRA_PLAN_ID to errorStr("Plan evaluert dato", invalidMaxDate)
            ),
            ForAllRowItem(
                "Tiltak with invalid non-nullable dates",
                individInTest().copy(tiltak = mutableListOf(tiltakInTest(populateNullableDates = false))),
                KOSTRA_TILTAK_ID to errorStr("Tiltak startdato", invalidMinDate),
            ),
            ForAllRowItem(
                "Tiltak with invalid dates, all",
                individInTest().copy(tiltak = mutableListOf(tiltakInTest())),
                KOSTRA_TILTAK_ID to errorStr("Tiltak startdato", invalidMinDate),
                KOSTRA_TILTAK_ID to errorStr("Tiltak sluttdato", invalidMaxDate),
            ),
            ForAllRowItem(
                "Flytting with invalid dates, all",
                individInTest().copy(flytting = mutableListOf(flyttingInTest())),
                KOSTRA_FLYTTING_ID to errorStr("Flytting sluttdato", invalidMaxDate),
            ),
        )
    )
}) {
    companion object {

        private const val INDIVID_KOSTRA_ID = "C1"
        private const val KOSTRA_TILTAK_ID = "~tiltak~"
        private const val KOSTRA_MELDING_ID = "~melding~"
        private const val KOSTRA_UNDERSOKELSE_ID = "~undersokelse~"
        private const val KOSTRA_PLAN_ID = "~plan~"
        private const val KOSTRA_FLYTTING_ID = "~flytting~"

        private val invalidMinDate = minDate.minusDays(1)
        private val invalidMaxDate = maxDate.plusDays(1)

        private fun errorStr(field: String, invalidDate: LocalDate) =
            ("$field: Dato ($invalidDate) må være mellom $minDate og $maxDate)")

        private fun individInTest(
            minDate: LocalDate = Individ01a.minDate,
            maxDate: LocalDate = Individ01a.maxDate,
            populateNullableDates: Boolean = true
        ) = individInTest.copy(
            startDato = minDate,
            sluttDato = populateNullableDates.takeIf { it }?.let { maxDate }
        )

        private fun meldingInTest(
            minDate: LocalDate = invalidMinDate,
            maxDate: LocalDate = invalidMaxDate,
            populateNullableDates: Boolean = true
        ) = meldingTypeInTest.copy(
            startDato = minDate,
            sluttDato = populateNullableDates.takeIf { it }?.let { maxDate },
            undersokelse = populateNullableDates.takeIf { it }?.let {
                undersokelseInTest()
            }
        )

        private fun undersokelseInTest(
            minDate: LocalDate = invalidMinDate,
            maxDate: LocalDate = invalidMaxDate,
            populateNullableDates: Boolean = true
        ) = undersokelseTypeInTest.copy(
            startDato = minDate,
            sluttDato = populateNullableDates.takeIf { it }?.let { maxDate }
        )

        private fun planInTest(
            minDate: LocalDate = invalidMinDate,
            maxDate: LocalDate = invalidMaxDate,
            populateNullableDates: Boolean = true
        ) = planTypeInTest.copy(
            startDato = minDate,
            sluttDato = populateNullableDates.takeIf { it }?.let { maxDate },
            evaluertDato = populateNullableDates.takeIf { it }?.let { maxDate }
        )

        private fun tiltakInTest(
            minDate: LocalDate = invalidMinDate,
            maxDate: LocalDate = invalidMaxDate,
            populateNullableDates: Boolean = true
        ) = tiltakTypeInTest.copy(
            startDato = minDate,
            sluttDato = populateNullableDates.takeIf { it }?.let { maxDate }
        )

        private fun flyttingInTest(
            maxDate: LocalDate = invalidMaxDate
        ) = flyttingTypeInTest.copy(
            sluttDato = maxDate
        )
    }
}