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
            Familievern55ColumnNames.RESULT_TOT_1_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_2_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_3_COL_NAME,
            Familievern55ColumnNames.RESULT_TOT_TOT_COL_NAME,
        )
}
