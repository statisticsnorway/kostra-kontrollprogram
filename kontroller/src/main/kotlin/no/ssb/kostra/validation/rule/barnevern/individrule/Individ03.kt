package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.util.SsnValidationUtils.isValidSocialSecurityIdOrDnr
import no.ssb.kostra.program.util.SsnValidationUtils.validateDUF
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Individ03 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_03.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.run {
        when {
            fodselsnummer != null -> when {
                isValidSocialSecurityIdOrDnr(fodselsnummer!!) -> null
                else -> createSingleReportEntryList(
                    contextId = context.id,
                    messageText = "Feil i fødselsnummer. Kan ikke identifisere individet."
                )
            }

            duFnummer != null -> when {
                validateDUF(duFnummer!!) -> null
                else -> createSingleReportEntryList(
                    contextId = context.id,
                    messageText = "DUF-nummer mangler. Kan ikke identifisere individet."
                )
            }

            else -> createSingleReportEntryList(
                contextId = context.id,
                messageText = "Fødselsnummer og DUF-nummer mangler. Kan ikke identifisere individet."
            )
        }
    }
}