package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isNotOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isUtgift

class Rule100SummeringDriftUtgiftsposteringer : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 100 : Summeringskontroller driftsregnskapet, utgiftsposteringer i driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isNotOsloBydel() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.isUtgift() }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { 0 < it }
        ?.let { sumDriftsUtgifter ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder utgiftsposteringene " +
                        "($sumDriftsUtgifter) i driftsregnskapet"
            )
        }
}