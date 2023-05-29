package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.SharedValidationConstants.AGE_TEN
import no.ssb.kostra.area.barnevern.extension.ageInYears
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Tiltak06 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_06.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.fodselsnummer
        ?.ageInYears(arguments.aargang.toInt())
        ?.takeIf { it > AGE_TEN }
        ?.let { ageInYears ->
            context.tiltak
                .filter { it.kategori.kode == "4.2" }
                .map { tiltak ->
                    createReportEntry(
                        journalId = context.journalnummer,
                        contextId = tiltak.id,
                        messageText = "Tiltak (${tiltak.id}). Barnet er over 11 år og i SFO. " +
                                "Barnets alder er $ageInYears år"
                    )
                }.ifEmpty { null }
        }
}