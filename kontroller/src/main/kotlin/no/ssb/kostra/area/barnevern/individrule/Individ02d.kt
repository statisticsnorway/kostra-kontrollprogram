package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Individ02d : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_02D.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context
        .takeIf { it.avslutta3112 == KOSTRA_IS_CLOSED_TRUE && it.sluttDato == null }
        ?.let { individ ->
            createSingleReportEntryList(
                journalId = individ.journalnummer,
                contextId = context.id,
                messageText = "Individet er avsluttet hos barnevernet og skal dermed være avsluttet. " +
                        "Sluttdato er ${individ.sluttDato}. Kode for avsluttet er '$KOSTRA_IS_CLOSED_TRUE'."
            )
        }
}