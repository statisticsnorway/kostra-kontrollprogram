package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isAktiva
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule125SummeringBalanseDifferanse : AbstractRecordRule(
    "Kontroll 125 : Summeringskontroller balanseregnskapet, differanse i balanseregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { it.isBalanseRegnskap() }
        .takeIf { it.any() }
        ?.partition { it.isAktiva() }
        ?.let { (aktivaPosteringer, passivaPosteringer) ->
            aktivaPosteringer
                .sumOf { it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) } to
                    passivaPosteringer
                        .sumOf { it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) }
        }
        ?.takeUnless { (aktiva, passiva) ->
            0 < aktiva
                    && passiva < 0
                    && aktiva + passiva in -10..10
        }
        ?.let { (aktiva, passiva) ->
            val balanseDifferanse = aktiva + passiva
            createSingleReportEntryList(
                messageText = "Korrigér differansen ($balanseDifferanse) mellom eiendeler ($aktiva) og gjeld " +
                        "og egenkapital ($passiva) i fila (Differanser opptil ±10' godtas)"
            )
        }
}