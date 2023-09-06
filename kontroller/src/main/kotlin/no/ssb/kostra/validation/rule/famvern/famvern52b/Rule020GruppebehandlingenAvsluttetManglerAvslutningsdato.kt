package no.ssb.kostra.validation.rule.famvern.famvern52b

import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.DATO_GRAVSLUTN_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.KONTOR_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.STATUS_ARETSSL_B_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule020GruppebehandlingenAvsluttetManglerAvslutningsdato : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52bRuleId.FAMILIEVERN52B_RULE020.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it[STATUS_ARETSSL_B_COL_NAME] == "2" }
        .filterNot { it.fieldAsLocalDate(DATO_GRAVSLUTN_B_COL_NAME) != null }
        .map {
            createValidationReportEntry(
                messageText = "Det er krysset av for at gruppebehandlingen er avsluttet i rapporteringsåret, " +
                        "men ikke fylt ut dato '${it[DATO_GRAVSLUTN_B_COL_NAME]}' for avslutning av saken.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KONTOR_NR_B_COL_NAME],
                journalId = it[GRUPPE_NR_B_COL_NAME]
            )
        }.ifEmpty { null }
}