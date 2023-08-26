package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isUtgift

class Rule110SummeringDriftDifferanse : AbstractRule<List<KostraRecord>>(
    "Kontroll 110 : Summeringskontroller driftsregnskapet, differanse i driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot { it.isOsloBydel() }
        .filter { it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.partition { it.isUtgift() }
        ?.let { (driftUtgifterPosteringer, driftInntekterPosteringer) ->
            Pair(
                driftUtgifterPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                driftInntekterPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            ).takeUnless { it.areValidFigures() }?.let { (driftUtgifter, driftInntekter) ->
                createSingleReportEntryList(
                    messageText = "Korrigér differansen (${driftUtgifter.plus(driftInntekter)}) mellom inntekter " +
                            "($driftInntekter) og utgifter ($driftUtgifter) i driftsregnskapet"
                )
            }
        }

    companion object {

        internal fun Pair<Int, Int>.areValidFigures() = this.let { (utgifter, inntekter) ->
            utgifter > 0
                    && inntekter < 0
                    && utgifter.plus(inntekter) in -30..30
        }
    }
}