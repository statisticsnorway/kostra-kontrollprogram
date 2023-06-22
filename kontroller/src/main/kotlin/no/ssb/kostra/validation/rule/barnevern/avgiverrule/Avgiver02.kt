package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.AvgiverRuleId

class Avgiver02 : AbstractRule<KostraAvgiverType>(
    AvgiverRuleId.AVGIVER_02.title,
    Severity.ERROR
) {
    override fun validate(context: KostraAvgiverType, arguments: KotlinArguments) = context
        .takeUnless { context.versjon == arguments.aargang.toInt() }
        ?.let {
            createSingleReportEntryList(
                messageText = "Filen inneholder feil rapporteringsår (${context.versjon}), forventet ${arguments.aargang}.",
            )
        }
}