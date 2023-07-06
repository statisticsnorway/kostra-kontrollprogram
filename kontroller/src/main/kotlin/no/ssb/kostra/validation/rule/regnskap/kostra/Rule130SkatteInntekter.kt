package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isLongyearbyen
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isRegional

class Rule130SkatteInntekter : AbstractRule<List<KostraRecord>>(
    "Kontroll 130 : Skatteinntekter",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { it.isOsloBydel() || it.isLongyearbyen() }
        .filter { it.isRegional() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { it[FIELD_FUNKSJON] == "800" && it[FIELD_ART] == "870" }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { skatteInntekter -> skatteInntekter < 0 }
        ?.let { skatteInntekter ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder skatteinntekter ($skatteInntekter)."
            )
        }
}