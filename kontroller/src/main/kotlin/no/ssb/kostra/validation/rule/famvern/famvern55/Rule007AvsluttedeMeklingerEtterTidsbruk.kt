package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity

class Rule007AvsluttedeMeklingerEtterTidsbruk : ValidateMatrixRule(
    Familievern55RuleId.FAMILIEVERN55_RULE007.title,
    Severity.WARNING
) {
    override val columnSize = 5

    override val fieldList = listOf(
        Familievern55ColumnNames.VENTETID_SEP_1_COL_NAME,
        Familievern55ColumnNames.VENTETID_SEP_2_COL_NAME,
        Familievern55ColumnNames.VENTETID_SEP_3_COL_NAME,
        Familievern55ColumnNames.VENTETID_SEP_4_COL_NAME,
        Familievern55ColumnNames.VENTETID_SEP_TOT_COL_NAME,
        Familievern55ColumnNames.VENTETID_SAM_1_COL_NAME,
        Familievern55ColumnNames.VENTETID_SAM_2_COL_NAME,
        Familievern55ColumnNames.VENTETID_SAM_3_COL_NAME,
        Familievern55ColumnNames.VENTETID_SAM_4_COL_NAME,
        Familievern55ColumnNames.VENTETID_SAM_TOT_COL_NAME,
        Familievern55ColumnNames.VENTETID_SAK_1_COL_NAME,
        Familievern55ColumnNames.VENTETID_SAK_2_COL_NAME,
        Familievern55ColumnNames.VENTETID_SAK_3_COL_NAME,
        Familievern55ColumnNames.VENTETID_SAK_4_COL_NAME,
        Familievern55ColumnNames.VENTETID_SAK_TOT_COL_NAME,
        Familievern55ColumnNames.VENTETID_TILB_1_COL_NAME,
        Familievern55ColumnNames.VENTETID_TILB_2_COL_NAME,
        Familievern55ColumnNames.VENTETID_TILB_3_COL_NAME,
        Familievern55ColumnNames.VENTETID_TILB_4_COL_NAME,
        Familievern55ColumnNames.VENTETID_TILB_TOT_COL_NAME,
        Familievern55ColumnNames.VENTETID_FLY_1_COL_NAME,
        Familievern55ColumnNames.VENTETID_FLY_2_COL_NAME,
        Familievern55ColumnNames.VENTETID_FLY_3_COL_NAME,
        Familievern55ColumnNames.VENTETID_FLY_4_COL_NAME,
        Familievern55ColumnNames.VENTETID_FLY_TOT_COL_NAME,
        Familievern55ColumnNames.VENTETID_TOT_1_COL_NAME,
        Familievern55ColumnNames.VENTETID_TOT_2_COL_NAME,
        Familievern55ColumnNames.VENTETID_TOT_3_COL_NAME,
        Familievern55ColumnNames.VENTETID_TOT_4_COL_NAME,
        Familievern55ColumnNames.VENTETID_TOT_TOT_COL_NAME
    )
}
