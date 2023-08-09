package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.util.SsnValidationUtils.validateDUF
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Individ19 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_19.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) =
        if (context.duFnummer != null && !validateDUF(context.duFnummer)) {
            createSingleReportEntryList(
                contextId = context.id,
                messageText = "Individet har ufullstendig DUF-nummer. Korriger DUF-nummer."
            )
        } else null
}