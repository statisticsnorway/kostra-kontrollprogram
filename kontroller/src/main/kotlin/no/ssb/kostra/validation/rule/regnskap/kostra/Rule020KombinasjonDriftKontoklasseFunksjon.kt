package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap

class Rule020KombinasjonDriftKontoklasseFunksjon(
    private val invalidDriftFunksjonList: List<String>
) : AbstractRule<List<KostraRecord>>(
    "Kontroll 020 : Kombinasjon i driftsregnskapet, kontoklasse og funksjon",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context.filter { kostraRecord ->
        kostraRecord.isBevilgningDriftRegnskap()
                && kostraRecord[FIELD_FUNKSJON].trim() in invalidDriftFunksjonList.map { it.trim() }
                && kostraRecord.fieldAsIntOrDefault(FIELD_BELOP) != 0
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Korrigér ugyldig funksjon " +
                    "'${kostraRecord.fieldAsString(FIELD_FUNKSJON)}' i driftsregnskapet " +
                    "til en gyldig funksjon i driftsregnskapet eller overfør posteringen til " +
                    "investeringsregnskapet.",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}