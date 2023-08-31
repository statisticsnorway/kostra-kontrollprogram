package no.ssb.kostra.validation.rule.regnskap.helseforetak

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isResultatRegnskap

class Rule510Art320(
    private val validFunksjonList: List<String>
) : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 510 : art 320",
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isResultatRegnskap() }
        .filter { it[FIELD_ART] == "320" }
        .filter { it[FIELD_FUNKSJON].trim() !in validFunksjonList }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Ugyldig funksjon. Kontokode 320 ISF inntekter kan kun benyttes av somatisk, psykisk " +
                        "helsevern og rus. Korriger funksjon.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        }.ifEmpty { null }
}