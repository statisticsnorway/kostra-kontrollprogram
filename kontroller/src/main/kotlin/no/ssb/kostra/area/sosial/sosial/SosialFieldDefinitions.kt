package no.ssb.kostra.area.sosial.sosial

import no.ssb.kostra.area.FieldDefinitions
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_10_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_11_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_12_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_1_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_2_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_3_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_4_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_5_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_6_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_7_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_8_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_9_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BOSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BYDELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.DISTRIKTSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.EKTSTAT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.FAAT_INDIVIDUELL_PLAN_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.GITT_OKONOMIRAD_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.KJONN_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_10_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_11_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_12_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_1_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_2_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_3_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_4_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_5_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_6_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_7_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_8_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_9_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.PERSON_DUF_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.SANKSJONANDRE_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.SANKSJONRED_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_10_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_11_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_12_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_1_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_2_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_3_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_4_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_5_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_6_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_7_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_8_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_9_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.UTBETDATO_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.UTBETTOMDATO_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VEDTAKAKT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VEDTAKARB_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VEDTAKDATO_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VEDTAKGRUNN_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VEDTAKHELSE_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARANNET_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARARBEID_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARDIGPLAN_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARHELSE_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARJOBBLOG_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARJOBBTILB_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARKURS_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARLIVSH_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKAROKRETT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSAMEKT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSAMT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSOSLOV_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARUTD_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VKLO_COL_NAME
import no.ssb.kostra.program.*

object SosialFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> = listOf(
        FieldDefinition(
            number = 1,
            name = KOMMUNE_NR_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 1,
            to = 4,
        ),
        FieldDefinition(
            number = 2,
            name = VERSION_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 5,
            to = 6,
        ),
        FieldDefinition(
            number = 3,
            name = BYDELSNR_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 7,
            to = 8,
        ),
        FieldDefinition(
            number = 4,
            name = DISTRIKTSNR_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 9,
            to = 10,
        ),
        FieldDefinition(
            number = 5,
            name = PERSON_JOURNALNR_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 11,
            to = 18,

        ),
        FieldDefinition(
            number = 6,
            name = PERSON_FODSELSNR_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 19,
            to = 29,
        ),
        FieldDefinition(
            number = 7,
            name = PERSON_DUF_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 30,
            to = 41,
        ),
        FieldDefinition(
            number = 8, name = KJONN_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 42, 42,
            codeList = listOf(
                Code("1", "Mann"),
                Code("2", "Kvinne")
            ),
        ),
        FieldDefinition(
            number = 9,
            name = EKTSTAT_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 43,
            to = 43,
            codeList = listOf(
                Code("1", "Ugift"),
                Code("2", "Gift"),
                Code("3", "Samboer"),
                Code("4", "Skilt/separert"),
                Code("5", "Enke/enkemann")
            ),
        ),
        FieldDefinition(
            number = 101,
            name = HAR_BARN_UNDER_18_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 44,
            to = 44,
            codeList = listOf(
                Code("1", "Ja"),
                Code("2", "Nei")
            ),
        ),
        FieldDefinition(
            number = 102,
            name = ANT_BARN_UNDER_18_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 45,
            to = 46,
        ),
        FieldDefinition(
            number = 11,
            name = VKLO_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 47,
            to = 47,
            codeList = listOf(
                Code("1", "Arbeidsinntekt"),
                Code("2", "Kursstønad/lønn i arbeidsmarkedstiltak"),
                Code("3", "Trygd/pensjon"),
                Code("4", "Stipend/lån"),
                Code("5", "Sosialhjelp"),
                Code("6", "Introduksjonsstøtte"),
                Code("7", "Ektefelle/samboers arbeidsinntekt"),
                Code("8", "Kvalifiseringsstønad"),
                Code("9", "Annen inntekt")
            ),
        ),
        FieldDefinition(
            number = 12,
            name = TRYGDESIT_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 48,
            to = 49,
            codeList = listOf(
                Code("01", "Sykepenger"),
                Code("02", "Dagpenger"),
                Code("04", "Uføretrygd"),
                Code("05", "Overgangsstønad"),
                Code("06", "Etterlattepensjon"),
                Code("07", "Alderspensjon"),
                Code("09", "Supplerende stønad (kort botid)"),
                Code("10", "Annen trygd"),
                Code("11", "Arbeidsavklaringspenger"),
                Code("12", "Har ingen trygd/pensjon")
            ),
        ),
        FieldDefinition(
            number = 13,
            name = ARBSIT_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 50,
            to = 51,
            codeList = listOf(
                Code("01", "Arbeid, heltid"),
                Code("02", "Arbeid, deltid"),
                Code("03", "Under utdanning"),
                Code("04", "Ikke arbeidssøker"),
                Code("05", "Arbeidsmarkedstiltak (statlig)"),
                Code("06", "Kommunalt tiltak"),
                Code("07", "Registrert arbeidsledig"),
                Code("08", "Arbeidsledig, men ikke registrert hos NAV"),
                Code("09", "Introduksjonsordning"),
                Code("10", "Kvalifiseringsprogram")
            ),

        ),
        FieldDefinition(
            number = 141,
            name = STMND_1_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 52,
            to = 53,
            codeList = listOf(
                Code("01", "Januar")
            ),
        ),
        FieldDefinition(
            number = 142,
            name = STMND_2_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 54,
            to = 55,
            codeList = listOf(
                Code("02", "Februar")
            ),
        ),
        FieldDefinition(
            number = 143,
            name = STMND_3_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 56,
            to = 57,
            codeList = listOf(
                Code("03", "Mars")
            ),
        ),
        FieldDefinition(
            number = 144,
            name = STMND_4_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 58,
            to = 59,
            codeList = listOf(
                Code("04", "April")
            ),
        ),
        FieldDefinition(
            number = 145,
            name = STMND_5_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 60,
            to = 61,
            codeList = listOf(
                Code("05", "Mai")
            ),
        ),
        FieldDefinition(
            number = 146,
            name = STMND_6_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 62,
            to = 63,
            codeList = listOf(
                Code("06", "Juni")
            ),

        ),
        FieldDefinition(
            number = 147,
            name = STMND_7_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 64,
            to = 65,
            codeList = listOf(
                Code("07", "Juli")
            ),
        ),
        FieldDefinition(
            number = 148,
            name = STMND_8_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 66,
            67,
            codeList = listOf(
                Code("08", "August")
            ),
        ),
        FieldDefinition(
            number = 149,
            name = STMND_9_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 68,
            to = 69,
            codeList = listOf(
                Code("09", "September")
            ),
        ),
        FieldDefinition(
            number = 1410,
            name = STMND_10_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 70,
            to = 71,
            codeList = listOf(
                Code("10", "Oktober")
            ),
        ),
        FieldDefinition(
            number = 1411,
            name = STMND_11_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 72,
            to = 73,
            codeList = listOf(
                Code("11", "November")
            ),
        ),
        FieldDefinition(
            number = 1412,
            name = STMND_12_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 74,
            to = 75,
            codeList = listOf(
                Code("12", "Desember")
            ),
        ),
        FieldDefinition(
            number = 151,
            name = BIDRAG_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 76,
            to = 82,
        ),
        FieldDefinition(
            152,
            name = LAAN_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 83,
            to = 89,
        ),
        FieldDefinition(
            number = 161,
            name = BIDRAG_1_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 90,
            to = 96,
        ),
        FieldDefinition(
            number = 162,
            name = LAAN_1_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 97,
            to = 103,
        ),
        FieldDefinition(
            number = 163,
            name = BIDRAG_2_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 104,
            to = 110,
        ),
        FieldDefinition(
            number = 164,
            name = LAAN_2_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 111,
            to = 117,
        ),
        FieldDefinition(
            number = 165,
            name = BIDRAG_3_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 118,
            to = 124,
        ),
        FieldDefinition(
            number = 166,
            name = LAAN_3_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 125,
            to = 131,
        ),
        FieldDefinition(
            number = 167,
            name = BIDRAG_4_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 132,
            to = 138,
        ),
        FieldDefinition(
            number = 168,
            name = LAAN_4_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 139,
            to = 145,
        ),
        FieldDefinition(
            number = 169,
            name = BIDRAG_5_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 146,
            to = 152,
        ),
        FieldDefinition(
            number = 1610,
            name = LAAN_5_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 153,
            to = 159,
        ),
        FieldDefinition(
            number = 1611,
            name = BIDRAG_6_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 160,
            to = 166,
        ),
        FieldDefinition(
            number = 1612,
            name = LAAN_6_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 167,
            to = 173,
        ),
        FieldDefinition(
            number = 1613,
            name = BIDRAG_7_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 174,
            to = 180,
        ),
        FieldDefinition(
            number = 1614,
            name = LAAN_7_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 181,
            to = 187,
        ),
        FieldDefinition(
            number = 1615,
            name = BIDRAG_8_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 188,
            to = 194,
        ),
        FieldDefinition(
            number = 1616,
            name = LAAN_8_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 195,
            to = 201,
        ),
        FieldDefinition(
            number = 1617,
            name = BIDRAG_9_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 202,
            to = 208,
        ),
        FieldDefinition(
            number = 1618,
            name = LAAN_9_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 209,
            to = 215,
        ),
        FieldDefinition(
            number = 1619,
            name = BIDRAG_10_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 216,
            to = 222,
        ),
        FieldDefinition(
            number = 1620,
            name = LAAN_10_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 223,
            to = 229,
        ),
        FieldDefinition(
            number = 1621,
            name = BIDRAG_11_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 230,
            to = 236,
        ),
        FieldDefinition(
            number = 1622,
            name = LAAN_11_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 237,
            to = 243,
        ),
        FieldDefinition(
            number = 1623,
            name = BIDRAG_12_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 244,
            to = 250,
        ),
        FieldDefinition(
            number = 1624,
            name = LAAN_12_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 251,
            to = 257,
        ),
        FieldDefinition(
            number = 17,
            name = GITT_OKONOMIRAD_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 258,
            to = 258,
            codeList = listOf(
                Code("1", "Ja"),
                Code("2", "Nei")
            ),
        ),
        FieldDefinition(
            number = 18,
            name = FAAT_INDIVIDUELL_PLAN_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 259,
            to = 259,
            codeList = listOf(
                Code("1", "Ja"),
                Code("2", "Nei")
            ),
        ),
        FieldDefinition(
            number = 19,
            name = SAKSBEHANDLER_COL_NAME,
            dataType = STRING_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 260,
            to = 269,
        ),
        FieldDefinition(
            number = 20,
            name = BOSIT_COL_NAME,
            dataType = INTEGER_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 270,
            to = 270,
            codeList = listOf(
                Code("1", "Bor i leid privat bolig"),
                Code("2", "Bor i leid kommunal bolig"),
                Code("3", "Bor i eid bolig"),
                Code("4", "Er uten bolig"),
                Code("5", "Annet"),
                Code("6", "Bor i institusjon")
            ),
        ),
        FieldDefinition(
            number = 21,
            name = VILKARSOSLOV_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 271,
            to = 271,
            codeList = listOf(
                Code("1", "Ja"),
                Code("2", "Nei")
            ),
        ),
        FieldDefinition(
            number = 22,
            name = VILKARSAMEKT_COL_NAME,
            dataType = STRING_TYPE,
            viewType = DROPDOWNLIST_VIEWTYPE,
            from = 272,
            to = 272,
            codeList = listOf(
                Code("1", "Ja"),
                Code("2", "Nei")
            ),
        ),
        FieldDefinition(
            number = 23,
            name = UTBETDATO_COL_NAME,
            dataType = DATE_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 273,
            to = 278,
            datePattern = DATE6_PATTERN,
        ),
        FieldDefinition(
            number = 24,
            name = UTBETTOMDATO_COL_NAME,
            dataType = DATE_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 279,
            to = 284,
            datePattern = DATE6_PATTERN
        ),
        FieldDefinition(
            number = 251,
            name = VILKARARBEID_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 285,
            to = 286,
            codeList = listOf(
                Code("16", "Delta på arbeidstrening/arbeidspraksis")
            ),
        ),
        FieldDefinition(
            number = 252,
            name = VILKARKURS_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 287,
            to = 288,
            codeList = listOf(
                Code("17", "Delta på arbeidsrettede kurs, opplæring eller jobbsøkingskurs")
            ),
        ),
        FieldDefinition(
            number = 254,
            name = VILKARUTD_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 289,
            to = 290,
            codeList = listOf(
                Code("04", "Benytte rett til skole")
            ),
        ),
        FieldDefinition(
            number = 256,
            name = VILKARJOBBLOG_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 291,
            to = 292,
            codeList = listOf(
                Code("06", "Registrere seg som arbeidssøker/føre jobblogg")
            ),
        ),
        FieldDefinition(
            number = 257,
            name = VILKARJOBBTILB_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 293,
            to = 294,
            codeList = listOf(
                Code("07", "Ta imot et konkret jobbtilbud")
            ),
        ),
        FieldDefinition(
            number = 258,
            name = VILKARSAMT_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 295,
            to = 296,
            codeList = listOf(
                Code("08", "Møte til veiledningssamtaler")
            ),
        ),
        FieldDefinition(
            number = 2510,
            name = VILKAROKRETT_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 297,
            to = 298,
            codeList = listOf(
                Code("10", "Gjøre gjeldende andre økonomiske rettigheter")
            ),
        ),
        FieldDefinition(
            number = 2511,
            name = VILKARLIVSH_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 299,
            to = 300,
            codeList = listOf(
                Code("18", "Realisere formuesgoder/ redusere boutgifter")
            ),
        ),
        FieldDefinition(
            number = 2514,
            name = VILKARHELSE_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 301,
            to = 302,
            codeList = listOf(
                Code("14", "Oppsøke helsetjenester/ lege")
            ),
        ),
        FieldDefinition(
            number = 2515,
            name = VILKARANNET_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 303,
            to = 304,
            codeList = listOf(
                Code("15", "Annet")
            ),
        ),
        FieldDefinition(
            number = 2516,
            name = VILKARDIGPLAN_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 305,
            to = 306,
            codeList = listOf(
                Code("19", "Bruke og følge opp digital aktivitetsplan")
            ),
        ),
        FieldDefinition(
            number = 26,
            name = VEDTAKDATO_COL_NAME,
            dataType = DATE_TYPE,
            viewType = TEXTBOX_VIEWTYPE,
            from = 307,
            to = 312,
            datePattern = DATE6_PATTERN,
        ),
        FieldDefinition(
            number = 271,
            name = VEDTAKARB_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 313,
            to = 314,
            codeList = listOf(
                Code("01", "Mottaker er i arbeid")
            ),
        ),
        FieldDefinition(
            number = 272,
            name = VEDTAKAKT_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 315,
            to = 316,
            codeList = listOf(
                Code(
                    "02",
                    "Mottaker er allerede i aktivitet knyttet til mottak av statlig eller annen kommunal ytelse"
                )
            ),
        ),
        FieldDefinition(
            number = 273,
            name = VEDTAKHELSE_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 317,
            to = 318,
            codeList = listOf(
                Code("03", "Mottakers helsemessige eller sosiale situasjon hindrer deltakelse i aktivitet")
            ),
        ),
        FieldDefinition(
            number = 274,
            name = VEDTAKGRUNN_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 319,
            to = 320,
            codeList = listOf(
                Code("04", "Andre tungtveiende grunner taler mot at det stilles vilkår om aktivitet")
            ),
        ),
        FieldDefinition(
            number = 281,
            name = SANKSJONRED_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 321,
            to = 322,
            codeList = listOf(
                Code("01", "Redusert stønad")
            ),
        ),
        FieldDefinition(
            number = 282,
            name = SANKSJONANDRE_COL_NAME,
            dataType = STRING_TYPE,
            viewType = CHECKBOX_VIEWTYPE,
            from = 323,
            to = 324,
            codeList = listOf(
                Code("02", "Andre konsekvenser")
            ),
        )
    )
}