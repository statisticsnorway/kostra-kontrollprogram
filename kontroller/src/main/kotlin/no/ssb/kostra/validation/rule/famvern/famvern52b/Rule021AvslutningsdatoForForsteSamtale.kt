package no.ssb.kostra.validation.rule.famvern.famvern52b

import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.DATO_GRAVSLUTN_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.DATO_GRSTART_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.KONTOR_NR_B_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import java.time.LocalDate

class Rule021AvslutningsdatoForForsteSamtale :
    AbstractNoArgsRule<List<KostraRecord>>(
        Familievern52bRuleId.FAMILIEVERN52B_RULE021.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filterNot {
                it.fieldAs<LocalDate?>(DATO_GRSTART_B_COL_NAME) != null &&
                    it.fieldAs<LocalDate?>(DATO_GRAVSLUTN_B_COL_NAME) != null &&
                    it.fieldAs<LocalDate>(DATO_GRSTART_B_COL_NAME) <=
                    it.fieldAs<LocalDate>(DATO_GRAVSLUTN_B_COL_NAME)
            }.map {
                createValidationReportEntry(
                    messageText =
                        "Dato for avslutting av gruppebehandlingen '${it[DATO_GRAVSLUTN_B_COL_NAME]}' " +
                            "kommer før dato for gruppebehandlingens start '${it[DATO_GRSTART_B_COL_NAME]}'.",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[KONTOR_NR_B_COL_NAME],
                    journalId = it[GRUPPE_NR_B_COL_NAME],
                )
            }.ifEmpty { null }
}
