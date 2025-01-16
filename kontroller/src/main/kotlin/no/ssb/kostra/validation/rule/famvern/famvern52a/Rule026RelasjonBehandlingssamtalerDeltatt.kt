package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_ANDR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_BARNO18_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_BARNU18_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_EKSPART_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_FORELDRE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_OVRFAM_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_PARTNER_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DELT_VENN_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_ANDRE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_BARNO18_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_BARNU18_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_EKSPART_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_FORELDRE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_OVRFAM_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_PARTNER_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_VENN_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule026RelasjonBehandlingssamtalerDeltatt :
    AbstractNoArgsRule<List<KostraRecord>>(
        Familievern52aRuleId.FAMILIEVERN52A_RULE026.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .flatMap {
                mappingList.mapNotNull { mapping ->
                    if (it[mapping.participation] == "1" &&
                        it.fieldAsIntOrDefault(
                            mapping.count,
                        ) == 0
                    ) {
                        createValidationReportEntry(
                            messageText =
                                "Det er oppgitt at andre personer (${mapping.title}) har deltatt i samtaler med " +
                                    "primærklient i løpet av året, men det er ikke oppgitt hvor mange behandlingssamtaler " +
                                    "(${it[mapping.count]}) de ulike personene har deltatt i gjennom av året.",
                            lineNumbers = listOf(it.lineNumber),
                        ).copy(
                            caseworker = it[KONTOR_NR_A_COL_NAME],
                            journalId = it[JOURNAL_NR_A_COL_NAME],
                        )
                    } else {
                        null
                    }
                }
            }.ifEmpty { null }

    private data class Mapping(
        val title: String,
        val participation: String,
        val count: String,
    )

    companion object {
        private val mappingList =
            listOf(
                Mapping(
                    "Partner",
                    DELT_PARTNER_A_COL_NAME,
                    SAMT_PARTNER_A_COL_NAME,
                ),
                Mapping(
                    "Ekspartner",
                    DELT_EKSPART_A_COL_NAME,
                    SAMT_EKSPART_A_COL_NAME,
                ),
                Mapping(
                    "Barn under 18år",
                    DELT_BARNU18_A_COL_NAME,
                    SAMT_BARNU18_A_COL_NAME,
                ),
                Mapping(
                    "Barn over 18år",
                    DELT_BARNO18_A_COL_NAME,
                    SAMT_BARNO18_A_COL_NAME,
                ),
                Mapping(
                    "Foreldre",
                    DELT_FORELDRE_A_COL_NAME,
                    SAMT_FORELDRE_A_COL_NAME,
                ),
                Mapping(
                    "Øvrige familie",
                    DELT_OVRFAM_A_COL_NAME,
                    SAMT_OVRFAM_A_COL_NAME,
                ),
                Mapping("Venner", DELT_VENN_A_COL_NAME, SAMT_VENN_A_COL_NAME),
                Mapping("Andre", DELT_ANDR_A_COL_NAME, SAMT_ANDRE_A_COL_NAME),
            )
    }
}
