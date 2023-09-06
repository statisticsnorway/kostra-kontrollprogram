package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_ALVH_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_AVKLAR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_BARNFOR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_BARNSIT_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_BOSTED_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_FLERGEN_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_FORBARN_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_FORELDRE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_KULTUR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_PARREL_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_RUS_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_SAERBARN_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_SAMBARN_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_SAMLBRUDD_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_SAMSPILL_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_SYKD_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_TVANG_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.TEMA_VOLD_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule022OmraaderArbeidet : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE022.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { tema.any { tema -> fieldDefinitions.byColumnName(tema).codeExists(it[tema]) } }
        .map {
            createValidationReportEntry(
                messageText = "Det er ikke fylt ut hvilke områder det har vært arbeidet med siden saken ble opprettet. " +
                        "Feltet er obligatorisk å fylle ut, og kan inneholde mer enn ett område.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KONTOR_NR_A_COL_NAME],
                journalId = it[JOURNAL_NR_A_COL_NAME]
            )
        }.ifEmpty { null }

    companion object {
        private val tema = listOf(
            TEMA_PARREL_A_COL_NAME,
            TEMA_AVKLAR_A_COL_NAME,
            TEMA_SAMLBRUDD_A_COL_NAME,
            TEMA_SAMSPILL_A_COL_NAME,
            TEMA_BARNSIT_A_COL_NAME,
            TEMA_BARNFOR_A_COL_NAME,
            TEMA_BOSTED_A_COL_NAME,
            TEMA_FORELDRE_A_COL_NAME,
            TEMA_FORBARN_A_COL_NAME,
            TEMA_FLERGEN_A_COL_NAME,
            TEMA_SAMBARN_A_COL_NAME,
            TEMA_SAERBARN_A_COL_NAME,
            TEMA_KULTUR_A_COL_NAME,
            TEMA_TVANG_A_COL_NAME,
            TEMA_RUS_A_COL_NAME,
            TEMA_SYKD_A_COL_NAME,
            TEMA_VOLD_A_COL_NAME,
            TEMA_ALVH_A_COL_NAME
        )
    }
}