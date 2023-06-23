package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap

class Rule055KombinasjonInvesteringKontoklasseArt(
    private val illogicalInvesteringArtList: List<String>
) : AbstractRecordRule(
    "Kontroll 055 : Ugyldig kombinasjon i investeringsregnskapet, kontoklasse og art",
    Severity.INFO
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningInvesteringRegnskap()
                    && kostraRecord.getFieldAsString(FIELD_ART) in illogicalInvesteringArtList
                    && kostraRecord.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) != 0
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Kun advarsel, hindrer ikke innsending: " +
                        "(${kostraRecord.getFieldAsString(FIELD_ART)}) regnes å være ulogisk art " +
                        "i investeringsregnskapet. Vennligst vurder å postere på annen art eller om posteringen " +
                        "hører til i driftsregnskapet.",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}