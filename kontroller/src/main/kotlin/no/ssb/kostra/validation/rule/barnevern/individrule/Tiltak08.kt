package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Tiltak08 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_08.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.tiltak
        .filter {
            it.opphevelse != null
                    && it.opphevelse.kode == "4"
                    && it.opphevelse.presisering == null
        }.map { tiltak ->
            createValidationReportEntry(
                contextId = tiltak.id,
                messageText = "Tiltak (${tiltak.id}). Tiltaksopphevelse (${tiltak.opphevelse!!.kode}) mangler presisering"
            )
        }.ifEmpty { null }
}
