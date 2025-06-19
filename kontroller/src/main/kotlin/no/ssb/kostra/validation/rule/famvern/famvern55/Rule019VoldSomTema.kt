package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity

class Rule019VoldSomTema :
    ValidateMatrixRule(
        Familievern55RuleId.FAMILIEVERN55_RULE019.title,
        Severity.WARNING,
    ) {
    override val columnSize = 3

    override val fieldList =
        listOf(
            Familievern55ColumnNames.VOLD_TEMA_COL_NAME,
            Familievern55ColumnNames.VOLD_IKKE_TEMA_COL_NAME,
            Familievern55ColumnNames.VOLD_TOT_COL_NAME,
        )
}
