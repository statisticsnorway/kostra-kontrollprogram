package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.AGE_EIGHTTEEN

class Individ08 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_08.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.fodselsnummer
        ?.ageInYears(arguments.aargang.toInt())
        ?.takeIf { ageInYears -> ageInYears > AGE_EIGHTTEEN && context.tiltak.none() }
        ?.let {
            createSingleReportEntryList(
                contextId = context.id,
                messageText = "Individet er over 18 år og skal dermed ha tiltak"
            )
        }
}