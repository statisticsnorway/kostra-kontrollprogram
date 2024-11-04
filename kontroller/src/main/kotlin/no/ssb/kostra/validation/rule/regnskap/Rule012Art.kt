package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KVARTAL
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isResultatRegnskap

class Rule012Art(
    val artList: List<String>
) : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 012 : Art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) =
        if (artList.isEmpty()) null
        else context
            .filter { it.isBevilgningRegnskap() || it.isResultatRegnskap() }
            .filter { kostraRecord -> artList.none { it == kostraRecord[FIELD_ART] } }
            .map { kostraRecord ->
                val artListAsString = artList.joinToString(", ")

                createValidationReportEntry(
                    messageText = "Fant ugyldig art '${kostraRecord[FIELD_ART]}'. " +
                            "Korrigér art til en av '$artListAsString'",
                    lineNumbers = listOf(kostraRecord.lineNumber),
                    severity = if (
                        kostraRecord[FIELD_KVARTAL].first()
                        in setOf('1', '2', '3')
                    )
                        Severity.WARNING
                    else
                        Severity.ERROR
                )
            }
            .ifEmpty { null }
}