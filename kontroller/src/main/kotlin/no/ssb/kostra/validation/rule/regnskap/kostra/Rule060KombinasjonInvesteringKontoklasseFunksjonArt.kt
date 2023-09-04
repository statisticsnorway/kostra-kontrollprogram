package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap

class Rule060KombinasjonInvesteringKontoklasseFunksjonArt : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 060 : Ugyldig kombinasjon i investeringsregnskapet, kontoklasse, funksjon og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context.filter { kostraRecord ->
        kostraRecord.isBevilgningInvesteringRegnskap()
                && kostraRecord[FIELD_ART] == "729"
                && kostraRecord[FIELD_FUNKSJON].trim() != "841"
                && kostraRecord.fieldAsIntOrDefault(FIELD_BELOP) != 0
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Korrigér til riktig kombinasjon av kontoklasse, funksjon og art. Art 729 er kun " +
                    "gyldig i kombinasjon med funksjon 841 i investeringsregnskapet.",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}