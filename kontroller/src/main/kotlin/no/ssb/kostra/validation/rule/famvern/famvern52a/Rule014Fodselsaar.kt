package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.PRIMK_FODT_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule014Fodselsaar : AbstractRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE014.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        it.fieldAs<String?>(PRIMK_FODT_A_COL_NAME) != null
                && it.fieldAsIntOrDefault(PRIMK_FODT_A_COL_NAME) in
                (arguments.aargang.toInt() - 100)..arguments.aargang.toInt()
    }.map {
        createValidationReportEntry(
            messageText = "Dette er ikke oppgitt fødselsår på primærklienten eller feltet har ugyldig format, " +
                    "årgang gir negativ alder, eller alder større enn 100 år. Fant '${it[PRIMK_FODT_A_COL_NAME]}'. " +
                    "Feltet er obligatorisk å fylle ut. Fyll inn fødselsår eller sjekk at fødselsårsfeltet har " +
                    "korrekt format og logisk verdi i forhold til alder.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}