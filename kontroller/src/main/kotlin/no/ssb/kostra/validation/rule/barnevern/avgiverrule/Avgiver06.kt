package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Avgiver06 : AbstractRule<KostraAvgiverType>(
    AvgiverRuleId.AVGIVER_06.title,
    Severity.ERROR
) {
    override fun validate(context: KostraAvgiverType, arguments: Arguments) = context
        .takeIf { context.kommunenavn.isBlank() }
        ?.let { createSingleReportEntryList(messageText = "Filen mangler kommunenavn.") }
}