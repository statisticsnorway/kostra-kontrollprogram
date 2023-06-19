package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel

class Rule165AvskrivningerMotpostAvskrivningerAndreFunksjoner : AbstractRecordRule(
    "Kontroll 165 : Avskrivninger, motpost avskrivninger ført på andre funksjoner",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter {
            !it.isOsloBydel() && it.isBevilgningDriftRegnskap()
                    && it.getFieldAsString(FIELD_FUNKSJON) != "860 "
                    && it.getFieldAsString(FIELD_ART) == "990"
                    && it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) > 0
        }
        .takeIf { it.any() }
        ?.let { kostraRecordList ->
            kostraRecordList.sumOf { it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) } to
                    kostraRecordList.map { it.getFieldAsTrimmedString(FIELD_FUNKSJON) }
        }
        ?.takeUnless { (motpostAvskrivninger, funksjoner) -> motpostAvskrivninger == 0 }
        ?.let { (motpostAvskrivninger, funksjoner) ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at motpost avskrivninger ($motpostAvskrivninger) kun er " +
                        "ført på funksjon 860, art 990 og ikke på funksjonene ($funksjoner)"
            )
        }
}