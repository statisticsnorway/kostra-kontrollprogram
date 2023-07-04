package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap

class Rule040KombinasjonInvesteringKontoklasseFunksjon(
    private val invalidInvesteringFunksjonList: List<String>
) : AbstractRule<List<KostraRecord>>(
    "Kontroll 040 : Kombinasjon i investeringsregnskapet, kontoklasse og funksjon",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context.filter { kostraRecord ->
        kostraRecord.isBevilgningInvesteringRegnskap()
                && kostraRecord.fieldAsString(FIELD_FUNKSJON) in invalidInvesteringFunksjonList
                && kostraRecord.fieldAsIntOrDefault(FIELD_BELOP) != 0
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Korrigér ugyldig funksjon " +
                    "'${kostraRecord.fieldAsString(FIELD_FUNKSJON)}' i " +
                    "investeringsregnskapet til en gyldig funksjon i investeringsregnskapet eller overfør " +
                    "posteringen til driftsregnskapet.",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}