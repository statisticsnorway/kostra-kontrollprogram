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

class Rule160AvskrivningerAndreFunksjoner : AbstractRecordRule(
    "Kontroll 160 : Avskrivninger, avskrivninger ført på andre funksjoner",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { !it.isOsloBydel() && it.isBevilgningDriftRegnskap() }
        .filter { kostraRecord ->
            kostraRecord.getFieldAsIntegerDefaultEquals0(FIELD_FUNKSJON) in 800..899
                    && kostraRecord.getFieldAsString(FIELD_ART) == "590"
                    && kostraRecord.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) > 0
        }
        .takeIf { it.any() }
        ?.let { kostraRecordList ->
            kostraRecordList.sumOf { it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) } to
                    kostraRecordList.map { it.getFieldAsTrimmedString(FIELD_FUNKSJON) }
        }
        ?.takeUnless { (avskrivninger, funksjoner) -> 0 < avskrivninger }
        ?.let { (avskrivninger, funksjoner) ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at avskrivningene ($avskrivninger) føres på " +
                        "tjenestefunksjon og ikke på funksjonene ($funksjoner)"
            )
        }
}