package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity

class Rule016AvsluttedeMeklingerUtenOppmote : ValidateMatrixRule(
    Familievern55RuleId.FAMILIEVERN55_RULE016.title,
    Severity.WARNING
) {
    override val columnSize = 6

    override val fieldList = listOf(
        Familievern55ColumnNames.UTEN_OPPM_1_COL_NAME,
        Familievern55ColumnNames.UTEN_OPPM_2_COL_NAME,
        Familievern55ColumnNames.UTEN_OPPM_3_COL_NAME,
        Familievern55ColumnNames.UTEN_OPPM_4_COL_NAME,
        Familievern55ColumnNames.UTEN_OPPM_5_COL_NAME,
        Familievern55ColumnNames.UTEN_OPPM_TOT_COL_NAME,
    )
}
