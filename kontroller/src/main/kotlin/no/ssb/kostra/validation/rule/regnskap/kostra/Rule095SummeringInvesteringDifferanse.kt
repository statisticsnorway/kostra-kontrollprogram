package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isUtgift

class Rule095SummeringInvesteringDifferanse : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 095 : Summeringskontroller investeringsregnskapet, differanse i investeringsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { !it.isOsloBydel() }
        .filter { it.isBevilgningInvesteringRegnskap() }
        .takeIf { it.any() }
        ?.partition { it.isUtgift() }
        ?.let { (investeringUtgifterPosteringer, investeringInntekterPosteringer) ->
            Pair(
                investeringUtgifterPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                investeringInntekterPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            )
        }
        ?.takeUnless { (investeringUtgifter, investeringInntekter) ->
            (investeringUtgifter + investeringInntekter) in -30..30
        }?.let { (investeringUtgifter, investeringInntekter) ->
            val investeringDifferanse = investeringUtgifter + investeringInntekter
            createSingleReportEntryList(
                messageText = "Korrigér differansen ($investeringDifferanse) mellom inntekter " +
                        "($investeringInntekter) og utgifter ($investeringUtgifter) i investeringsregnskapet"
            )
        }

}