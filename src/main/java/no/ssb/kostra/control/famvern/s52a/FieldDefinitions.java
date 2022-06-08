package no.ssb.kostra.control.famvern.s52a;

import no.ssb.kostra.felles.Code;
import no.ssb.kostra.felles.FieldDefinition;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class FieldDefinitions {
    @SuppressWarnings("Duplicates")
    public static List<FieldDefinition> getFieldDefinitions() {
        return List.of(
                new FieldDefinition(1, "REGION_NR_A",
                        "String",
                        "textBox",
                        1, 6
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(2, "KONTOR_NR_A",
                        "String",
                        "textBox",
                        7, 9
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(3, "JOURNAL_NR_A",
                        "String",
                        "textBox",
                        10, 18
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(4, "HENV_DATO_A",
                        "Date",
                        "textBox",
                        19, 26
                        , List.of(),
                        "ddMMyyyy",
                        false),
                new FieldDefinition(5, "KONTAKT_TIDL_A",
                        "String",
                        "dropDownList",
                        27, 27
                        , List.of(
                        new Code("1", "under 6 md siden")
                        , new Code("2", "mellom 6 md og 3 år siden")
                        , new Code("3", "3 år eller mer siden")
                        , new Code("4", "har ikke vært i kontakt med familievernet tidligere")),
                        "",
                        false),
                new FieldDefinition(6, "HENV_GRUNN_A",
                        "String",
                        "dropDownList",
                        28, 28
                        , List.of(
                        new Code("1", "Parforholdet")
                        , new Code("2", "Foreldresamarbeid/- veiledning")
                        , new Code("3", "Andre eller sammensatte problemer i familien")
                        , new Code("4", "Hjelp til barn og ungdom")),
                        "",
                        false),
                new FieldDefinition(7, "PRIMK_KJONN_A",
                        "String",
                        "dropDownList",
                        29, 29
                        , List.of(
                        new Code("1", "Mann/gutt")
                        , new Code("2", "Kvinne/jente")),
                        "",
                        false),
                new FieldDefinition(8, "PRIMK_FODT_A",
                        "Integer",
                        "textBox",
                        30, 33
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(91, "PRIMK_SIVILS_A",
                        "String",
                        "dropDownList",
                        34, 34
                        , List.of(
                        new Code("1", "Gift")
                        , new Code("2", "Registrert partner")
                        , new Code("3", "Samboer")
                        , new Code("4", "Lever ikke i samliv")),
                        "",
                        false),
                new FieldDefinition(92, "FORMELL_SIVILS_A",
                        "String",
                        "dropDownList",
                        35, 35
                        , List.of(
                        new Code("1", "Ugift")
                        , new Code("2", "Gift")
                        , new Code("3", "Registrert partner")
                        , new Code("4", "Separert / separert partner")
                        , new Code("5", "Skilt / skilt partner")
                        , new Code("6", "Enke / enkemann / gjenlevende partner")),
                        "",
                        false),
                new FieldDefinition(10, "PRIMK_SAMBO_A",
                        "String",
                        "dropDownList",
                        36, 36
                        , List.of(
                        new Code("1", "Partner (og eventuelt barn)")
                        , new Code("2", "Barn")
                        , new Code("3", "Foreldre / Andre omsorgspersoner")
                        , new Code("4", "Andre")
                        , new Code("5", "Ikke sammen med andre")),
                        "",
                        false),
                new FieldDefinition(11, "PRIMK_ARBSIT_A",
                        "String",
                        "dropDownList",
                        37, 37
                        , List.of(
                        new Code("1", "Arbeid heltid")
                        , new Code("2", "Arbeid deltid")
                        , new Code("3", "Arbeidssøker")
                        , new Code("4", "Under utdanning")
                        , new Code("5", "Fødselspermisjon / Fedrekvote")
                        , new Code("6", "Annen inntekt fre NAV")
                        , new Code("7", "Uten inntekt")),
                        "",
                        false),
                new FieldDefinition(12, "PRIMK_VSRELASJ_A",
                        "String",
                        "dropDownList",
                        38, 38
                        , List.of(
                        new Code("1", "Partner")
                        , new Code("2", "Ekspartner")
                        , new Code("3", "Forelder")
                        , new Code("4", "Sønn/datter under 18 år")
                        , new Code("5", "Sønn/datter 18 år eller eldre")
                        , new Code("6", "Øvrig familie")
                        , new Code("7", "Venn")
                        , new Code("8", "Annet")),
                        "",
                        false),
                new FieldDefinition(13, "PART_LENGDE_A",
                        "String",
                        "dropDownList",
                        39, 39
                        , List.of(
                        new Code("1", "Har ikke bodd sammen")
                        , new Code("2", "Under 2 år")
                        , new Code("3", "2 - 4 år")
                        , new Code("4", "5 - 9 år")
                        , new Code("5", "10 - 19 år")
                        , new Code("6", "20 år eller mer")),
                        "",
                        false),
                new FieldDefinition(14, "EKSPART_LENGDE_A",
                        "String",
                        "dropDownList",
                        40, 40
                        , List.of(
                        new Code("1", "Har ikke bodd sammen")
                        , new Code("2", "Under 2 år")
                        , new Code("3", "2 - 4 år")
                        , new Code("4", "5 - 9 år")
                        , new Code("5", "10 - 19 år")
                        , new Code("6", "20 år eller mer")),
                        "",
                        false),
                new FieldDefinition(15, "EKSPART_VARIGH_A",
                        "String",
                        "dropDownList",
                        41, 41
                        , List.of(
                        new Code("1", "Har ikke bodd sammen")
                        , new Code("2", "Under 2 år")
                        , new Code("3", "2 - 4 år")
                        , new Code("4", "5 - 9 år")
                        , new Code("5", "10 - 19 år")
                        , new Code("6", "20 år eller mer")),
                        "",
                        false),
                new FieldDefinition(161, "PRIMKREL_PART_A",
                        "Integer",
                        "checkBox",
                        42, 42
                        , List.of(
                        new Code("1", "Partner")),
                        "",
                        false),
                new FieldDefinition(162, "PRIMKREL_EKSPART_A",
                        "Integer",
                        "checkBox",
                        43, 43
                        , List.of(
                        new Code("1", "Ekspartner")),
                        "",
                        false),
                new FieldDefinition(163, "PRIMKREL_FORELD_A",
                        "Integer",
                        "checkBox",
                        44, 44
                        , List.of(
                        new Code("1", "Forelder")),
                        "",
                        false),
                new FieldDefinition(164, "PRIMKREL_BU18_A",
                        "Integer",
                        "checkBox",
                        45, 45
                        , List.of(
                        new Code("1", "Sønn/datter (under 18 år)")),
                        "",
                        false),
                new FieldDefinition(165, "PRIMKREL_B18_A",
                        "Integer",
                        "checkBox",
                        46, 46
                        , List.of(
                        new Code("1", "Sønn/datter (18 år eller eldre)")),
                        "",
                        false),
                new FieldDefinition(166, "PRIMKREL_OVRIG_A",
                        "Integer",
                        "checkBox",
                        47, 47
                        , List.of(
                        new Code("1", "Øvrig familie")),
                        "",
                        false),
                new FieldDefinition(167, "PRIMKREL_VENN_A",
                        "Integer",
                        "checkBox",
                        48, 48
                        , List.of(
                        new Code("1", "Venner")),
                        "",
                        false),
                new FieldDefinition(168, "PRIMKREL_ANDRE_A",
                        "Integer",
                        "checkBox",
                        49, 49
                        , List.of(
                        new Code("1", "Annet")),
                        "",
                        false),
                new FieldDefinition(17, "FORSTE_SAMT_A",
                        "Date",
                        "textBox",
                        50, 57
                        , List.of(),
                        "ddMMyyyy",
                        false),
                new FieldDefinition(18, "SAMT_FORHOLD_A",
                        "String",
                        "dropDownList",
                        58, 58
                        , List.of(
                        new Code("1", "Forhold hos kontoret")
                        , new Code("2", "Forhold hos klient/klientene")),
                        "",
                        false),
                new FieldDefinition(191, "TEMA_PARREL_A",
                        "Integer",
                        "checkBox",
                        59, 59
                        , List.of(
                        new Code("1", "Styrke parforholdet")),
                        "",
                        false),
                new FieldDefinition(192, "TEMA_AVKLAR_A",
                        "Integer",
                        "checkBox",
                        60, 60
                        , List.of(
                        new Code("1", "Avklare/avslutte parforholdet")),
                        "",
                        false),
                new FieldDefinition(193, "TEMA_SAMLBRUDD_A",
                        "Integer",
                        "checkBox",
                        61, 61
                        , List.of(
                        new Code("1", "Samlivsbrudd i familien")),
                        "",
                        false),
                new FieldDefinition(194, "TEMA_SAMSPILL_A",
                        "Integer",
                        "checkBox",
                        62, 62
                        , List.of(
                        new Code("1", "Samspillsvansker")),
                        "",
                        false),
                new FieldDefinition(195, "TEMA_BARNSIT_A",
                        "Integer",
                        "checkBox",
                        63, 63
                        , List.of(
                        new Code("1", "Barnets opplevelse av sin livssituasjon")),
                        "",
                        false),
                new FieldDefinition(196, "TEMA_BARNFOR_A",
                        "Integer",
                        "checkBox",
                        64, 64
                        , List.of(
                        new Code("1", "Barnets situasjon i foreldrenes konflikt")),
                        "",
                        false),
                new FieldDefinition(197, "TEMA_BOSTED_A",
                        "Integer",
                        "checkBox",
                        65, 65
                        , List.of(
                        new Code("1", "bostedsavklaring/samvær")),
                        "",
                        false),
                new FieldDefinition(198, "TEMA_FORELDRE_A",
                        "Integer",
                        "checkBox",
                        66, 66
                        , List.of(
                        new Code("1", "foreldrerollen")),
                        "",
                        false),
                new FieldDefinition(199, "TEMA_FORBARN_A",
                        "Integer",
                        "checkBox",
                        67, 67
                        , List.of(
                        new Code("1", "foreldre-barn-relasjonen")),
                        "",
                        false),
                new FieldDefinition(1910, "TEMA_FLERGEN_A",
                        "Integer",
                        "checkBox",
                        68, 68
                        , List.of(
                        new Code("1", "flergenerasjonsproblematikk")),
                        "",
                        false),
                new FieldDefinition(1911, "TEMA_SAMBARN_A",
                        "Integer",
                        "checkBox",
                        69, 69
                        , List.of(
                        new Code("1", "samarb. om felles barn (foreldre bor ikke sammen)")),
                        "",
                        false),
                new FieldDefinition(1912, "TEMA_SÆRBARN_A",
                        "Integer",
                        "checkBox",
                        70, 70
                        , List.of(
                        new Code("1", "særkullsbarn og/eller ny familie")),
                        "",
                        false),
                new FieldDefinition(1913, "TEMA_KULTUR_A",
                        "Integer",
                        "checkBox",
                        71, 71
                        , List.of(
                        new Code("1", "kultur-/minoritetsspørsmål")),
                        "",
                        false),
                new FieldDefinition(1914, "TEMA_TVANG_A",
                        "Integer",
                        "checkBox",
                        72, 72
                        , List.of(
                        new Code("1", "tvangsekteskap")),
                        "",
                        false),
                new FieldDefinition(1915, "TEMA_RUS_A",
                        "Integer",
                        "checkBox",
                        73, 73
                        , List.of(
                        new Code("1", "bruk av rusmidler")),
                        "",
                        false),
                new FieldDefinition(1916, "TEMA_SYKD_A",
                        "Integer",
                        "checkBox",
                        74, 74
                        , List.of(
                        new Code("1", "sykdom / nedsatt funksjonsevne")),
                        "",
                        false),
                new FieldDefinition(1917, "TEMA_VOLD_A",
                        "Integer",
                        "checkBox",
                        75, 75
                        , List.of(
                        new Code("1", "fysisk / psykisk vold / seksuelt misbruk")),
                        "",
                        false),
                new FieldDefinition(1918, "TEMA_ALVH_A",
                        "Integer",
                        "checkBox",
                        76, 76
                        , List.of(
                        new Code("1", "annen alvorlig hendelse")),
                        "",
                        false),
                new FieldDefinition(20, "HOVEDF_BEHAND_A",
                        "String",
                        "dropDownList",
                        77, 77
                        , List.of(
                        new Code("1", "Parsamtale")
                        , new Code("2", "Foreldresamtale")
                        , new Code("3", "Familiesamtale")
                        , new Code("4", "Individualsamtale")),
                        "",
                        false),
                new FieldDefinition(21, "BEKYMR_MELD_A",
                        "String",
                        "dropDownList",
                        78, 78
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(221, "DELT_PARTNER_A",
                        "String",
                        "dropDownList",
                        79, 79
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(222, "DELT_EKSPART_A",
                        "String",
                        "dropDownList",
                        80, 80
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(223, "DELT_BARNU18_A",
                        "String",
                        "dropDownList",
                        81, 81
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(224, "DELT_BARNO18_A",
                        "String",
                        "dropDownList",
                        82, 82
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(225, "DELT_FORELDRE_A",
                        "String",
                        "dropDownList",
                        83, 83
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(226, "DELT_OVRFAM_A",
                        "String",
                        "dropDownList",
                        84, 84
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(227, "DELT_VENN_A",
                        "String",
                        "dropDownList",
                        85, 85
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(228, "DELT_ANDR_A",
                        "String",
                        "dropDownList",
                        86, 86
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(231, "SAMT_PRIMK_A",
                        "Integer",
                        "textBox",
                        87, 88
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(232, "SAMT_PARTNER_A",
                        "Integer",
                        "textBox",
                        89, 90
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(233, "SAMT_EKSPART_A",
                        "Integer",
                        "textBox",
                        91, 92
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(234, "SAMT_BARNU18_A",
                        "Integer",
                        "textBox",
                        93, 94
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(235, "SAMT_BARNO18_A",
                        "Integer",
                        "textBox",
                        95, 96
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(236, "SAMT_FORELDRE_A",
                        "Integer",
                        "textBox",
                        97, 98
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(237, "SAMT_OVRFAM_A",
                        "Integer",
                        "textBox",
                        99, 100
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(238, "SAMT_VENN_A",
                        "Integer",
                        "textBox",
                        101, 102
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(239, "SAMT_ANDRE_A",
                        "Integer",
                        "textBox",
                        103, 104
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(241, "ANTSAMT_HOVEDT_A",
                        "Integer",
                        "textBox",
                        105, 107
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(242, "ANTSAMT_ANDREANS_A",
                        "Integer",
                        "textBox",
                        108, 110
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(251, "ANTSAMT_IARET_A",
                        "Integer",
                        "textBox",
                        111, 113
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(252, "ANTSAMT_OPPR_A",
                        "Integer",
                        "textBox",
                        114, 116
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(261, "TLFSAMT_IARET_A",
                        "Integer",
                        "textBox",
                        117, 119
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(262, "TLFSAMT_OPPR_A",
                        "Integer",
                        "textBox",
                        120, 122
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(271, "TIMER_IARET_A",
                        "Integer",
                        "textBox",
                        123, 125
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(272, "TIMER_OPPR_A",
                        "Integer",
                        "textBox",
                        126, 128
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(28, "TOLK_A",
                        "String",
                        "dropDownList",
                        129, 129
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(291, "SAMARB_INGEN_A",
                        "Integer",
                        "checkBox",
                        130, 130
                        , List.of(
                        new Code("1", "Ikke samarbeid med med annen instans")),
                        "",
                        false),
                new FieldDefinition(292, "SAMARB_LEGE_A",
                        "Integer",
                        "checkBox",
                        131, 131
                        , List.of(
                        new Code("1", "Fastlege")),
                        "",
                        false),
                new FieldDefinition(293, "SAMARB_HELSE_A",
                        "Integer",
                        "checkBox",
                        132, 132
                        , List.of(
                        new Code("1", "Helsestasjon / Familiesenter")),
                        "",
                        false),
                new FieldDefinition(294, "SAMARB_PSYKH_A",
                        "Integer",
                        "checkBox",
                        133, 133
                        , List.of(
                        new Code("1", "Psykisk helsevern")),
                        "",
                        false),
                new FieldDefinition(295, "SAMARB_JURIST_A",
                        "Integer",
                        "checkBox",
                        134, 134
                        , List.of(
                        new Code("1", "Jurist")),
                        "",
                        false),
                new FieldDefinition(296, "SAMARB_KRISES_A",
                        "Integer",
                        "checkBox",
                        135, 135
                        , List.of(
                        new Code("1", "Krisesenter")),
                        "",
                        false),
                new FieldDefinition(297, "SAMARB_SKOLE_A",
                        "Integer",
                        "checkBox",
                        136, 136
                        , List.of(
                        new Code("1", "Skole/PP-tjeneste")),
                        "",
                        false),
                new FieldDefinition(298, "SAMARB_SOS_A",
                        "Integer",
                        "checkBox",
                        137, 137
                        , List.of(
                        new Code("1", "NAV")),
                        "",
                        false),
                new FieldDefinition(299, "SAMARB_KOMMB_A",
                        "Integer",
                        "checkBox",
                        138, 138
                        , List.of(
                        new Code("1", "Kommunalt barnevern")),
                        "",
                        false),
                new FieldDefinition(2910, "SAMARB_STATB_A",
                        "Integer",
                        "checkBox",
                        139, 139
                        , List.of(
                        new Code("1", "Statlig barnevern")),
                        "",
                        false),
                new FieldDefinition(2911, "SAMARB_ANDRE_A",
                        "Integer",
                        "checkBox",
                        140, 140
                        , List.of(
                        new Code("1", "Annet")),
                        "",
                        false),
                new FieldDefinition(30, "STATUS_ARETSSL_A",
                        "String",
                        "dropDownList",
                        141, 141
                        , List.of(
                        new Code("1", "Avsluttet etter avtale med klient")
                        , new Code("2", "Klient uteblitt")
                        , new Code("3", "Saken ikke avsluttet i inneværende år")),
                        "",
                        false),
                new FieldDefinition(31, "HOVEDTEMA_A",
                        "String",
                        "dropDownList",
                        142, 143
                        , List.of(
                        new Code("01", "styrke parforholdet")
                        , new Code("02", "avklare/avslutte parforholdet")
                        , new Code("03", "samlivsbrudd i familien")
                        , new Code("04", "samspillvansker")
                        , new Code("05", "barnets opplevelse av sin livssituasjon")
                        , new Code("06", "barnets situasjon i foreldrenes konflikt")
                        , new Code("07", "bostedsavklaring/ samvær")
                        , new Code("08", "foreldrerollen")
                        , new Code("09", "foreldre-barn-relasjonen")
                        , new Code("10", "flergenerasjons- problematikk")
                        , new Code("11", "samarb. om felles barn (foreldre bor ikke sammen)")
                        , new Code("12", "særkullsbarn og/eller ny familie")
                        , new Code("13", "kultur-/minoritetsspørsmål")
                        , new Code("14", "tvangsekteskap")
                        , new Code("15", "bruk av rusmidler")
                        , new Code("16", "sykdom / nedsatt funksjonsevne")
                        , new Code("17", "fysisk / psykisk vold / seksuelt misbruk")
                        , new Code("18", "annen alvorlig hendelse")),
                        "",
                        false),
                new FieldDefinition(32, "DATO_AVSL_A",
                        "Date",
                        "textBox",
                        144, 151
                        , List.of(),
                        "ddMMyyyy",
                        false)
        );
    }

    public static Integer getFieldLength() {
        return getFieldDefinitions().get(getFieldDefinitions().size() - 1).getTo();
    }
}
