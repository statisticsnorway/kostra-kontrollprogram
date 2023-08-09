package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.util.SsnValidationUtils.isValidSocialSecurityIdOrDnr
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Individ12 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_12.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) =
        if (context.fodselsnummer == null
            || !isValidSocialSecurityIdOrDnr(context.fodselsnummer)
        ) {
            createSingleReportEntryList(
                contextId = context.id,
                messageText = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
            )
        } else null
}