package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel

class Rule165AvskrivningerMotpostAvskrivningerAndreFunksjoner : AbstractRule<List<KostraRecord>>(
    "Kontroll 165 : Avskrivninger, motpost avskrivninger ført på andre funksjoner",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            !it.isOsloBydel()
                    && it.isBevilgningDriftRegnskap()
                    && it.getFieldAsTrimmedString(FIELD_FUNKSJON) != "860"
                    && it.getFieldAsString(FIELD_ART) == "990"
                    && it.getFieldAsIntegerOrDefault(FIELD_BELOP) > 0
        }
        .takeIf { it.any() }
        ?.let { kostraRecordList ->
            kostraRecordList.sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) } to
                    kostraRecordList.map { it.getFieldAsTrimmedString(FIELD_FUNKSJON) }
        }
        /** TODO Jon Ole: Denne blir litt tricky. it.getFieldAsIntegerOrDefault(FIELD_BELOP) > 0 over gjør det
         * umulig å kjøre inn negative beløp, og vi kommer oss derfor aldri forbi takeUnless under */
        ?.takeUnless { (motpostAvskrivninger, _) -> motpostAvskrivninger == 0 }
        ?.let { (motpostAvskrivninger, funksjoner) ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at motpost avskrivninger ($motpostAvskrivninger) kun er " +
                        "ført på funksjon 860, art 990 og ikke på funksjonene ($funksjoner)"
            )
        }
}