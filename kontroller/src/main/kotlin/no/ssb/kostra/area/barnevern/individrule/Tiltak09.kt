package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.extension.erPlasseringsTiltak
import no.ssb.kostra.area.barnevern.extension.isOverlapWithAtLeastThreeMonthsOf
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import java.time.LocalDate

class Tiltak09 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_09.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.tiltak
        .filter { it.erPlasseringsTiltak() }
        .takeIf {
            /** 2 measures or more are required for determining overlaps */
            it.size > 1
        }?.let { measures ->

            val fallbackEndDate = LocalDate.of(arguments.aargang.toInt(), 12, 31)
            val unprocessedMeasures = ArrayDeque(measures.sortedBy { it.startDato })

            mutableListOf<ValidationReportEntry>().apply {
                do {
                    addAll(
                        unprocessedMeasures.removeFirst().let { outerMeasure ->
                            unprocessedMeasures
                                .filter { innerMeasure ->
                                    outerMeasure.isOverlapWithAtLeastThreeMonthsOf(
                                        innerMeasure,
                                        fallbackEndDate
                                    )
                                }.map { innerMeasure ->
                                    createReportEntry(
                                        journalId = context.journalnummer,
                                        contextId = innerMeasure.id,
                                        messageText = "Plasseringstiltak kan ikke overlappe med mer enn 3 måneder"
                                    )
                                }
                        })
                } while (unprocessedMeasures.size > 1)
            }.ifEmpty { null }
        }
}
