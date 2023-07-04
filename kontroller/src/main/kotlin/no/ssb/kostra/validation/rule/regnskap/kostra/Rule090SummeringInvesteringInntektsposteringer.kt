package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isInntekt
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isRegional

class Rule090SummeringInvesteringInntektsposteringer : AbstractRule<List<KostraRecord>>(
    "Kontroll 090 : Summeringskontroller investeringsregnskapet, inntektsposteringer i investeringsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { !it.isOsloBydel() && it.isRegional() && it.isBevilgningInvesteringRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.isInntekt() }
        ?.sumOf { it.fieldAsIntOrDefault(RegnskapConstants.FIELD_BELOP) }
        ?.takeUnless { 0 > it }
        ?.let { sumInvesteringsInntekter ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder inntektsposteringene " +
                        "($sumInvesteringsInntekter) i investeringsregnskapet"
            )
        }
}