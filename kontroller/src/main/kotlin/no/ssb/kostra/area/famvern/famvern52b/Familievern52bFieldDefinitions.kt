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
import no.ssb.kostra.program.extension.buildFieldDefinitions
import no.ssb.kostra.program.DataType.DATE_TYPE
import no.ssb.kostra.program.DataType.STRING_TYPE

object Familievern52bFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions = listOf(
        FieldDefinition(
            name = REGION_NR_B_COL_NAME,
            dataType = STRING_TYPE,
            size = 6
        ),
        FieldDefinition(
            name = KONTOR_NR_B_COL_NAME,
            dataType = STRING_TYPE,
            size = 3
        ),
        FieldDefinition(
            name = GRUPPE_NR_B_COL_NAME,
            dataType = STRING_TYPE,
            size = 6
        ),
        FieldDefinition(
            name = GRUPPE_NAVN_B_COL_NAME,
            dataType = STRING_TYPE,
            size = 30
        ),
        FieldDefinition(
            name = DATO_GRSTART_B_COL_NAME,
            dataType = DATE_TYPE,
            size = 8,
            datePattern = DATE8_PATTERN,
        ),
        FieldDefinition(
            name = STRUKTUR_GR_B_COL_NAME,
            dataType = STRING_TYPE,
            codeList = listOf(
                Code("1", "Par"),
                Code("2", "Barn (under 18 år)"),
                Code("3", "Individ"),
                Code("4", "Familie"),
                Code("5", "Foreldre")
            ),
        ),
        FieldDefinition(
            name = HOVEDI_GR_B_COL_NAME,
            dataType = STRING_TYPE,
            size = 2,
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
            name = ANTMOTERTOT_IARET_B_COL_NAME,
            size = 3
        ),
        FieldDefinition(
            name = ANTMOTERTOT_OPPR_B_COL_NAME,
            size = 3
        ),
        FieldDefinition(
            name = TIMERTOT_IARET_B_COL_NAME,
            size = 3
        ),
        FieldDefinition(
            name = TIMERTOT_OPPR_B_COL_NAME,
            size = 3
        ),
        FieldDefinition(
            name = ANTDELT_IARET_B_COL_NAME,
            size = 3
        ),
        FieldDefinition(
            name = ANTDELT_OPPR_B_COL_NAME,
            size = 3
        ),
        FieldDefinition(
            name = ANTTER_GRUPPEB_B_COL_NAME,
            size = 2
        ),
        FieldDefinition(
            name = TOLK_B_COL_NAME,
            dataType = STRING_TYPE,
            codeList = listOf(
                Code("1", "Ja"),
                Code("2", "Nei")
            ),
        ),
        FieldDefinition(
            name = STATUS_ARETSSL_B_COL_NAME,
            dataType = STRING_TYPE,
            codeList = listOf(
                Code("1", "Gruppebehandlingen ikke avsluttet i inneværende år"),
                Code("2", "Avsluttet")
            ),
        ),
        FieldDefinition(
            name = DATO_GRAVSLUTN_B_COL_NAME,
            dataType = DATE_TYPE,
            size = 8,
            datePattern = DATE8_PATTERN,
        )
    ).buildFieldDefinitions()
}