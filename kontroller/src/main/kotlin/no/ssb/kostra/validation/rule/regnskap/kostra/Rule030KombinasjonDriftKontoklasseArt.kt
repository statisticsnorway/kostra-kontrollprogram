package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.isBevilgningDriftRegnskap

class Rule030KombinasjonDriftKontoklasseArt(
    private val illogicalDriftArtList: List<String>
) : AbstractRecordRule(
    "Kontroll 030 : Kombinasjon i driftsregnskapet, kontoklasse og art",
    Severity.INFO
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningDriftRegnskap()
                    && kostraRecord.getFieldAsString(RegnskapConstants.FIELD_ART) in illogicalDriftArtList
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Kun advarsel, hindrer ikke innsending: ('${
                    kostraRecord.getFieldAsString(
                        RegnskapConstants.FIELD_ART
                    )
                }') regnes å være ulogisk art i driftsregnskapet. Vennligst vurder å postere på annen art eller om posteringen hører til i investeringsregnskapet.",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}