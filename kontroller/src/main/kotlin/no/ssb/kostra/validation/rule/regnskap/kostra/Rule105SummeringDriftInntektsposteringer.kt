package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isInntekt
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isRegional

class Rule105SummeringDriftInntektsposteringer : AbstractRecordRule(
    "Kontroll 105 : Summeringskontroller driftsregnskapet, inntektsposteringer i driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { !it.isOsloBydel() && it.isRegional() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.isInntekt() }
        ?.sumOf { it.getFieldAsIntegerOrDefault(RegnskapConstants.FIELD_BELOP) }
        ?.takeUnless { 0 > it }
        ?.let { sumDriftsInntekter ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder inntektsposteringene " +
                        "($sumDriftsInntekter) i driftsregnskapet"
            )
        }
}