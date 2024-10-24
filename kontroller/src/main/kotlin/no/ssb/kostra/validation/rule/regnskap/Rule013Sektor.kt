package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KVARTAL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule013Sektor(
    val sektorList: List<String>
) : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 013 : Sektor",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) =
        if (sektorList.isEmpty())
            null
        else
            context
                .filter { it.isBalanseRegnskap() }
                .filter { kostraRecord -> sektorList.none { it == kostraRecord[FIELD_SEKTOR] } }
                .map { kostraRecord ->
                    val sektorListAsString = sektorList.joinToString(", ")

                    if (sektorList.first() == "   ")
                        createValidationReportEntry(
                            messageText = "Fant ugyldig sektor '${kostraRecord[FIELD_SEKTOR]}'. " +
                                    "Posisjoner for sektorkoder skal være blanke",
                            lineNumbers = listOf(kostraRecord.lineNumber)
                        )
                    else
                        createValidationReportEntry(
                            messageText = "Fant ugyldig sektor '${kostraRecord[FIELD_SEKTOR]}'. " +
                                    "Korrigér sektor til en av '$sektorListAsString'",
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