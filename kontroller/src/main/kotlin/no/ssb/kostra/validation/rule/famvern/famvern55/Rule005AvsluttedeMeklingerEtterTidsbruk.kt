package no.ssb.kostra.validation.rule.famvern.famvern55

import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_FLY_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_FLY_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_FLY_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_FLY_TOT_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAK_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAK_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAK_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAK_TOT_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAM_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAM_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAM_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SAM_TOT_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SEP_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SEP_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SEP_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_SEP_TOT_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TILB_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TILB_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TILB_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TILB_TOT_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TOT_1_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TOT_2_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TOT_3_COL_NAME
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames.MEKLING_TOT_ALLE_COL_NAME
import no.ssb.kostra.validation.report.Severity

class Rule005AvsluttedeMeklingerEtterTidsbruk : ValidateMatrixRule(
    Familievern55RuleId.FAMILIEVERN55_RULE005.title,
    Severity.WARNING
) {
    override val columnSize = 4

    override val fieldList = listOf(
        MEKLING_SEP_1_COL_NAME,
        MEKLING_SEP_2_COL_NAME,
        MEKLING_SEP_3_COL_NAME,
        MEKLING_SEP_TOT_COL_NAME,
        MEKLING_SAM_1_COL_NAME,
        MEKLING_SAM_2_COL_NAME,
        MEKLING_SAM_3_COL_NAME,
        MEKLING_SAM_TOT_COL_NAME,
        MEKLING_SAK_1_COL_NAME,
        MEKLING_SAK_2_COL_NAME,
        MEKLING_SAK_3_COL_NAME,
        MEKLING_SAK_TOT_COL_NAME,
        MEKLING_TILB_1_COL_NAME,
        MEKLING_TILB_2_COL_NAME,
        MEKLING_TILB_3_COL_NAME,
        MEKLING_TILB_TOT_COL_NAME,
        MEKLING_FLY_1_COL_NAME,
        MEKLING_FLY_2_COL_NAME,
        MEKLING_FLY_3_COL_NAME,
        MEKLING_FLY_TOT_COL_NAME,
        MEKLING_TOT_1_COL_NAME,
        MEKLING_TOT_2_COL_NAME,
        MEKLING_TOT_3_COL_NAME,
        MEKLING_TOT_ALLE_COL_NAME,
    )
}

