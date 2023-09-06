package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import no.ssb.kostra.barnevern.xsd.KostraAvgiverType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.AvgiverRuleId

class Avgiver06 : AbstractRule<KostraAvgiverType>(
    AvgiverRuleId.AVGIVER_06.title,
    Severity.ERROR
) {
    override fun validate(context: KostraAvgiverType, arguments: KotlinArguments) = context
        .takeIf { context.kommunenavn.isBlank() }
        ?.let { createSingleReportEntryList(messageText = "Filen mangler kommunenavn.") }
}