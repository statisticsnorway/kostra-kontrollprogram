package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity

class Rule010AvsluttedeMeklingerHvorBarnHarDeltatt :
    ValidateMatrixRule(
        Familievern55RuleId.FAMILIEVERN55_RULE010.title,
        Severity.WARNING,
    ) {
    override val columnSize = 6

    override val fieldList =
        listOf(
            Familievern55ColumnNames.BARNDELT_SEP_TOT_COL_NAME,
            Familievern55ColumnNames.BARNDELT_SAM_TOT_COL_NAME,
            Familievern55ColumnNames.BARNDELT_SAK_TOT_COL_NAME,
            Familievern55ColumnNames.BARNDELT_TILB_TOT_COL_NAME,
            Familievern55ColumnNames.BARNDELT_FLY_TOT_COL_NAME,
            Familievern55ColumnNames.BARNDELT_TOT_TOT_COL_NAME,
        )
}
