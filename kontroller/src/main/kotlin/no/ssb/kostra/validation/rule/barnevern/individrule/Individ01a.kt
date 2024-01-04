package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.*
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId
import java.time.LocalDate

class Individ01a : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_01A.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = with(context) {
        melding.asSequence().map { validateMelding(it) }
            .plus(plan.map { validatePlan(it) })
            .plus(tiltak.map { validateTiltak(it) })
            .plus(flytting.map { validateFlytting(it) })
            .plus(listOf(validateIndivid(this)))
            .flatten().toList().ifEmpty { null }
    }

    private fun validateIndivid(individ: KostraIndividType) =
        emptyList<ValidationReportEntry>()
            .plusIfNotNull(
                validateDate(
                    dateToCheck = individ.startDato,
                    contextId = individ.id,
                    errorText = "Individ startdato"
                )
            )
            .plusIfNotNull(
                validateDate(
                    dateToCheck = individ.sluttDato,
                    contextId = individ.id,
                    errorText = "Individ sluttdato"
                )
            )

    private fun validateMelding(melding: KostraMeldingType) =
        emptyList<ValidationReportEntry>()
            .plusIfNotNull(
                validateDate(
                    dateToCheck = melding.startDato,
                    contextId = melding.id,
                    errorText = "Melding startdato"
                )
            )
            .plusIfNotNull(
                validateDate(
                    dateToCheck = melding.sluttDato,
                    contextId = melding.id,
                    errorText = "Melding sluttdato"
                )
            )
            .plusIfNotNull(
                melding.undersokelse?.let {
                    validateUndersokelse(it)
                }
            )

    private fun validateUndersokelse(undersokelse: KostraUndersokelseType) =
        emptyList<ValidationReportEntry>()
            .plusIfNotNull(
                validateDate(
                    dateToCheck = undersokelse.startDato,
                    contextId = undersokelse.id,
                    errorText = "Undersokelse startdato"
                )
            )
            .plusIfNotNull(
                validateDate(
                    dateToCheck = undersokelse.sluttDato,
                    contextId = undersokelse.id,
                    errorText = "Undersokelse sluttdato"
                )
            )


    private fun validatePlan(plan: KostraPlanType) =
        emptyList<ValidationReportEntry>()
            .plusIfNotNull(
                validateDate(
                    dateToCheck = plan.startDato,
                    contextId = plan.id,
                    errorText = "Plan startdato"
                )
            )
            .plusIfNotNull(
                validateDate(
                    dateToCheck = plan.sluttDato,
                    contextId = plan.id,
                    errorText = "Plan sluttdato"
                )
            )
            .plusIfNotNull(
                validateDate(
                    dateToCheck = plan.evaluertDato,
                    contextId = plan.id,
                    errorText = "Plan evaluert dato"
                )
            )

    private fun validateTiltak(tiltak: KostraTiltakType) =
        emptyList<ValidationReportEntry>()
            .plusIfNotNull(
                validateDate(
                    dateToCheck = tiltak.startDato,
                    contextId = tiltak.id,
                    errorText = "Tiltak startdato"
                )
            )
            .plusIfNotNull(
                validateDate(
                    dateToCheck = tiltak.sluttDato,
                    contextId = tiltak.id,
                    errorText = "Tiltak sluttdato"
                )
            )

    private fun validateFlytting(flytting: KostraFlyttingType) =
        emptyList<ValidationReportEntry>()
            .plusIfNotNull(
                validateDate(
                    dateToCheck = flytting.sluttDato,
                    contextId = flytting.id,
                    errorText = "Flytting sluttdato"
                )
            )

    private fun validateDate(
        dateToCheck: LocalDate?,
        contextId: String,
        errorText: String
    ): ValidationReportEntry? = dateToCheck
        ?.takeIf { dateToCheck.isBefore(minDate) || dateToCheck.isAfter(maxDate) }
        ?.let {
            createValidationReportEntry(
                messageText = errorText.plus(": Dato ($dateToCheck) må være mellom $minDate og $maxDate)"),
                contextId = contextId
            )
        }

    companion object {
        val minDate: LocalDate = LocalDate.of(1998, 1, 1)
        val maxDate: LocalDate = LocalDate.of(2049, 12, 31)

        fun List<ValidationReportEntry>.plusIfNotNull(other: ValidationReportEntry?) =
            if (other != null) this.plus(other) else this

        fun List<ValidationReportEntry>.plusIfNotNull(otherList: List<ValidationReportEntry?>?) =
            if (otherList != null) this.plus(otherList.filterNotNull()) else this
    }
}