package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isNotOsloBydel

class Rule155AvskrivningerDifferanse : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 155 : Avskrivninger, differanse",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isNotOsloBydel() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.let { driftPosteringer ->
            Pair(
                driftPosteringer
                    .filter { it.fieldAsIntOrDefault(FIELD_FUNKSJON) in 100..799 && it[FIELD_ART] == "590" }
                    .sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                driftPosteringer
                    .filter { it[FIELD_FUNKSJON].trim() == "860" && it[FIELD_ART] == "990" }
                    .sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            )
        }
        ?.takeUnless { (avskrivninger, motpostAvskrivninger) ->
            avskrivninger + motpostAvskrivninger in -30..30
        }
        ?.let { (avskrivninger, motpostAvskrivninger) ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at avskrivninger ($avskrivninger) stemmer " +
                        "overens med motpost avskrivninger ($motpostAvskrivninger) (margin på +/- 30')"
            )
        }

}