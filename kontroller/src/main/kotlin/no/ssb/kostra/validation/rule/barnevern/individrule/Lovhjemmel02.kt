package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId
import no.ssb.kostra.validation.rule.barnevern.extension.erOmsorgsTiltak

class Lovhjemmel02 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.LOVHJEMMEL_02.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.tiltak.filter {
        it.erOmsorgsTiltak()
                && it.sluttDato != null
                && it.opphevelse == null
    }.map { tiltak ->
        createValidationReportEntry(
            contextId = tiltak.id,
            messageText = "Lovhjemmel Kontroll 2: Omsorgstiltak med sluttdato krever årsak til opphevelse"
        )
    }.ifEmpty { null }
}