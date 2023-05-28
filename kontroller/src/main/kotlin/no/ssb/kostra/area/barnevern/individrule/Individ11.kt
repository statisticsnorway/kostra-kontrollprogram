package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.ValidationUtils.isValidSocialSecurityId
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Individ11 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_11.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) =
        if (context.fodselsnummer == null
            || !isValidSocialSecurityId(context.fodselsnummer)
            || context.fodselsnummer.endsWith("55555")
        ) {
            createSingleReportEntryList(
                journalId = context.journalnummer,
                contextId = context.id,
                messageText = "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."
            )
        } else null
}