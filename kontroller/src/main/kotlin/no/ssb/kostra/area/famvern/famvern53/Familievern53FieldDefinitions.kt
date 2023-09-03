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
import no.ssb.kostra.program.STRING_TYPE
import no.ssb.kostra.program.extension.buildFieldDefinitions

object Familievern53FieldDefinitions : FieldDefinitions {
    override val fieldDefinitions = listOf(
        FieldDefinition(
            number = 1,
            name = FYLKE_NR_COL_NAME,
            dataType = STRING_TYPE,
            size = 2
        ),
        FieldDefinition(
            number = 2,
            name = KONTORNR_COL_NAME,
            dataType = STRING_TYPE,
            size = 3
        ),
        FieldDefinition(
            number = 51,
            name = TILTAK_PUBLIKUM_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 52,
            name = TILTAK_PUBLIKUM_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 61,
            name = VEILEDNING_STUDENTER_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 62,
            name = VEILEDNING_STUDENTER_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 71,
            name = VEILEDNING_HJELPEAP_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 72,
            name = VEILEDNING_HJELPEAP_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 91,
            name = INFO_MEDIA_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 92,
            name = INFO_MEDIA_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 101,
            name = TILSYN_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 102,
            name = TILSYN_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 111,
            name = FORELDREVEIL_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 112,
            name = FORELDREVEIL_TIMER_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 121,
            name = ANNET_TILTAK_COL_NAME,
            size = 4
        ),
        FieldDefinition(
            number = 122,
            name = ANNET_TIMER_COL_NAME,
            size = 4
        )
    ).buildFieldDefinitions()
}