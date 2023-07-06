package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isUtgift

class Rule110SummeringDriftDifferanse : AbstractRule<List<KostraRecord>>(
    "Kontroll 110 : Summeringskontroller driftsregnskapet, differanse i driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { it.isOsloBydel() }
        .filter { it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.partition { it.isUtgift() }
        ?.let { (driftUtgifterPosteringer, driftInntekterPosteringer) ->
            Pair(
                driftUtgifterPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                driftInntekterPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            ).takeUnless { (driftUtgifter, driftInntekter) -> areValidFigures(driftUtgifter, driftInntekter) }
                ?.let { (driftUtgifter, driftInntekter) ->
                    createSingleReportEntryList(
                        messageText = "Korrigér differansen (${driftUtgifter.plus(driftInntekter) }) mellom inntekter " +
                                "($driftInntekter) og utgifter ($driftUtgifter) i driftsregnskapet"
                    )
                }
        }

    companion object {
        internal fun areValidFigures(driftUtgifter: Int, driftInntekter: Int) =
            driftUtgifter > 0
                    && driftInntekter < 0
                    && driftUtgifter.plus(driftInntekter) in -30..30
    }
}