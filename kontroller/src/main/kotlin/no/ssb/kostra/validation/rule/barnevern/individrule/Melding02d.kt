package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_TRUE
import java.time.LocalDate

/**
    Jon Ole: Det burde vært en test på individnivå om avslutta3112 == 1 og sluttDato == null
 */

class Melding02d : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.MELDING_02D.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context
        .takeIf { it.avslutta3112 == KOSTRA_IS_CLOSED_TRUE }
        ?.let { innerContext ->
            innerContext.melding.filter {
                it.sluttDato == null
                        || innerContext.sluttDato == null
                        || it.sluttDato.isAfter(LocalDate.of(arguments.aargang.toInt(), 12, 31))
            }.map { melding ->
                createValidationReportEntry(
                    journalId = innerContext.journalnummer,
                    contextId = melding.id,
                    messageText = "Melding (${melding.id}). Individet er avsluttet hos barnevernet og dets " +
                            "meldinger skal dermed være avsluttet. Sluttdato er ${melding.sluttDato ?: "uoppgitt"}"
                )
            }.ifEmpty { null }
        }
}