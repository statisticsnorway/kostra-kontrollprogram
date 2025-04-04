package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isFylkeRegnskap

class Rule081KombinasjonBevilgningFunksjonArt :
    AbstractRule<List<KostraRecord>>(
        "Kontroll 081 : Ugyldig kombinasjon i bevilgningsregnskapet, funksjon og art",
        Severity.ERROR,
    ) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments,
    ) = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningRegnskap() &&
                !kostraRecord.isFylkeRegnskap() &&
                kostraRecord[RegnskapConstants.FIELD_FUNKSJON].trim() == REQUIRED_FUNCTION &&
                kostraRecord[RegnskapConstants.FIELD_ART] !in qualifyingArtCodes &&
                kostraRecord.fieldAsIntOrDefault(RegnskapConstants.FIELD_BELOP) != 0
        }.map { kostraRecord ->
            createValidationReportEntry(
                messageText =
                    "Det er kun art 450 og art 810 som er logiske i kombinasjon med funksjon 850. " +
                        "Andre arter er ulogiske i kombinasjon med funksjon 850.",
                lineNumbers = listOf(kostraRecord.lineNumber),
                severity = if (arguments.kvartal.first() in RegnskapConstants.WARNING_QUARTERS) Severity.WARNING else Severity.ERROR,
            )
        }.ifEmpty { null }

    companion object {
        internal val qualifyingArtCodes = setOf("450", "810")
        internal const val REQUIRED_FUNCTION = "850"
    }
}
