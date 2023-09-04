package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity

class Rule006AvsluttedeMeklingerEtterDeltakere : ValidateMatrixRule(
    Familievern55RuleId.FAMILIEVERN55_RULE006.title,
    Severity.WARNING
) {
    override val columnSize = 3

    override val fieldList = listOf(
        Familievern55ColumnNames.SEP_BEGGE_COL_NAME,
        Familievern55ColumnNames.SEP_EN_COL_NAME,
        Familievern55ColumnNames.SEP_TOT_COL_NAME,
        Familievern55ColumnNames.SAM_BEGGE_COL_NAME,
        Familievern55ColumnNames.SAM_EN_COL_NAME,
        Familievern55ColumnNames.SAM_TOT_COL_NAME,
        Familievern55ColumnNames.SAK_BEGGE_COL_NAME,
        Familievern55ColumnNames.SAK_EN_COL_NAME,
        Familievern55ColumnNames.SAK_TOT_COL_NAME,
        Familievern55ColumnNames.TILB_BEGGE_COL_NAME,
        Familievern55ColumnNames.TILB_EN_COL_NAME,
        Familievern55ColumnNames.TILB_TOT_COL_NAME,
        Familievern55ColumnNames.FLY_BEGGE_COL_NAME,
        Familievern55ColumnNames.FLY_EN_COL_NAME,
        Familievern55ColumnNames.FLY_TOT_COL_NAME,
        Familievern55ColumnNames.BEGGE_TOT_COL_NAME,
        Familievern55ColumnNames.EN_TOT_COL_NAME,
        Familievern55ColumnNames.ENBEGGE_TOT_COL_NAME,
    )
}
