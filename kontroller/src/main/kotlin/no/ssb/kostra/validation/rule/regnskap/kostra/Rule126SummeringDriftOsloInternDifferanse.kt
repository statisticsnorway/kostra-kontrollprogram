package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel

class Rule126SummeringDriftOsloInternDifferanse : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 126 : Summeringskontroller driftsregnskapet for de Oslointerne artene 298 og 798, " +
            "differanse i driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isOsloBydel() }
        .filter { it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { kostraRecord -> kostraRecord[FIELD_ART] in listOf("298", "798") }
        ?.partition { kostraRecord -> kostraRecord[FIELD_ART] == "298" }
        ?.let { (art298Posteringer, art798Posteringer) ->
            Pair(
                art298Posteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                art798Posteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            )
        }?.takeUnless { (sumArt298Drift, sumArt798Drift) -> sumArt298Drift + sumArt798Drift in -10..10 }
        ?.let { (sumArt298Drift, sumArt798Drift) ->
            val sumDifferanse = sumArt298Drift + sumArt798Drift
            createSingleReportEntryList(
                messageText = "Korrigér differansen ($sumDifferanse) mellom sum over alle funksjoner " +
                        "for art 298 ($sumArt298Drift) og sum over alle funksjoner for art 798 " +
                        "($sumArt798Drift) i driftsregnskapet."
            )
        }
}