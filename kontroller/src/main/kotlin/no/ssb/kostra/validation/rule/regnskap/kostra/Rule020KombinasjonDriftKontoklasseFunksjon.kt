package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.isBevilgningDriftRegnskap

class Rule020KombinasjonDriftKontoklasseFunksjon(
    val invalidDriftFunksjonList: List<String>
) : AbstractRecordRule(
    "Kontroll 020 : Kombinasjon i driftsregnskapet, kontoklasse og funksjon",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningDriftRegnskap()
                    && kostraRecord.getFieldAsString(RegnskapConstants.FIELD_FUNKSJON) in invalidDriftFunksjonList
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = """Korrigér ugyldig funksjon '${kostraRecord.getFieldAsString(RegnskapConstants.FIELD_FUNKSJON)}' i driftsregnskapet til en gyldig funksjon i driftsregnskapet eller overfør posteringen til investeringsregnskapet.""".trimIndent(),
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}