package no.ssb.kostra.validation.rule.regnskap.helseforetak

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule520Konti19XKunPositiveBelop(
    private val validArtList: List<String>
) : AbstractRule<List<KostraRecord>>(
    "Kontroll 520 : Konti 190, 192, 194, 195 inneholder kun positive beløp",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isBalanseRegnskap() }
        .filter { it[FIELD_ART] in validArtList }
        .filterNot { it.fieldAsIntOrDefault(FIELD_BELOP) > 0 }.map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Kun positive beløp er gyldig. Fant ugyldig " +
                        "beløp (${kostraRecord.fieldAsIntOrDefault(FIELD_BELOP)})",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        }.ifEmpty { null }
}