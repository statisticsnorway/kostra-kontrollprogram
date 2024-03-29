package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule190Memoriakonti : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 190 : Memoriakonti",
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isBalanseRegnskap() && it.fieldAsIntOrDefault(FIELD_KAPITTEL) in 9100..9999 }
        .takeIf { it.any() }
        ?.partition { it.fieldAsIntOrDefault(FIELD_KAPITTEL) == 9999 }
        ?.let { (motpostMemoriakontiPosteringer, memoriakontiPosteringer) ->
            Pair(
                motpostMemoriakontiPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                memoriakontiPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            )
        }
        ?.takeUnless { (motpostMemoriakonti, memoriakonti) ->
            memoriakonti + motpostMemoriakonti in -30..30
        }
        ?.let { (motpostMemoriakonti, memoriakonti) ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differansen (${memoriakonti + motpostMemoriakonti}) " +
                        "mellom memoriakontiene ($memoriakonti) og motkonto for memoriakontiene " +
                        "($motpostMemoriakonti) går i 0. (margin på +/- 10')"
            )
        }
}