package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import java.time.LocalDate

class Individ02b : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_02B.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: Arguments) = context.run {
        val avgiverVersjon = arguments.aargang.toInt()
        val forrigeTelleDato = LocalDate.of(avgiverVersjon - 1, 12, 31)

        sluttDato
            ?.takeIf { sluttDato -> sluttDato.isBefore(forrigeTelleDato) }
            ?.let { sluttDato ->
                createSingleReportEntryList(
                    journalId = journalnummer,
                    messageText = "Individets sluttdato ($sluttDato) er før forrige telletidspunkt ($forrigeTelleDato)"
                )
            }
    }
}