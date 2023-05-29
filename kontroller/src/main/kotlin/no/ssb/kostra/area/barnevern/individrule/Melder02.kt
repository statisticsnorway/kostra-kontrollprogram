package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Melder02 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.MELDER_02.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.melding.flatMap { melding ->
        melding.melder.filter { melder ->
            melder.kode == ANDRE_OFFENTLIGE_INSTANSER
                    && melder.presisering.isNullOrEmpty()
        }.map { melder ->
            createReportEntry(
                journalId = context.journalnummer,
                contextId = melding.id,
                messageText = "Melder med kode (${melder.kode}) mangler presisering"
            )
        }
    }.ifEmpty { null }

    companion object {
        /** 22 = Andre offentlige instanser */
        const val ANDRE_OFFENTLIGE_INSTANSER = "22"
    }
}
