package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.WARNING_QUARTERS
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap

class Rule025KombinasjonDriftKontoklasseArt(
    private val invalidDriftArtList: List<String>,
) : AbstractRule<List<KostraRecord>>(
        "Kontroll 025 : Kombinasjon i driftsregnskapet, kontoklasse og art",
        Severity.ERROR,
    ) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments,
    ) = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningDriftRegnskap() &&
                kostraRecord[FIELD_ART] in invalidDriftArtList &&
                kostraRecord.fieldAsIntOrDefault(FIELD_BELOP) != 0
        }.map { kostraRecord ->
            createValidationReportEntry(
                messageText =
                    "Korrigér ugyldig art '${kostraRecord[FIELD_ART]}' i driftsregnskapet " +
                        "til en gyldig art i driftsregnskapet eller overfør posteringen til investeringsregnskapet.",
                lineNumbers = listOf(kostraRecord.lineNumber),
                severity = if (arguments.kvartal.first() in WARNING_QUARTERS) Severity.WARNING else Severity.ERROR,
            )
        }.ifEmpty { null }
}
