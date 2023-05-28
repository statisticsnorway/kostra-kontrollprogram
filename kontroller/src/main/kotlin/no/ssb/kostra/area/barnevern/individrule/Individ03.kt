package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.ValidationUtils.isValidSocialSecurityIdOrDnr
import no.ssb.kostra.area.barnevern.ValidationUtils.validateDUF
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Individ03 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_03.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.run {
        when {
            fodselsnummer != null -> when {
                isValidSocialSecurityIdOrDnr(fodselsnummer) -> null
                else -> createSingleReportEntryList(
                    journalId = journalnummer,
                    messageText = "Feil i fødselsnummer. Kan ikke identifisere individet."
                )
            }

            duFnummer != null -> when {
                validateDUF(duFnummer) -> null
                else -> createSingleReportEntryList(
                    journalId = journalnummer,
                    messageText = "DUF-nummer mangler. Kan ikke identifisere individet."
                )
            }

            else -> createSingleReportEntryList(
                journalId = journalnummer,
                messageText = "Fødselsnummer og DUF-nummer mangler. Kan ikke identifisere individet."
            )
        }
    }
}