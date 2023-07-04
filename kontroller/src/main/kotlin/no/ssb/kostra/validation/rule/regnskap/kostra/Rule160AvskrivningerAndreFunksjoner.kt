package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel

class Rule160AvskrivningerAndreFunksjoner : AbstractRule<List<KostraRecord>>(
    "Kontroll 160 : Avskrivninger, avskrivninger ført på andre funksjoner",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            !it.isOsloBydel()
                    && it.isBevilgningDriftRegnskap()
                    && it.fieldAsIntOrDefault(FIELD_FUNKSJON) in 800..899
                    && it.fieldAsString(FIELD_ART) == "590"
        }.takeIf { it.any() }
        ?.let { kostraRecordList ->
            kostraRecordList.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) } to
                    kostraRecordList.map { it.fieldAsTrimmedString(FIELD_FUNKSJON) }
        }?.takeUnless { (avskrivninger, _) -> avskrivninger == 0 }
        ?.let { (avskrivninger, funksjoner) ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at avskrivningene ($avskrivninger) føres på " +
                        "tjenestefunksjon og ikke på funksjonene ($funksjoner)"
            )
        }
}