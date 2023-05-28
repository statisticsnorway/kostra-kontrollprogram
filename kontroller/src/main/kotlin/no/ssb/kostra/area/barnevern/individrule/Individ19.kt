package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.ValidationUtils.validateDUF
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Individ19 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_19.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) =
        if (context.duFnummer != null && !validateDUF(context.duFnummer)) {
            createSingleReportEntryList(
                journalId = context.journalnummer,
                contextId = context.id,
                messageText = "Individet har ufullstendig DUF-nummer. Korriger DUF-nummer."
            )
        } else null
}