package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity

class Rule018BekymringsmeldingSendt :
    ValidateMatrixRule(
        Familievern55RuleId.FAMILIEVERN55_RULE018.title,
        Severity.WARNING,
    ) {
    override val columnSize = 3

    override val fieldList =
        listOf(
            Familievern55ColumnNames.BEKYMR_SENDT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_IKKE_SENDT_COL_NAME,
            Familievern55ColumnNames.BEKYMR_TOT_COL_NAME,
        )
}