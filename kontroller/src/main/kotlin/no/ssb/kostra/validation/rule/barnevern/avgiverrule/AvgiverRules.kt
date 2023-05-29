package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.ValidationReportEntry

class AvgiverRules {

    fun validate(
        context: KostraAvgiverType,
        arguments: Arguments
    ): List<ValidationReportEntry> = rules.mapNotNull { it.validate(context, arguments) }.flatten()

    private val rules = setOf(
        Avgiver01(),
        Avgiver02(),
        Avgiver03(),
        Avgiver04(),
        Avgiver06()
    )
}