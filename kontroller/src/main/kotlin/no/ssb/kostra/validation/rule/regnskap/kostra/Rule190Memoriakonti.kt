package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isMemoriaKonti

class Rule190Memoriakonti : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 190 : Memoriakonti",
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isBalanseRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.isMemoriaKonti() }
        ?.partition { it.fieldAsIntOrDefault(FIELD_KAPITTEL) == 9999 }
        ?.let { (motpostMemoriakontiPosteringer, memoriakontiPosteringer) ->
            Pair(
                motpostMemoriakontiPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                memoriakontiPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            )
        }
        ?.takeUnless { (motpostMemoriakonti, memoriakonti) ->
            memoriakonti + motpostMemoriakonti in -10..10
        }
        ?.let { (motpostMemoriakonti, memoriakonti) ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differansen (${memoriakonti + motpostMemoriakonti}) " +
                        "mellom memoriakontiene ($memoriakonti) og motkonto for memoriakontiene " +
                        "($motpostMemoriakonti) går i 0. (margin på +/- 10')"
            )
        }
}