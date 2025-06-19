package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity

class Rule017BarnDeltatt :
    ValidateMatrixRule(
        Familievern55RuleId.FAMILIEVERN55_RULE017.title,
        Severity.WARNING,
    ) {
    override val columnSize = 3

    override val fieldList =
        listOf(
            Familievern55ColumnNames.BARN_DELT_COL_NAME,
            Familievern55ColumnNames.BARN_IKKE_DELT_COL_NAME,
            Familievern55ColumnNames.BARN_TOT_COL_NAME,
        )
}
