package no.ssb.kostra.area.famvern.famvern53

import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.ANNET_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.ANNET_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.FORELDREVEIL_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.FORELDREVEIL_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.INFO_MEDIA_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.INFO_MEDIA_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.TILSYN_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.TILSYN_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.TILTAK_PUBLIKUM_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.TILTAK_PUBLIKUM_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.VEILEDNING_HJELPEAP_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.VEILEDNING_HJELPEAP_TIMER_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.VEILEDNING_STUDENTER_TILTAK_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.VEILEDNING_STUDENTER_TIMER_COL_NAME

object Familievern53Constants {
    data class Rule010Mapping(
        val kategori: String,
        val tiltakField: String,
        val timerField: String
    )

    val rule010Mappings = listOf(
        Rule010Mapping(
            "Andre tiltak mot publikum",
            TILTAK_PUBLIKUM_TILTAK_COL_NAME,
            TILTAK_PUBLIKUM_TIMER_COL_NAME
        ),
        Rule010Mapping(
            "Undervisning/veiledning studenter",
            VEILEDNING_STUDENTER_TILTAK_COL_NAME,
            VEILEDNING_STUDENTER_TIMER_COL_NAME
        ),
        Rule010Mapping(
            "Veiledning/konsultasjon for hjelpeapparatet",
            VEILEDNING_HJELPEAP_TILTAK_COL_NAME,
            VEILEDNING_HJELPEAP_TIMER_COL_NAME
        ),
        Rule010Mapping(
            "Informasjon i media",
            INFO_MEDIA_TILTAK_COL_NAME,
            INFO_MEDIA_TIMER_COL_NAME
        ),
        Rule010Mapping(
            "Tilsyn",
            TILSYN_TILTAK_COL_NAME,
            TILSYN_TIMER_COL_NAME
        ),
        Rule010Mapping(
            "Foreldreveiledning",
            FORELDREVEIL_TILTAK_COL_NAME,
            FORELDREVEIL_TIMER_COL_NAME
        ),
        Rule010Mapping(
            "Annet",
            ANNET_TILTAK_COL_NAME,
            ANNET_TIMER_COL_NAME
        )
    )
}