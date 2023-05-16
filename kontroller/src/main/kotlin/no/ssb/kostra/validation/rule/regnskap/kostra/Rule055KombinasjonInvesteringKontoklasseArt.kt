package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.isBevilgningInvesteringRegnskap

class Rule055KombinasjonInvesteringKontoklasseArt(
    private val illogicalInvesteringArtList: List<String>
) : AbstractRecordRule(
    "Kontroll 055 : Ugyldig kombinasjon i investeringsregnskapet, kontoklasse og art",
    Severity.INFO
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningInvesteringRegnskap()
                    && kostraRecord.getFieldAsString(RegnskapConstants.FIELD_ART) in illogicalInvesteringArtList
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Kun advarsel, hindrer ikke innsending: (${kostraRecord.getFieldAsString(RegnskapConstants.FIELD_ART)}) regnes å være ulogisk art i investeringsregnskapet. Vennligst vurder å postere på annen art eller om posteringen hører til i driftsregnskapet.",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}