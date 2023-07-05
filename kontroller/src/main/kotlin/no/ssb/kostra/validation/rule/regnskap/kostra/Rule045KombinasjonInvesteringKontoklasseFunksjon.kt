package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap

class Rule045KombinasjonInvesteringKontoklasseFunksjon(
    private val illogicalInvesteringFunksjonArtList: List<String>
) : AbstractRule<List<KostraRecord>>(
    "Kontroll 045 : Kombinasjon i investeringsregnskapet, kontoklasse og funksjon",
    Severity.INFO
) {
    override fun validate(context: List<KostraRecord>) = context.filter { kostraRecord ->
        kostraRecord.isBevilgningInvesteringRegnskap()
                && kostraRecord.fieldAsTrimmedString(FIELD_FUNKSJON) in illogicalInvesteringFunksjonArtList.map { it.trim() }
                && kostraRecord.fieldAsIntOrDefault(FIELD_BELOP) != 0
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Kun advarsel, hindrer ikke innsending: " +
                    "(${kostraRecord.fieldAsString(FIELD_FUNKSJON)}) regnes å være " +
                    "ulogisk funksjon i investeringsregnskapet. Vennligst vurder å postere på annen " +
                    "funksjon eller om posteringen hører til i driftsregnskapet.",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}
