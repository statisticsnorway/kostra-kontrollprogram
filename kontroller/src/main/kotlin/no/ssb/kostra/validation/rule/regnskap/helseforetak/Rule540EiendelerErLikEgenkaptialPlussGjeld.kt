package no.ssb.kostra.validation.rule.regnskap.helseforetak

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule540EiendelerErLikEgenkaptialPlussGjeld : AbstractRule<List<KostraRecord>>(
    "Kontroll 540 : Eiendeler = egenkapital + gjeld",
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it.isBalanseRegnskap()
        }
        .groupBy {
            when (it.fieldAsIntOrDefault(FIELD_SEKTOR)) {
                in 100..195 -> "eiendeler"
                in 200..209 -> "egenkaptital"
                in 210..299 -> "gjeld"
                else -> "annet"
            }
        }
        .mapValues {
            it.value.sumOf { kostraRecord -> kostraRecord.fieldAsIntOrDefault(FIELD_BELOP) }
        }
        .let {
            Triple(
                it.getOrDefault("eiendeler", 0),
                it.getOrDefault("egenkaptital", 0),
                it.getOrDefault("gjeld", 0),
            ).also { that -> println(that) }
        }
        .takeUnless { (sumEiendeler, sumEgenkapital, sumGjeld) ->
            (sumEiendeler
                    + sumEgenkapital
                    + sumGjeld
                    ) in -50..50
        }
        ?.let { (sumEiendeler, sumEgenkapital, sumGjeld) ->
            val sumBalanse = sumEiendeler + (sumEgenkapital + sumGjeld)
            createSingleReportEntryList(
                messageText = "Balansen ($sumBalanse) skal balansere ved at sum eiendeler ($sumEiendeler)  = sum " +
                        "egenkapital ($sumEgenkapital) + sum gjeld ($sumGjeld) . Differanser +/- 50' kroner godtas"
            )
        }
}