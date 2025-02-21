package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule200Funksjon089Finansieringstransaksjoner(
    private val validArtList: List<String>,
) : AbstractNoArgsRule<List<KostraRecord>>(
        "Kontroll 200 : Funksjon 089, Finansieringstransaksjoner",
        Severity.ERROR,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filter { it.isBevilgningRegnskap() && it[FIELD_FUNKSJON].trim() == "089" }
            .filterNot { it.fieldAsIntOrDefault(FIELD_ART) in validArtList.map { art -> art.toInt() } }
            .map {
                createValidationReportEntry(
                    messageText =
                        "Art (${it[FIELD_ART]}) er ugyldig mot funksjon 089. " +
                                "Gyldige arter er: ($validArtList).",
                    lineNumbers = listOf(it.lineNumber),
                )
            }.ifEmpty { null }
}
