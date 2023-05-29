package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Individ02a : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_02A.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.run {
        sluttDato
            ?.takeIf { sluttDato -> context.startDato.isAfter(sluttDato) }
            ?.let { sluttDato ->
                createSingleReportEntryList(
                    journalId = journalnummer,
                    contextId = context.id,
                    messageText = "Individets startdato ($startDato) er etter sluttdato ($sluttDato)",
                )
            }
    }
}