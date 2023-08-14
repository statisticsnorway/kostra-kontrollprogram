package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.s52a.Familievern52aColumnNames.REGION_NR_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule003Regionsnummer(
    private val regionList: List<String>
) : AbstractRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE003.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        it[REGION_NR_A_COL_NAME] in regionList
    }.map {
        createValidationReportEntry(
            messageText = "Regionsnummeret som er oppgitt i recorden fins ikke i listen med gyldige regionsnumre. "
                    + "Fant '${it[REGION_NR_A_COL_NAME]}', forventet én av : $regionList.",
            lineNumbers = listOf(it.lineNumber)
        )
    }.ifEmpty { null }
}