package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.ValidationUtils.isValidSocialSecurityIdOrDnr

class Individ12 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_12.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) =
        if (context.fodselsnummer == null
            || !isValidSocialSecurityIdOrDnr(context.fodselsnummer)
        ) {
            createSingleReportEntryList(
                journalId = context.journalnummer,
                contextId = context.id,
                messageText = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
            )
        } else null
}