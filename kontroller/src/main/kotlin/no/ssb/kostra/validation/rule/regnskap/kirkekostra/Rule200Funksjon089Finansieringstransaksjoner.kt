package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule200Funksjon089Finansieringstransaksjoner : AbstractRule<List<KostraRecord>>(
    "Kontroll 200 : Funksjon 089, Finansieringstransaksjoner",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context.filter {
        it.isBevilgningRegnskap()
                && it[FIELD_FUNKSJON].trim() == "089"
    }.filterNot { it.fieldAsIntOrDefault(FIELD_ART) in artStopList }.map {
        createValidationReportEntry(
            messageText = "Korrigér i fila slik at art (${it[FIELD_ART]}) " +
                    "er gyldig mot funksjon 089. Gyldige arter er 500-580, 830 og 900-980.",
            lineNumbers = listOf(it.lineNumber)
        )
    }.ifEmpty { null }

    companion object {
        private val artStopList = (500..580)
            .plus(900..980)
            .plus(830)
    }
}