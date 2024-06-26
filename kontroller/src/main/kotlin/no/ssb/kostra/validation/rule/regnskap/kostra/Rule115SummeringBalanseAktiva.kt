package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isAktiva
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule115SummeringBalanseAktiva : AbstractRule<List<KostraRecord>>(
    "Kontroll 115 : Summeringskontroller balanseregnskap, registrering av aktiva",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter { it.isBalanseRegnskap() && it.isAktiva() }
        .takeIf { it.any() }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { 0 < it }
        ?.let { sumAktiva ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder registrering av aktiva/eiendeler " +
                        "($sumAktiva), sum kapittel 10-29 i balanse."
            )
        }
}