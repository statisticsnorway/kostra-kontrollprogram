package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isAktiva
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule125SummeringBalanseDifferanse : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 125 : Summeringskontroller balanseregnskapet, differanse i balanseregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isBalanseRegnskap() }
        .takeIf { it.any() }
        ?.partition { it.isAktiva() }
        ?.let { (aktivaPosteringer, passivaPosteringer) ->
            Pair(
                aktivaPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                passivaPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            ).takeUnless { it.areValidFigures() }?.let { (aktiva, passiva) ->
                createSingleReportEntryList(
                    messageText = "Korrigér differansen (${aktiva.plus(passiva)}) mellom eiendeler ($aktiva) " +
                            "og gjeld og egenkapital ($passiva) i fila (Differanser opptil ±10' godtas)"
                )
            }
        }

    companion object {
        internal fun Pair<Int, Int>.areValidFigures() = this.let { (aktiva, passiva) ->
            aktiva > 0
                    && passiva < 0
                    && aktiva.plus(passiva) in -10..10
        }
    }
}