package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Avgiver04 : AbstractRule<KostraAvgiverType>(
    AvgiverRuleId.AVGIVER_04.title,
    Severity.ERROR
) {
    override fun validate(context: KostraAvgiverType, arguments: Arguments) = context
        .takeUnless { context.kommunenummer == arguments.region.substring(0, 4) }
        ?.let {
            createSingleReportEntryList(
                messageText = "Filen inneholder feil kommunenummer. Forskjellig kommunenummer i skjema og " +
                        "filuttrekk.${context.kommunenummer} : ${arguments.region.substring(0, 4)}"
            )
        }
}