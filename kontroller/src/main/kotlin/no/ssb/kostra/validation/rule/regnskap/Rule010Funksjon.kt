package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KVARTAL
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isResultatRegnskap

class Rule010Funksjon(
    val funksjonList: List<String>
) : AbstractNoArgsRule<List<KostraRecord>>("Kontroll 010 : Funksjon", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>) =
        if (funksjonList.isEmpty()) null
        else context
            .filter { it.isBevilgningRegnskap() || it.isResultatRegnskap() }
            .filter { kostraRecord -> funksjonList.none { it.trim() == kostraRecord[FIELD_FUNKSJON].trim() } }
            .map { kostraRecord ->
                createValidationReportEntry(
                    messageText = "Fant ugyldig funksjon '${kostraRecord[FIELD_FUNKSJON]}'. " +
                            "Korrigér funksjon til en av '${funksjonList.joinToString(", ")}'",
                    lineNumbers = listOf(kostraRecord.lineNumber),
                    severity = if (kostraRecord[FIELD_KVARTAL].first() in setOf('1', '2', '3')) Severity.WARNING
                    else Severity.ERROR
                )
            }
            .ifEmpty { null }
}