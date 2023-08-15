package no.ssb.kostra.area.famvern.famvern52b

import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.ANTDELT_IARET_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.ANTDELT_OPPR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.ANTMOTERTOT_IARET_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.ANTMOTERTOT_OPPR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.ANTTER_GRUPPEB_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.DATO_GRAVSLUTN_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.DATO_GRSTART_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NAVN_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.HOVEDI_GR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.KONTOR_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.REGION_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.STATUS_ARETSSL_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.STRUKTUR_GR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.TIMERTOT_IARET_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.TIMERTOT_OPPR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.TOLK_B_COL_NAME
import no.ssb.kostra.program.*

object Familievern52bFieldDefinitions : FieldDefinitions() {
    override val fieldDefinitions = listOf(
        FieldDefinition(
            number = 1,
            name = REGION_NR_B_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 1,
            to = 6,
        ),
        FieldDefinition(
            number = 2,
            name = KONTOR_NR_B_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 7,
            to = 9,
        ),
        FieldDefinition(
            number = 3,
            name = GRUPPE_NR_B_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 10,
            to = 15,
        ),
        FieldDefinition(
            number = 4,
            name = GRUPPE_NAVN_B_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 16,
            to = 45,
        ),
        FieldDefinition(
            number = 5,
            name = DATO_GRSTART_B_COL_NAME,
            dataType = DATE_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 46,
            to = 53,
            datePattern = DATE8_PATTERN,
        ),
        FieldDefinition(
            number = 6,
            name = STRUKTUR_GR_B_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 54,
            to = 54,
            codeList = listOf(
                Code("1", "Par"),
                Code("2", "Barn (under 18 år)"),
                Code("3", "Individ"),
                Code("4", "Familie"),
                Code("5", "Foreldre")
            ),
        ),
        FieldDefinition(
            number = 7,
            name = HOVEDI_GR_B_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 55,
            to = 56,
            codeList = listOf(
                Code("01", "Samlivskurs"),
                Code("02", "Samlivsbrudd"),
                Code("03", "Samarbeid om barn etter brudd"),
                Code("04", "Barn som har opplevd brudd i familien"),
                Code("05", "Vold/overgrep"),
                Code("06", "Sinnemestring"),
                Code("07", "Kultur-/Minoritetsspørsmål"),
                Code("08", "Foreldreveiledning"),
                Code("09", "Foreldre som har mistet omsorgen for egne barn"),
                Code("10", "Andre alvorlige hendelser"),
                Code("11", "Annet, spesifiser")
            ),
        ),
        FieldDefinition(
            number = 81,
            name = ANTMOTERTOT_IARET_B_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 57,
            to = 59,
        ),
        FieldDefinition(
            number = 82,
            name = ANTMOTERTOT_OPPR_B_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 60,
            to = 62,
        ),
        FieldDefinition(
            number = 91,
            name = TIMERTOT_IARET_B_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 63,
            to = 65,
        ),
        FieldDefinition(
            number = 92,
            name = TIMERTOT_OPPR_B_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 66,
            to = 68,
        ),
        FieldDefinition(
            number = 101,
            name = ANTDELT_IARET_B_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 69,
            to = 71,
        ),
        FieldDefinition(
            number = 102,
            name = ANTDELT_OPPR_B_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 72,
            to = 74,
        ),
        FieldDefinition(
            number = 11,
            name = ANTTER_GRUPPEB_B_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 75,
            to = 76,
        ),
        FieldDefinition(
            number = 12,
            name = TOLK_B_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 77,
            to = 77,
            codeList = listOf(
                Code("1", "Ja"),
                Code("2", "Nei")
            ),
        ),
        FieldDefinition(
            number = 13,
            name = STATUS_ARETSSL_B_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 78,
            to = 78,
            codeList = listOf(
                Code("1", "Gruppebehandlingen ikke avsluttet i inneværende år"),
                Code("2", "Avsluttet")
            ),
        ),
        FieldDefinition(
            number = 14,
            name = DATO_GRAVSLUTN_B_COL_NAME,
            dataType = DATE_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 79,
            to = 86,
            datePattern = DATE8_PATTERN,
        )
    )

}