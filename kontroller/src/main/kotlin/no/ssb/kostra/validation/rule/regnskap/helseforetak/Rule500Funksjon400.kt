package no.ssb.kostra.validation.rule.regnskap.helseforetak

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isResultatRegnskap

class Rule500Funksjon400(
    private val validOrgnrList: List<String>
) : AbstractRule<List<KostraRecord>>(
    "Kontroll 500 : Funksjon 400",
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            it.isResultatRegnskap()
        }.filter {
            it[FIELD_FUNKSJON].trim() == "400"
        }.filter {
            it[RegnskapConstants.FIELD_ORGNR] !in validOrgnrList
        }.map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Ugyldig funksjon. Funksjonen '400' kan kun benyttes av RHF og Nasjonale felleseide HF. Korriger funksjon.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        }.ifEmpty { null }
}