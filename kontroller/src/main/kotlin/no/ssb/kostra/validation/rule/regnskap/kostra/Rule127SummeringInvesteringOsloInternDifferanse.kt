package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloInternRegnskap

class Rule127SummeringInvesteringOsloInternDifferanse : AbstractRule<List<KostraRecord>>(
    "Kontroll 127 : Summeringskontroller investeringsregnskapet for de Oslointerne artene 298 og 798, " +
            "differanse i investeringsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { it.isOsloInternRegnskap() && it.isBevilgningInvesteringRegnskap() }
        .takeIf { it.any() }
        ?.filter { kostraRecord -> kostraRecord.getFieldAsString(RegnskapConstants.FIELD_ART) in listOf("298", "798") }
        ?.partition { kostraRecord -> kostraRecord.getFieldAsString(RegnskapConstants.FIELD_ART) == "298" }
        ?.let { (art298Posteringer, art798Posteringer) ->
            art298Posteringer.sumOf { it.getFieldAsIntegerOrDefault(RegnskapConstants.FIELD_BELOP) } to
                    art798Posteringer.sumOf { it.getFieldAsIntegerOrDefault(RegnskapConstants.FIELD_BELOP) }
        }
        ?.takeUnless { (sumArt298Investering, sumArt798Investering) ->
            sumArt298Investering + sumArt798Investering in -10..10
        }
        ?.let { (sumArt298Investering, sumArt798Investering) ->
            val sumOslointerneInvestering = sumArt298Investering + sumArt798Investering
            createSingleReportEntryList(
                messageText = "Korrigér differansen ($sumOslointerneInvestering) mellom sum over alle funksjoner " +
                        "for art 298 ($sumArt298Investering) og sum over alle funksjoner for art 798 " +
                        "($sumArt798Investering) i investeringsregnskapet."
            )
        }
}