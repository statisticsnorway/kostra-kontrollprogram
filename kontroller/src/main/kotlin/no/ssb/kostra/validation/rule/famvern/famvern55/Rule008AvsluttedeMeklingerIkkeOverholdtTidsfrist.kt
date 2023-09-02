package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity

class Rule008AvsluttedeMeklingerIkkeOverholdtTidsfrist : ValidateMatrixRule(
    Familievern55RuleId.FAMILIEVERN55_RULE008.title,
    Severity.WARNING
) {
    override val columnSize = 3

    override val fieldList = listOf(
        Familievern55ColumnNames.FORHOLD_MEKLER_COL_NAME,
        Familievern55ColumnNames.FORHOLD_KLIENT_COL_NAME,
        Familievern55ColumnNames.FORHOLD_TOT_COL_NAME,
    )
}
