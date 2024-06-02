package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule016KapittelFortegn : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 016 : Kapittel, fortegn",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { kostraRecord -> kostraRecord.isBalanseRegnskap()
                && kostraRecord[FIELD_KAPITTEL] in setOf("5900", "5970") }
        .filterNot { 0 <= it.fieldAsIntOrDefault(FIELD_BELOP) }
        .map { kostraRecord ->
            val belop5900 = if (kostraRecord[FIELD_KAPITTEL] == "5900")
                "(Beløp = ${kostraRecord[FIELD_BELOP]})"
            else ""
            val belop5970 = if (kostraRecord[FIELD_KAPITTEL] == "5970")
                "(Beløp = ${kostraRecord[FIELD_BELOP]})"
            else ""
            createValidationReportEntry(
                messageText = "Kapittel 5900 Merforbruk i driftsregnskapet $belop5900 og " +
                        "kapittel 5970 Udekket beløp i investeringsregnskapet $belop5970 skal bare kunne rapporteres " +
                        "som positive tall, altså tall større eller lik 0. Eventuelt mindreforbruk i driftsregnskapet " +
                        "eller udisponert i investeringsregnskapet må posteres etter reglene i Forskrift om " +
                        "økonomiplan, årsbudsjett, årsregnskap og årsberetning for kommuner og fylkeskommuner " +
                        "mv. , henholdsvis paragrafene 4.3 og 4.6",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        }
        .ifEmpty { null }
}