package no.ssb.kostra.validation.rule.regnskap.helseforetak

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule540EiendelerErLikEgenkaptialPlussGjeld : AbstractRule<List<KostraRecord>>(
    "Kontroll 540 : Eiendeler = egenkapital + gjeld",
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? {
        val balanseRegnskap = context.filter { it.isBalanseRegnskap() }

        if (balanseRegnskap.isEmpty()) return null

        // 1) Balanse må ha føring på eiendelskontiene , dvs. være høyere enn 0
        val sumEiendeler: Int = balanseRegnskap
            .filter { kostraRecord ->
                kostraRecord.fieldAsIntOrDefault(FIELD_ART) in 100..195
            }.sumOf { kostraRecord ->
                kostraRecord.fieldAsIntOrDefault(FIELD_BELOP)
            }

        // 2) Balanse må ha føring på egenkapitalskontoer og/eller gjeldskontoer, dvs. være mindre enn 0
        val sumEgenkapital: Int = balanseRegnskap
            .filter { kostraRecord ->
                kostraRecord.fieldAsIntOrDefault(FIELD_ART) in 200..209
            }.sumOf { kostraRecord ->
                kostraRecord.fieldAsIntOrDefault(FIELD_BELOP)
            }

        val sumGjeld = balanseRegnskap
            .filter { kostraRecord ->
                kostraRecord.fieldAsIntOrDefault(FIELD_ART) in 210..299
            }.sumOf { kostraRecord ->
                kostraRecord.fieldAsIntOrDefault(FIELD_BELOP)
            }

        // 3) sumEiendeler skal være lik Egenkapital + Gjeld. Differanser opptil +50' godtas, og skal ikke utlistes.
        val sumBalanse = sumEiendeler + (sumEgenkapital + sumGjeld)

        if (sumBalanse in -50..50) return null

        return createSingleReportEntryList(
            messageText = "Balansen ($sumBalanse) skal balansere ved at sum eiendeler ($sumEiendeler)  = sum egenkapital ($sumEgenkapital) + sum gjeld ($sumGjeld) . Differanser +/- 50' kroner godtas"
        )
    }
}