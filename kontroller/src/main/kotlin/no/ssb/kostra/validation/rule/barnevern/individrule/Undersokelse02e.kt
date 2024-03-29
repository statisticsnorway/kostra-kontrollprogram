package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Undersokelse02e : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.UNDERSOKELSE_02E.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.melding
        .mapNotNull { it.undersokelse }
        .filter { it.startDato.isBefore(context.startDato) }
        .map { undersokelse ->
            createValidationReportEntry(
                contextId = undersokelse.id,
                messageText = "Undersøkelse (${undersokelse.id}). StartDato (${undersokelse.startDato}) skal " +
                        "være lik eller etter startdato (${context.startDato}) på individet"
            )
        }.ifEmpty { null }
}