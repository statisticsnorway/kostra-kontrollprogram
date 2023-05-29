package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE
import java.time.LocalDate

class Tiltak02d : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_02D.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context
        .takeIf { it.avslutta3112 == KOSTRA_IS_CLOSED_TRUE }
        ?.let { innerContext ->
            innerContext.tiltak.filter {
                it.sluttDato == null
                        || innerContext.sluttDato == null
                        || it.sluttDato.isAfter(LocalDate.of(arguments.aargang.toInt(), 12, 31))
            }.map { tiltak ->
                createValidationReportEntry(
                    journalId = innerContext.journalnummer,
                    contextId = tiltak.id,
                    messageText = "Tiltak (${tiltak.id}). Individet er avsluttet hos barnevernet og dets tiltak " +
                            "skal dermed være avsluttet. Sluttdato er ${tiltak.sluttDato ?: "uoppgitt"}"
                )
            }.ifEmpty { null }
        }
}