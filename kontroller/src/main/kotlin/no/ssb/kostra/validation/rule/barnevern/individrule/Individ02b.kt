package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId
import java.time.LocalDate

class Individ02b : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.INDIVID_02B.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.run {
        val avgiverVersjon = arguments.aargang.toInt()
        val forrigeTelleDato = LocalDate.of(avgiverVersjon - 1, 12, 31)

        sluttDato
            ?.takeIf { sluttDato -> sluttDato.isBefore(forrigeTelleDato) }
            ?.let { sluttDato ->
                createSingleReportEntryList(
                    contextId = context.id,
                    messageText = "Individets sluttdato ($sluttDato) er før forrige telletidspunkt ($forrigeTelleDato)"
                )
            }
    }
}