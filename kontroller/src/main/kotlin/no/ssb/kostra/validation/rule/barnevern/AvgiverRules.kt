package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.Avgiver02
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.Avgiver03
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.Avgiver04
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.Avgiver06

class AvgiverRules {

    fun validate(
        context: KostraAvgiverType,
        arguments: KotlinArguments
    ): List<ValidationReportEntry> = rules.mapNotNull { it.validate(context, arguments) }.flatten()

    private val rules = setOf(
        Avgiver02(),
        Avgiver03(),
        Avgiver04(),
        Avgiver06()
    )
}