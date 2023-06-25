package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.AGE_SEVENTEEN
import no.ssb.kostra.validation.rule.barnevern.extension.erOmsorgsTiltak

class Lovhjemmel03 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.LOVHJEMMEL_03.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.fodselsnummer
        ?.ageInYears(arguments.aargang.toInt())
        ?.takeIf { ageInYears -> ageInYears > AGE_SEVENTEEN && context.tiltak.any { it.erOmsorgsTiltak() }
        }?.let { ageInYears ->
            context.tiltak
                .filter { it.erOmsorgsTiltak() }
                .map {
                    createValidationReportEntry(
                        contextId = it.id,
                        messageText = "Tiltak (${it.id}). Individet er $ageInYears år og skal dermed ikke ha omsorgstiltak"
                    )
                }
        }
}