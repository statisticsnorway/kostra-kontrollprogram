package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isLongyearbyen
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isRegional

class Rule135Rammetilskudd : AbstractRule<List<KostraRecord>>(
    "Kontroll 135 : Rammetilskudd",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot { it.isOsloBydel() || it.isLongyearbyen() }
        // unhealthy exception, ask for a more stable approach
        .filterNot { it[FIELD_REGION] == "501400" }
        .filter { it.isRegional() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { it[FIELD_FUNKSJON].trim() == "840" && it[FIELD_ART] == "800" }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { rammeTilskudd -> rammeTilskudd < 0 }
        ?.let { rammeTilskudd ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder rammetilskudd ($rammeTilskudd).",
                severity = if (arguments.kvartal.first() == ' ') Severity.ERROR else Severity.WARNING
            )
        }
}