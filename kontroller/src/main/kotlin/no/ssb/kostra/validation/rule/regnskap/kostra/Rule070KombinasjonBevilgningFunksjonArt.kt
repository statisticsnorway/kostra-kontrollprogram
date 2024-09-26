package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule070KombinasjonBevilgningFunksjonArt : AbstractRule<List<KostraRecord>>(
    "Kontroll 070 : Ugyldig kombinasjon i bevilgningsregnskapet, funksjon og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningRegnskap()
                    && kostraRecord[FIELD_FUNKSJON].trim() != "880"
                    && kostraRecord[FIELD_ART] == "530"
                    && kostraRecord.fieldAsIntOrDefault(FIELD_BELOP) != 0
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Art 530 er kun tillat brukt i kombinasjon med funksjon 880",
                lineNumbers = listOf(kostraRecord.lineNumber),
                severity = if (arguments.kvartal.first() in setOf('1', '2', '3')) Severity.WARNING else Severity.ERROR
            )
        }
        .ifEmpty { null }
}