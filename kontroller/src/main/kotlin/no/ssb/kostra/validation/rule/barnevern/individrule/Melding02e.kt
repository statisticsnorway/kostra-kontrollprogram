package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Melding02e : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.MELDING_02E.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.melding
        .filter { it.startDato.isBefore(context.startDato) }
        .map { melding ->
            createValidationReportEntry(
                contextId = melding.id,
                messageText = "Melding (${melding.id}). Startdato (${melding.startDato}) skal være lik eller " +
                        "etter individets startdato (${context.startDato})"
            )
        }.ifEmpty { null }
}