package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule075KombinasjonBevilgningFunksjonArt : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 075 : Ugyldig kombinasjon i bevilgningsregnskapet, funksjon og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context.filter { kostraRecord ->
        kostraRecord.isBevilgningRegnskap()
                && kostraRecord[FIELD_ART] in qualifyingArtCodes
                && kostraRecord[FIELD_FUNKSJON].trim() != REQUIRED_FUNCTION
                && kostraRecord.fieldAsIntOrDefault(FIELD_BELOP) != 0
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Artene ${qualifyingArtCodes.joinToString(", ")} er kun tillat brukt i " +
                    "kombinasjon med funksjon 800.",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }

    companion object {
        internal val qualifyingArtCodes = setOf("870", "871", "872", "873", "875", "876")
        internal const val REQUIRED_FUNCTION = "800"
    }
}