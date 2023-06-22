package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule200Funksjon089Finansieringstransaksjoner : AbstractRecordRule(
    "Kontroll 200 : Funksjon 089, Finansieringstransaksjoner",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter {
            it.isBevilgningRegnskap()
                    && it.getFieldAsString(FIELD_FUNKSJON) == "089 "
                    && !(
                    it.getFieldAsIntegerDefaultEquals0(FIELD_ART) in 500..580
                            || it.getFieldAsIntegerDefaultEquals0(FIELD_ART) == 830
                            || it.getFieldAsIntegerDefaultEquals0(FIELD_ART) in 900..980
                    )
        }
        .map {
            createValidationReportEntry(
                messageText = "Korrigér i fila slik at art (${it.getFieldAsString(FIELD_ART)}) " +
                        "er gyldig mot funksjon 089. Gyldige arter er 500-580, 830 og 900-980.",
                lineNumbers = listOf(it.index)
            )
        }
        .ifEmpty { null }
}