package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.WARNING_QUARTERS
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule065KombinasjonBevilgningFunksjonArt :
    AbstractRule<List<KostraRecord>>(
        "Kontroll 065 : Ugyldig kombinasjon i bevilgningsregnskapet, funksjon og art",
        Severity.ERROR,
    ) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments,
    ) = context
        .filter {
            it.isBevilgningRegnskap() &&
                it.fieldAsIntOrDefault(
                    FIELD_BELOP,
                ) != 0
        }.filter { kostraRecord ->
            when (kostraRecord[FIELD_FUNKSJON].trim()) {
                "899" -> kostraRecord[FIELD_ART] !in artList
                else -> kostraRecord[FIELD_ART] in artList
            }
        }.map { kostraRecord ->
            createValidationReportEntry(
                messageText =
                    "Artene 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899. " +
                        "Og motsatt, funksjon 899 er kun tillat brukt i kombinasjon med artene 589, 980 og 989.",
                lineNumbers = listOf(kostraRecord.lineNumber),
                severity = if (arguments.kvartal.first() in WARNING_QUARTERS) Severity.WARNING else Severity.ERROR,
            )
        }.ifEmpty { null }

    companion object {
        private val artList = setOf("589", "980", "989")
    }
}
