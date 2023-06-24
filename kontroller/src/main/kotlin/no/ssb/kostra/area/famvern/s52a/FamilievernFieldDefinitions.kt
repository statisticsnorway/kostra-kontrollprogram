package no.ssb.kostra.area.famvern.s52a

import no.ssb.kostra.area.FieldDefinitions
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.FieldDefinition

class FamilievernFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions = listOf(
        FieldDefinition(
            1, "REGION_NR_A",
            "String",
            "textBox",
            1, 6, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            2, "KONTOR_NR_A",
            "String",
            "textBox",
            7, 9, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            3, "JOURNAL_NR_A",
            "String",
            "textBox",
            10, 18, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            4, "HENV_DATO_A",
            "Date",
            "textBox",
            19, 26, emptyList(),
            "ddMMyyyy",
            false
        ),
        FieldDefinition(
            5, "KONTAKT_TIDL_A",
            "String",
            "dropDownList",
            27, 27, listOf(
                Code("1", "under 6 md siden"),
                Code("2", "mellom 6 md og 3 år siden"),
                Code("3", "3 år eller mer siden"),
                Code("4", "har ikke vært i kontakt med familievernet tidligere")
            ),
            "",
            false
        ),
        FieldDefinition(
            6, "HENV_GRUNN_A",
            "String",
            "dropDownList",
            28, 28, listOf(
                Code("1", "Parforholdet"),
                Code("2", "Foreldresamarbeid/- veiledning"),
                Code("3", "Andre eller sammensatte problemer i familien"),
                Code("4", "Hjelp til barn og ungdom")
            ),
            "",
            false
        ),
        FieldDefinition(
            7, "PRIMK_KJONN_A",
            "String",
            "dropDownList",
            29, 29, listOf(
                Code("1", "Mann/gutt"), Code("2", "Kvinne/jente")
            ),
            "",
            false
        ),
        FieldDefinition(
            8, "PRIMK_FODT_A",
            "Integer",
            "textBox",
            30, 33, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            91, "PRIMK_SIVILS_A",
            "String",
            "dropDownList",
            34, 34, listOf(
                Code("1", "Gift"),
                Code("2", "Registrert partner"),
                Code("3", "Samboer"),
                Code("4", "Lever ikke i samliv")
            ),
            "",
            false
        ),
        FieldDefinition(
            92, "FORMELL_SIVILS_A",
            "String",
            "dropDownList",
            35, 35, listOf(
                Code("1", "Ugift"),
                Code("2", "Gift"),
                Code("3", "Registrert partner"),
                Code("4", "Separert / separert partner"),
                Code("5", "Skilt / skilt partner"),
                Code("6", "Enke / enkemann / gjenlevende partner")
            ),
            "",
            false
        ),
        FieldDefinition(
            10, "PRIMK_SAMBO_A",
            "String",
            "dropDownList",
            36, 36, listOf(
                Code("1", "Partner (og eventuelt barn)"),
                Code("2", "Barn"),
                Code("3", "Foreldre / Andre omsorgspersoner"),
                Code("4", "Andre"),
                Code("5", "Ikke sammen med andre")
            ),
            "",
            false
        ),
        FieldDefinition(
            11, "PRIMK_ARBSIT_A",
            "String",
            "dropDownList",
            37, 37, listOf(
                Code("1", "Arbeid heltid"),
                Code("2", "Arbeid deltid"),
                Code("3", "Arbeidssøker"),
                Code("4", "Under utdanning"),
                Code("5", "Fødselspermisjon / Fedrekvote"),
                Code("6", "Annen inntekt fre NAV"),
                Code("7", "Uten inntekt")
            ),
            "",
            false
        ),
        FieldDefinition(
            12, "PRIMK_VSRELASJ_A",
            "String",
            "dropDownList",
            38, 38, listOf(
                Code("1", "Partner"),
                Code("2", "Ekspartner"),
                Code("3", "Forelder"),
                Code("4", "Sønn/datter under 18 år"),
                Code("5", "Sønn/datter 18 år eller eldre"),
                Code("6", "Øvrig familie"),
                Code("7", "Venn"),
                Code("8", "Annet")
            ),
            "",
            false
        ),
        FieldDefinition(
            13, "PART_LENGDE_A",
            "String",
            "dropDownList",
            39, 39, listOf(
                Code("1", "Har ikke bodd sammen"),
                Code("2", "Under 2 år"),
                Code("3", "2 - 4 år"),
                Code("4", "5 - 9 år"),
                Code("5", "10 - 19 år"),
                Code("6", "20 år eller mer")
            ),
            "",
            false
        ),
        FieldDefinition(
            14, "EKSPART_LENGDE_A",
            "String",
            "dropDownList",
            40, 40, listOf(
                Code("1", "Har ikke bodd sammen"),
                Code("2", "Under 2 år"),
                Code("3", "2 - 4 år"),
                Code("4", "5 - 9 år"),
                Code("5", "10 - 19 år"),
                Code("6", "20 år eller mer")
            ),
            "",
            false
        ),
        FieldDefinition(
            15, "EKSPART_VARIGH_A",
            "String",
            "dropDownList",
            41, 41, listOf(
                Code("1", "Har ikke bodd sammen"),
                Code("2", "Under 2 år"),
                Code("3", "2 - 4 år"),
                Code("4", "5 - 9 år"),
                Code("5", "10 - 19 år"),
                Code("6", "20 år eller mer")
            ),
            "",
            false
        ),
        FieldDefinition(
            161, "PRIMKREL_PART_A",
            "Integer",
            "checkBox",
            42, 42, listOf(
                Code("1", "Partner")
            ),
            "",
            false
        ),
        FieldDefinition(
            162, "PRIMKREL_EKSPART_A",
            "Integer",
            "checkBox",
            43, 43, listOf(
                Code("1", "Ekspartner")
            ),
            "",
            false
        ),
        FieldDefinition(
            163, "PRIMKREL_FORELD_A",
            "Integer",
            "checkBox",
            44, 44, listOf(
                Code("1", "Forelder")
            ),
            "",
            false
        ),
        FieldDefinition(
            164, "PRIMKREL_BU18_A",
            "Integer",
            "checkBox",
            45, 45, listOf(
                Code("1", "Sønn/datter (under 18 år)")
            ),
            "",
            false
        ),
        FieldDefinition(
            165, "PRIMKREL_B18_A",
            "Integer",
            "checkBox",
            46, 46, listOf(
                Code("1", "Sønn/datter (18 år eller eldre)")
            ),
            "",
            false
        ),
        FieldDefinition(
            166, "PRIMKREL_OVRIG_A",
            "Integer",
            "checkBox",
            47, 47, listOf(
                Code("1", "Øvrig familie")
            ),
            "",
            false
        ),
        FieldDefinition(
            167, "PRIMKREL_VENN_A",
            "Integer",
            "checkBox",
            48, 48, listOf(
                Code("1", "Venner")
            ),
            "",
            false
        ),
        FieldDefinition(
            168, "PRIMKREL_ANDRE_A",
            "Integer",
            "checkBox",
            49, 49, listOf(
                Code("1", "Annet")
            ),
            "",
            false
        ),
        FieldDefinition(
            17, "FORSTE_SAMT_A",
            "Date",
            "textBox",
            50, 57, emptyList(),
            "ddMMyyyy",
            true
        ),
        FieldDefinition(
            18, "SAMT_FORHOLD_A",
            "String",
            "dropDownList",
            58, 58, listOf(
                Code("1", "Forhold hos kontoret"), Code("2", "Forhold hos klient/klientene")
            ),
            "",
            false
        ),
        FieldDefinition(
            191, "TEMA_PARREL_A",
            "Integer",
            "checkBox",
            59, 59, listOf(
                Code("1", "Styrke parforholdet")
            ),
            "",
            false
        ),
        FieldDefinition(
            192, "TEMA_AVKLAR_A",
            "Integer",
            "checkBox",
            60, 60, listOf(
                Code("1", "Avklare/avslutte parforholdet")
            ),
            "",
            false
        ),
        FieldDefinition(
            193, "TEMA_SAMLBRUDD_A",
            "Integer",
            "checkBox",
            61, 61, listOf(
                Code("1", "Samlivsbrudd i familien")
            ),
            "",
            false
        ),
        FieldDefinition(
            194, "TEMA_SAMSPILL_A",
            "Integer",
            "checkBox",
            62, 62, listOf(
                Code("1", "Samspillsvansker")
            ),
            "",
            false
        ),
        FieldDefinition(
            195, "TEMA_BARNSIT_A",
            "Integer",
            "checkBox",
            63, 63, listOf(
                Code("1", "Barnets opplevelse av sin livssituasjon")
            ),
            "",
            false
        ),
        FieldDefinition(
            196, "TEMA_BARNFOR_A",
            "Integer",
            "checkBox",
            64, 64, listOf(
                Code("1", "Barnets situasjon i foreldrenes konflikt")
            ),
            "",
            false
        ),
        FieldDefinition(
            197, "TEMA_BOSTED_A",
            "Integer",
            "checkBox",
            65, 65, listOf(
                Code("1", "bostedsavklaring/samvær")
            ),
            "",
            false
        ),
        FieldDefinition(
            198, "TEMA_FORELDRE_A",
            "Integer",
            "checkBox",
            66, 66, listOf(
                Code("1", "foreldrerollen")
            ),
            "",
            false
        ),
        FieldDefinition(
            199, "TEMA_FORBARN_A",
            "Integer",
            "checkBox",
            67, 67, listOf(
                Code("1", "foreldre-barn-relasjonen")
            ),
            "",
            false
        ),
        FieldDefinition(
            1910, "TEMA_FLERGEN_A",
            "Integer",
            "checkBox",
            68, 68, listOf(
                Code("1", "flergenerasjonsproblematikk")
            ),
            "",
            false
        ),
        FieldDefinition(
            1911, "TEMA_SAMBARN_A",
            "Integer",
            "checkBox",
            69, 69, listOf(
                Code("1", "samarb. om felles barn (foreldre bor ikke sammen)")
            ),
            "",
            false
        ),
        FieldDefinition(
            1912, "TEMA_SÆRBARN_A",
            "Integer",
            "checkBox",
            70, 70, listOf(
                Code("1", "særkullsbarn og/eller ny familie")
            ),
            "",
            false
        ),
        FieldDefinition(
            1913, "TEMA_KULTUR_A",
            "Integer",
            "checkBox",
            71, 71, listOf(
                Code("1", "kultur-/minoritetsspørsmål")
            ),
            "",
            false
        ),
        FieldDefinition(
            1914, "TEMA_TVANG_A",
            "Integer",
            "checkBox",
            72, 72, listOf(
                Code("1", "tvangsekteskap")
            ),
            "",
            false
        ),
        FieldDefinition(
            1915, "TEMA_RUS_A",
            "Integer",
            "checkBox",
            73, 73, listOf(
                Code("1", "bruk av rusmidler")
            ),
            "",
            false
        ),
        FieldDefinition(
            1916, "TEMA_SYKD_A",
            "Integer",
            "checkBox",
            74, 74, listOf(
                Code("1", "sykdom / nedsatt funksjonsevne")
            ),
            "",
            false
        ),
        FieldDefinition(
            1917, "TEMA_VOLD_A",
            "Integer",
            "checkBox",
            75, 75, listOf(
                Code("1", "fysisk / psykisk vold / seksuelt misbruk")
            ),
            "",
            false
        ),
        FieldDefinition(
            1918, "TEMA_ALVH_A",
            "Integer",
            "checkBox",
            76, 76, listOf(
                Code("1", "annen alvorlig hendelse")
            ),
            "",
            false
        ),
        FieldDefinition(
            20, "HOVEDF_BEHAND_A",
            "String",
            "dropDownList",
            77, 77, listOf(
                Code("1", "Parsamtale"),
                Code("2", "Foreldresamtale"),
                Code("3", "Familiesamtale"),
                Code("4", "Individualsamtale")
            ),
            "",
            false
        ),
        FieldDefinition(
            21, "BEKYMR_MELD_A",
            "String",
            "dropDownList",
            78, 78, listOf(
                Code("1", "Ja"), Code("2", "Nei")
            ),
            "",
            false
        ),
        FieldDefinition(
            221, "DELT_PARTNER_A",
            "String",
            "dropDownList",
            79, 79, listOf(
                Code("1", "Ja"), Code("2", "Nei")
            ),
            "",
            false
        ),
        FieldDefinition(
            222, "DELT_EKSPART_A",
            "String",
            "dropDownList",
            80, 80, listOf(
                Code("1", "Ja"), Code("2", "Nei")
            ),
            "",
            false
        ),
        FieldDefinition(
            223, "DELT_BARNU18_A",
            "String",
            "dropDownList",
            81, 81, listOf(
                Code("1", "Ja"), Code("2", "Nei")
            ),
            "",
            false
        ),
        FieldDefinition(
            224, "DELT_BARNO18_A",
            "String",
            "dropDownList",
            82, 82, listOf(
                Code("1", "Ja"), Code("2", "Nei")
            ),
            "",
            false
        ),
        FieldDefinition(
            225, "DELT_FORELDRE_A",
            "String",
            "dropDownList",
            83, 83, listOf(
                Code("1", "Ja"), Code("2", "Nei")
            ),
            "",
            false
        ),
        FieldDefinition(
            226, "DELT_OVRFAM_A",
            "String",
            "dropDownList",
            84, 84, listOf(
                Code("1", "Ja"), Code("2", "Nei")
            ),
            "",
            false
        ),
        FieldDefinition(
            227, "DELT_VENN_A",
            "String",
            "dropDownList",
            85, 85, listOf(
                Code("1", "Ja"), Code("2", "Nei")
            ),
            "",
            false
        ),
        FieldDefinition(
            228, "DELT_ANDR_A",
            "String",
            "dropDownList",
            86, 86, listOf(
                Code("1", "Ja"), Code("2", "Nei")
            ),
            "",
            false
        ),
        FieldDefinition(
            231, "SAMT_PRIMK_A",
            "Integer",
            "textBox",
            87, 88, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            232, "SAMT_PARTNER_A",
            "Integer",
            "textBox",
            89, 90, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            233, "SAMT_EKSPART_A",
            "Integer",
            "textBox",
            91, 92, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            234, "SAMT_BARNU18_A",
            "Integer",
            "textBox",
            93, 94, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            235, "SAMT_BARNO18_A",
            "Integer",
            "textBox",
            95, 96, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            236, "SAMT_FORELDRE_A",
            "Integer",
            "textBox",
            97, 98, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            237, "SAMT_OVRFAM_A",
            "Integer",
            "textBox",
            99, 100, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            238, "SAMT_VENN_A",
            "Integer",
            "textBox",
            101, 102, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            239, "SAMT_ANDRE_A",
            "Integer",
            "textBox",
            103, 104, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            241, "ANTSAMT_HOVEDT_A",
            "Integer",
            "textBox",
            105, 107, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            242, "ANTSAMT_ANDREANS_A",
            "Integer",
            "textBox",
            108, 110, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            251, "ANTSAMT_IARET_A",
            "Integer",
            "textBox",
            111, 113, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            252, "ANTSAMT_OPPR_A",
            "Integer",
            "textBox",
            114, 116, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            261, "TLFSAMT_IARET_A",
            "Integer",
            "textBox",
            117, 119, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            262, "TLFSAMT_OPPR_A",
            "Integer",
            "textBox",
            120, 122, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            271, "TIMER_IARET_A",
            "Integer",
            "textBox",
            123, 125, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            272, "TIMER_OPPR_A",
            "Integer",
            "textBox",
            126, 128, emptyList(),
            "",
            false
        ),
        FieldDefinition(
            28, "TOLK_A",
            "String",
            "dropDownList",
            129, 129, listOf(
                Code("1", "Ja"), Code("2", "Nei")
            ),
            "",
            false
        ),
        FieldDefinition(
            291, "SAMARB_INGEN_A",
            "Integer",
            "checkBox",
            130, 130, listOf(
                Code("1", "Ikke samarbeid med med annen instans")
            ),
            "",
            false
        ),
        FieldDefinition(
            292, "SAMARB_LEGE_A",
            "Integer",
            "checkBox",
            131, 131, listOf(
                Code("1", "Fastlege")
            ),
            "",
            false
        ),
        FieldDefinition(
            293, "SAMARB_HELSE_A",
            "Integer",
            "checkBox",
            132, 132, listOf(
                Code("1", "Helsestasjon / Familiesenter")
            ),
            "",
            false
        ),
        FieldDefinition(
            294, "SAMARB_PSYKH_A",
            "Integer",
            "checkBox",
            133, 133, listOf(
                Code("1", "Psykisk helsevern")
            ),
            "",
            false
        ),
        FieldDefinition(
            295, "SAMARB_JURIST_A",
            "Integer",
            "checkBox",
            134, 134, listOf(
                Code("1", "Jurist")
            ),
            "",
            false
        ),
        FieldDefinition(
            296, "SAMARB_KRISES_A",
            "Integer",
            "checkBox",
            135, 135, listOf(
                Code("1", "Krisesenter")
            ),
            "",
            false
        ),
        FieldDefinition(
            297, "SAMARB_SKOLE_A",
            "Integer",
            "checkBox",
            136, 136, listOf(
                Code("1", "Skole/PP-tjeneste")
            ),
            "",
            false
        ),
        FieldDefinition(
            298, "SAMARB_SOS_A",
            "Integer",
            "checkBox",
            137, 137, listOf(
                Code("1", "NAV")
            ),
            "",
            false
        ),
        FieldDefinition(
            299, "SAMARB_KOMMB_A",
            "Integer",
            "checkBox",
            138, 138, listOf(
                Code("1", "Kommunalt barnevern")
            ),
            "",
            false
        ),
        FieldDefinition(
            2910, "SAMARB_STATB_A",
            "Integer",
            "checkBox",
            139, 139, listOf(
                Code("1", "Statlig barnevern")
            ),
            "",
            false
        ),
        FieldDefinition(
            2911, "SAMARB_ANDRE_A",
            "Integer",
            "checkBox",
            140, 140, listOf(
                Code("1", "Annet")
            ),
            "",
            false
        ),
        FieldDefinition(
            30, "STATUS_ARETSSL_A",
            "String",
            "dropDownList",
            141, 141, listOf(
                Code("1", "Avsluttet etter avtale med klient"),
                Code("2", "Klient uteblitt"),
                Code("3", "Saken ikke avsluttet i inneværende år")
            ),
            "",
            false
        ),
        FieldDefinition(
            31, "HOVEDTEMA_A",
            "String",
            "dropDownList",
            142, 143, listOf(
                Code("01", "styrke parforholdet"),
                Code("02", "avklare/avslutte parforholdet"),
                Code("03", "samlivsbrudd i familien"),
                Code("04", "samspillvansker"),
                Code("05", "barnets opplevelse av sin livssituasjon"),
                Code("06", "barnets situasjon i foreldrenes konflikt"),
                Code("07", "bostedsavklaring/ samvær"),
                Code("08", "foreldrerollen"),
                Code("09", "foreldre-barn-relasjonen"),
                Code("10", "flergenerasjons- problematikk"),
                Code("11", "samarb. om felles barn (foreldre bor ikke sammen)"),
                Code("12", "særkullsbarn og/eller ny familie"),
                Code("13", "kultur-/minoritetsspørsmål"),
                Code("14", "tvangsekteskap"),
                Code("15", "bruk av rusmidler"),
                Code("16", "sykdom / nedsatt funksjonsevne"),
                Code("17", "fysisk / psykisk vold / seksuelt misbruk"),
                Code("18", "annen alvorlig hendelse")
            ),
            "",
            false
        ),
        FieldDefinition(
            32, "DATO_AVSL_A",
            "Date",
            "textBox",
            144, 151, emptyList(),
            "ddMMyyyy",
            false
        )


    )
}

