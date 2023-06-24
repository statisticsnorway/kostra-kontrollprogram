package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap

class Rule050KombinasjonInvesteringKontoklasseArt(
    private val invalidInvesteringArtList: List<String>
) : AbstractRecordRule(
    "Kontroll 050 : Ugyldig kombinasjon i investeringsregnskapet, kontoklasse og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningInvesteringRegnskap()
                    && kostraRecord.getFieldAsString(FIELD_ART) in invalidInvesteringArtList
                    && kostraRecord.getFieldAsIntegerOrDefault(FIELD_BELOP) != 0
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Korrigér ugyldig art '${kostraRecord.getFieldAsString(FIELD_ART)}' " +
                        "i investeringsregnskapet til en gyldig art i investeringsregnskapet eller overfør " +
                        "posteringen til driftsregnskapet.",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}