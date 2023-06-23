package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule075KombinasjonBevilgningFunksjonArt : AbstractRecordRule(
    "Kontroll 075 : Ugyldig kombinasjon i bevilgningsregnskapet, funksjon og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningRegnskap()
                    && kostraRecord.getFieldAsString(FIELD_ART) in listOf("870", "871", "872", "873", "875", "876")
                    && kostraRecord.getFieldAsString(FIELD_FUNKSJON) != "800 "
                    && kostraRecord.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) != 0
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Artene 870, 871, 872, 873, 875 og 876 er kun tillat brukt i kombinasjon med funksjon 800.",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}