package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isPassiva

class Rule120SummeringBalansePassiva : AbstractRule<List<KostraRecord>>(
    "Kontroll 120 : Summeringskontroller balanseregnskap, registrering av passiva",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter { it.isBalanseRegnskap() && it.isPassiva() }
        .takeIf { it.any() }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { it < 0 }
        ?.let { sumPassiva ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder registrering av passiva/gjeld og egenkapital " +
                        "($sumPassiva), sum kapittel 31-5990 i balanse.",
                severity = if (arguments.kvartal.first() == ' ') Severity.ERROR else Severity.WARNING
            )
        }
}