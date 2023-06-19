package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isUtgift

class Rule110SummeringDriftDifferanse : AbstractRecordRule(
    "Kontroll 110 : Summeringskontroller driftsregnskapet, differanse i driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { !it.isOsloBydel() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.partition { it.isUtgift() }
        ?.let { (driftUtgifterPosteringer,
                    driftInntekterPosteringer) ->
            (
                    driftUtgifterPosteringer
                        .sumOf { it.getFieldAsIntegerDefaultEquals0(RegnskapConstants.FIELD_BELOP) }
                            to
                            driftInntekterPosteringer
                                .sumOf { it.getFieldAsIntegerDefaultEquals0(RegnskapConstants.FIELD_BELOP) }
                    )
        }
        ?.takeUnless { (driftUtgifter, driftInntekter) ->
            0 < driftUtgifter
                    && driftInntekter < 0
                    && driftUtgifter + driftInntekter in -30..30
        }
        ?.let { (driftUtgifter, driftInntekter) ->
            val driftDifferanse = driftUtgifter + driftInntekter
            createSingleReportEntryList(
                messageText = "Korrigér differansen ($driftDifferanse) mellom inntekter " +
                        "($driftInntekter) og utgifter ($driftUtgifter) i driftsregnskapet"
            )
        }
}