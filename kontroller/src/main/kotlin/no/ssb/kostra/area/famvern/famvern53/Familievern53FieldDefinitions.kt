package no.ssb.kostra.area.famvern.famvern53

import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.ANNET_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.ANNET_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.FORELDREVEIL_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.FORELDREVEIL_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.FYLKE_NR_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.INFO_MEDIA_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.INFO_MEDIA_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.KONTORNR_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.TILSYN_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.TILSYN_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.TILTAK_PUBLIKUM_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.TILTAK_PUBLIKUM_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.VEILEDNING_HJELPEAP_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.VEILEDNING_HJELPEAP_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.VEILEDNING_STUDENTER_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.VEILEDNING_STUDENTER_TIMER_COL_NAME
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.extension.buildFieldDefinitions
import no.ssb.kostra.program.DataType.STRING_TYPE

object Familievern53FieldDefinitions : FieldDefinitions {
    override val fieldDefinitions = listOf(
        FieldDefinition(
            name = FYLKE_NR_COL_NAME,
            dataType = STRING_TYPE,
            size = 2
        ),
        FieldDefinition(
            name = KONTORNR_COL_NAME,
            dataType = STRING_TYPE,
            size = 3
        ),
        FieldDefinition(
            name = TILTAK_PUBLIKUM_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = TILTAK_PUBLIKUM_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = VEILEDNING_STUDENTER_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = VEILEDNING_STUDENTER_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = VEILEDNING_HJELPEAP_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = VEILEDNING_HJELPEAP_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = INFO_MEDIA_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = INFO_MEDIA_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = TILSYN_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = TILSYN_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = FORELDREVEIL_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = FORELDREVEIL_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = ANNET_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            name = ANNET_TIMER_COL_NAME,
            size = 4
        )
    ).buildFieldDefinitions()
}