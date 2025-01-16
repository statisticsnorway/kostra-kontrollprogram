package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity

class Rule011ResultatAvAvsluttedeMeklinger :
    ValidateMatrixRule(
        Familievern55RuleId.FAMILIEVERN55_RULE011.title,
        Severity.WARNING,
    ) {
    override val columnSize = 4

    override val fieldList =
        listOf(
            Familievern55ColumnNames.RESULT_SEP_1_COL_NAME,
            Familievern55ColumnNames.RESULT_SEP_2_COL_NAME,
            Familievern55ColumnNames.RESULT_SEP_3_COL_NAME,
            Familievern55ColumnNames.RESULT_SEP_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_SAM_1_COL_NAME,
            Familievern55ColumnNames.RESULT_SAM_2_COL_NAME,
            Familievern55ColumnNames.RESULT_SAM_3_COL_NAME,
            Familievern55ColumnNames.RESULT_SAM_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_SAK_1_COL_NAME,
            Familievern55ColumnNames.RESULT_SAK_2_COL_NAME,
            Familievern55ColumnNames.RESULT_SAK_3_COL_NAME,
            Familievern55ColumnNames.RESULT_SAK_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_TILB_1_COL_NAME,
            Familievern55ColumnNames.RESULT_TILB_2_COL_NAME,
            Familievern55ColumnNames.RESULT_TILB_3_COL_NAME,
            Familievern55ColumnNames.RESULT_TILB_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_FLY_1_COL_NAME,
            Familievern55ColumnNames.RESULT_FLY_2_COL_NAME,
            Familievern55ColumnNames.RESULT_FLY_3_COL_NAME,
            Familievern55ColumnNames.RESULT_FLY_TOT_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_1_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_2_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_3_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_TOT_COL_NAME,
        )
}
