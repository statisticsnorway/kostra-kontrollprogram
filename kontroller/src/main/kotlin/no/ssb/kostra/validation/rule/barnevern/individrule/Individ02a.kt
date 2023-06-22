package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Individ02a : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_02A.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.run {
        sluttDato
            ?.takeIf { sluttDato -> context.startDato.isAfter(sluttDato) }
            ?.let { sluttDato ->
                createSingleReportEntryList(
                    contextId = context.id,
                    messageText = "Individets startdato ($startDato) er etter sluttdato ($sluttDato)",
                )
            }
    }
}