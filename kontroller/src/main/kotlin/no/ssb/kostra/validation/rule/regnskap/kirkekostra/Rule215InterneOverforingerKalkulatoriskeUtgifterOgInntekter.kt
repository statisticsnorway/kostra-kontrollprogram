package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule215InterneOverforingerKalkulatoriskeUtgifterOgInntekter : AbstractRule<List<KostraRecord>>(
    "Kontroll 215 : Interne overføringer, kalkulatoriske utgifter og inntekter",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isBevilgningRegnskap() }
        .takeIf { it.any() }
        ?.filter { it[FIELD_ART] in listOf("390", "790") }
        ?.partition { it[FIELD_ART] == "390" }
        ?.let { (utgifterPosteringer, inntekterPosteringer) ->
            Pair(
                utgifterPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                inntekterPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            )
        }?.takeUnless { (kalkulatoriskeUtgifter, kalkulatoriskeInntekter) ->
            (kalkulatoriskeUtgifter + kalkulatoriskeInntekter) in -30..30
        }?.let { (kalkulatoriskeUtgifter, kalkulatoriskeInntekter) ->
            val kalkulatoriskeDifferanse = kalkulatoriskeUtgifter + kalkulatoriskeInntekter
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differansen ($kalkulatoriskeDifferanse) mellom " +
                        "kalkulatoriske utgifter ($kalkulatoriskeUtgifter) og inntekter " +
                        "($kalkulatoriskeInntekter) ved kommunal tjenesteytelse stemmer overens " +
                        "(margin på +/- 30')"
            )
        }
}