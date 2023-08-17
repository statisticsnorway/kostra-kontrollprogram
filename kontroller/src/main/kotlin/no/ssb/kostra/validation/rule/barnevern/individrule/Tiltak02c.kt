package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Tiltak02c : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_02C.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context
        .takeIf { it.sluttDato != null }
        ?.let { innerContext ->
            innerContext.tiltak.filter {
                it.sluttDato?.let { sluttDato -> sluttDato.isAfter(context.sluttDato) } ?: false
            }.map { tiltak ->
                createValidationReportEntry(
                    contextId = tiltak.id,
                    messageText = "Tiltak (${tiltak.id}). Sluttdato (${tiltak.sluttDato}) er " +
                            "etter individets sluttdato (${context.sluttDato})"
                )
            }.ifEmpty { null }
        }
}