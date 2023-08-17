package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import no.ssb.kostra.barnevern.xsd.KostraAvgiverType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.AvgiverRuleId

class Avgiver04 : AbstractRule<KostraAvgiverType>(
    AvgiverRuleId.AVGIVER_04.title,
    Severity.ERROR
) {
    override fun validate(context: KostraAvgiverType, arguments: KotlinArguments) = context
        .takeUnless { context.kommunenummer == arguments.region.substring(0, 4) }
        ?.let {
            createSingleReportEntryList(
                messageText = "Filen inneholder feil kommunenummer. Forskjellig kommunenummer i skjema og " +
                        "filuttrekk. ${context.kommunenummer} : ${arguments.region.substring(0, 4)}"
            )
        }
}