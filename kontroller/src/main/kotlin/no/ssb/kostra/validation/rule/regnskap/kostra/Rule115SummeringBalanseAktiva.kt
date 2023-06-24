package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isAktiva
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule115SummeringBalanseAktiva : AbstractRecordRule(
    "Kontroll 115 : Summeringskontroller balanseregnskap, registrering av aktiva",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { it.isBalanseRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.isAktiva() }
        ?.sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) }
        ?.takeUnless { 0 < it }
        ?.let { sumAktiva ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder registrering av aktiva/eiendeler " +
                        "($sumAktiva), sum sektor 000-990 for kapittel 10-29 i balanse."
            )
        }
}