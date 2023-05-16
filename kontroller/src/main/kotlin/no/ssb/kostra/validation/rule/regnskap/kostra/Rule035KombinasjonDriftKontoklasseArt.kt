package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.isBevilgningDriftRegnskap

class Rule035KombinasjonDriftKontoklasseArt(
    private val illogicalDriftArtList: List<String>
) : AbstractRecordRule(
    "Kontroll 035 : Kombinasjon i driftsregnskapet, kontoklasse og art",
    Severity.INFO
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningDriftRegnskap()
                    && kostraRecord.getFieldAsString(RegnskapConstants.FIELD_ART) in illogicalDriftArtList
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Kun advarsel, hindrer ikke innsending: (${
                    kostraRecord.getFieldAsString(
                        RegnskapConstants.FIELD_ART
                    )
                }) regnes å være ulogisk i driftsregnskapet, med mindre posteringen gjelder sosiale utlån og næringsutlån eller mottatte avdrag på sosiale utlån og næringsutlån, som finansieres av driftsinntekter.",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}

