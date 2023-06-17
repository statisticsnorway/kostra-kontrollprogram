package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap

class Rule040KombinasjonInvesteringKontoklasseFunksjon(
    private val invalidInvesteringFunksjonList: List<String>
) : AbstractRecordRule(
    "Kontroll 040 : Kombinasjon i investeringsregnskapet, kontoklasse og funksjon",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningInvesteringRegnskap()
                    && kostraRecord.getFieldAsString(RegnskapConstants.FIELD_FUNKSJON) in invalidInvesteringFunksjonList
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Korrigér ugyldig funksjon " +
                        "'${kostraRecord.getFieldAsString(RegnskapConstants.FIELD_FUNKSJON)}' i " +
                        "investeringsregnskapet til en gyldig funksjon i investeringsregnskapet eller overfør " +
                        "posteringen til driftsregnskapet.",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}