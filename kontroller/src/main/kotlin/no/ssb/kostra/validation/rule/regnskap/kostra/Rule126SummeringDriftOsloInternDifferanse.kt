package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloInternRegnskap

class Rule126SummeringDriftOsloInternDifferanse : AbstractRecordRule(
    "Kontroll 126 : Summeringskontroller driftsregnskapet for de Oslointerne artene 298 og 798, " +
            "differanse i driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { it.isOsloInternRegnskap() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { kostraRecord -> kostraRecord.getFieldAsString(FIELD_ART) in listOf("298", "798") }
        ?.partition { kostraRecord -> kostraRecord.getFieldAsString(FIELD_ART) == "298" }
        ?.let { (art298Posteringer, art798Posteringer) ->
            art298Posteringer.sumOf { it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) } to
                    art798Posteringer.sumOf { it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) }
        }
        ?.takeUnless { (sumArt298Drift, sumArt798Drift) -> sumArt298Drift + sumArt798Drift in -10..10 }
        ?.let { (sumArt298Drift, sumArt798Drift) ->
            val sumOslointerneDrift = sumArt298Drift + sumArt798Drift
            createSingleReportEntryList(
                messageText = "Korrigér differansen ($sumOslointerneDrift) mellom sum over alle funksjoner " +
                        "for art 298 ($sumArt298Drift) og sum over alle funksjoner for art 798 " +
                        "($sumArt798Drift) i driftsregnskapet."
            )
        }
}