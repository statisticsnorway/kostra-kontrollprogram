package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isInntekt
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isNotOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isRegional

class Rule105SummeringDriftInntektsposteringer : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 105 : Summeringskontroller driftsregnskapet, inntektsposteringer i driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isNotOsloBydel() && it.isRegional() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.isInntekt() }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { 0 > it }
        ?.let { sumDriftsInntekter ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder inntektsposteringene " +
                        "($sumDriftsInntekter) i driftsregnskapet"
            )
        }
}