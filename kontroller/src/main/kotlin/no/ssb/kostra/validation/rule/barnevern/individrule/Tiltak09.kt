package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId
import no.ssb.kostra.validation.rule.barnevern.extension.erPlasseringsTiltak
import no.ssb.kostra.validation.rule.barnevern.extension.isOverlapWithAtLeastThreeMonthsOf
import no.ssb.kostra.validation.rule.barnevern.extension.runDuplicateCheck
import java.time.LocalDate

class Tiltak09 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_09.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fallbackEndDate = LocalDate.of(arguments.aargang.toInt(), 12, 31)

        return context.tiltak.filter { it.erPlasseringsTiltak() }.runDuplicateCheck(
            areEqualFunc = { first, second ->
                first.isOverlapWithAtLeastThreeMonthsOf(
                    other = second,
                    fallbackEndDate = fallbackEndDate
                )
            },
            reportEntryProducerFunc = { first, _ ->
                createValidationReportEntry(
                    contextId = first.id,
                    messageText = "Plasseringstiltak kan ikke overlappe med mer enn 3 måneder"
                )
            })
    }
}
