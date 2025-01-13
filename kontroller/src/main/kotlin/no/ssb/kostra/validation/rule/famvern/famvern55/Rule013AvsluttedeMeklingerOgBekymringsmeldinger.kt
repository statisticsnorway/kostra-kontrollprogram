package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity

class Rule013AvsluttedeMeklingerOgBekymringsmeldinger :
    ValidateMatrixRule(
        Familievern55RuleId.FAMILIEVERN55_RULE013.title,
        Severity.WARNING,
    ) {
    override val columnSize = 3

    override val fieldList =
        listOf(
            Familievern55ColumnNames.BEKYMR_SEP_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SEP_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SEP_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAM_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAM_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAM_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAK_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAK_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_SAK_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TILB_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TILB_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TILB_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_FLY_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_FLY_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_FLY_TOT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TOT_1_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TOT_2_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TOT_TOT_COL_NAME,
        )
}
