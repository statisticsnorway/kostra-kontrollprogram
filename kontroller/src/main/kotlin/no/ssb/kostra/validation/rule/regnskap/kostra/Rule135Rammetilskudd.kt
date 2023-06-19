package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isLongyearbyen
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isRegional

class Rule135Rammetilskudd : AbstractRecordRule(
    "Kontroll 135 : Rammetilskudd",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { !it.isOsloBydel() && !it.isLongyearbyen() && it.isRegional() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter {
            it.getFieldAsString(RegnskapConstants.FIELD_FUNKSJON) == "840 "
                    && it.getFieldAsString(RegnskapConstants.FIELD_ART) == "800"
        }
        ?.sumOf { it.getFieldAsIntegerDefaultEquals0(RegnskapConstants.FIELD_BELOP) }
        ?.takeUnless { rammeTilskudd -> rammeTilskudd < 0 }
        ?.let { rammeTilskudd ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder rammetilskudd ($rammeTilskudd)."
            )
        }
}