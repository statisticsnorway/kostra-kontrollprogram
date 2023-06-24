package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_REGIONALE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapConstants.getRegnskapTypeBySkjema
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel

class Rule145AvskrivningerMotpostAvskrivninger : AbstractRecordRule(
    "Kontroll 145 : Avskrivninger, motpost avskrivninger",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { !it.isOsloBydel() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter {
            it.getFieldAsString(FIELD_FUNKSJON) == "860 "
                    && it.getFieldAsString(FIELD_ART) == "990"
        }
        ?.let { avskrivningPosteringer ->
            avskrivningPosteringer[0].getFieldAsString(FIELD_SKJEMA) to
                    avskrivningPosteringer
                        .sumOf { it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) }
        }
        ?.takeUnless { (_, motpostAvskrivninger) -> motpostAvskrivninger != 0 }
        ?.let { (skjema, motpostAvskrivninger) ->
            val severity = if (ACCOUNTING_TYPE_REGIONALE in getRegnskapTypeBySkjema(skjema)) Severity.ERROR
            else Severity.INFO

            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at den inneholder motpost avskrivninger " +
                        "($motpostAvskrivninger), føres på funksjon 860 og art 990.",
                severity
            )
        }
}