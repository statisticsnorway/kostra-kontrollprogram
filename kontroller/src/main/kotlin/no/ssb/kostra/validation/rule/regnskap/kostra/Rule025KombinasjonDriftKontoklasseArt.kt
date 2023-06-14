package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.isBevilgningDriftRegnskap

class Rule025KombinasjonDriftKontoklasseArt(
    private val invalidDriftArtList: List<String>
) : AbstractRecordRule(
    "Kontroll 025 : Kombinasjon i driftsregnskapet, kontoklasse og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningDriftRegnskap()
                    && kostraRecord.getFieldAsString(FIELD_ART) in invalidDriftArtList
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Korrigér ugyldig art '${kostraRecord.getFieldAsString(FIELD_ART)}' i driftsregnskapet " +
                        "til en gyldig art i driftsregnskapet eller overfør posteringen til investeringsregnskapet.",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}