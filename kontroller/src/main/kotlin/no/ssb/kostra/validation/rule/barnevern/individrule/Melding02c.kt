package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Melding02c : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.MELDING_02C.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context
        .takeIf { it.sluttDato != null }
        ?.let { innerContext ->
            innerContext.melding.filter {
                it.sluttDato != null
                        && it.sluttDato.isAfter(context.sluttDato)
                        && it.konklusjon != HENLAGT
            }.map { melding ->
                createValidationReportEntry(
                    contextId = melding.id,
                    messageText = "Melding (${melding.id}). Meldingens sluttdato (${melding.sluttDato}) " +
                            "er etter individets sluttdato (${innerContext.sluttDato})"
                )
            }.ifEmpty { null }
        }

        companion object{
            const val HENLAGT = "1"
        }
}