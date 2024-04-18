package no.ssb.kostra.validation.rule.regnskap.helseforetak

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
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
        .filter {
            it.isResultatRegnskap()
                    && it.fieldAsIntOrDefault(FIELD_BELOP) != 0
                    && it[FIELD_ART] == "320"
                    && validFunksjonList.none { validFunksjon -> validFunksjon.trim() == it[FIELD_FUNKSJON].trim() }
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Ugyldig funksjon. Art 320  – ISF inntekter kan kun føres på funksjonene " +
                        "for somatikk, psykisk helsevern og rus.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        }.ifEmpty { null }
}