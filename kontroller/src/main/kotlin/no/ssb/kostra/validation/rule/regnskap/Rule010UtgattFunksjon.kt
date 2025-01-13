package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule010UtgattFunksjon(
    val funksjonList: List<String>,
) : AbstractNoArgsRule<List<KostraRecord>>(
        "Kontroll 010 : Utgått funksjon",
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        if (funksjonList.isEmpty()) {
            null
        } else {
            context
                .filter { it.isBevilgningRegnskap() }
                .filter { kostraRecord -> funksjonList.none { it.trim() == kostraRecord[FIELD_FUNKSJON].trim() } }
                .map { kostraRecord ->
                    createValidationReportEntry(
                        messageText =
                            "Fant utgått funksjon '${kostraRecord[FIELD_FUNKSJON]}'. " +
                                "Se kontoplan for gyldige funksjoner.",
                        lineNumbers = listOf(kostraRecord.lineNumber),
                    )
                }.ifEmpty { null }
        }
}
