package no.ssb.kostra.validation.rule.regnskap.helseforetak

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isResultatRegnskap

class Rule510Art320(
    private val validFunksjonList: List<String>
) : AbstractRule<List<KostraRecord>>(
    "Kontroll 510 : art 320",
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            it.isResultatRegnskap()
        }.filter {
            it.fieldAsString(RegnskapConstants.FIELD_ART)  == "320"
        }.filter {
            it.fieldAsTrimmedString(RegnskapConstants.FIELD_FUNKSJON) !in validFunksjonList
        }.map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Ugyldig funksjon. Kontokode 320 ISF inntekter kan kun benyttes av somatisk, psykisk helsevern og rus. Korriger funksjon.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        }.ifEmpty { null }
}