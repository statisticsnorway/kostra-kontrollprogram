package no.ssb.kostra.validation.rule.sosial.kvalifisering

enum class KvalifiseringRuleId(val title: String) {
    REG_DATO_14("14RegDato"),
    VEDTAK_DATO_15("15VedtakDato"),
    BEGYNT_DATO_16("16BegyntDato"),
    KVALIFISERINGSPROGRAM_I_ANNEN_KOMMUNE_19("19KvalifiseringsprogramIAnnenKommune"),
    KVALIFISERINGSPROGRAM_I_ANNEN_KOMMUNE_KOMMUNENUMMER_20("20KvalifiseringsprogramIAnnenKommuneKommunenummer"),
    YTELSER_21("21Ytelser"),
    MOTTATT_STOTTE_26("26MottattStotte"),
    MOTTATT_OKONOMISK_SOSIALHJELP_27("27MottattOkonomiskSosialhjelp"),
    MAANEDER_MED_KVALIFISERINGSSTONAD_28("28MaanederMedKvalifiseringsstonad"),
    KVALIFISERINGSSUM_MANGLER_29("29KvalifiseringssumMangler"),
    HAR_VARIGHET_MEN_MANGLER_KVALIFISERINGSSUM_30("30HarVarighetMenManglerKvalifiseringssum"),
    HAR_KVALIFISERINGSSUM_MEN_MANGLER_VARIGHET_31("31HarKvalifiseringssumMenManglerVarighet"),
    KVALIFISERINGSSUM_OVER_MAKSIMUM_32("32KvalifiseringssumOverMaksimum"),
    STATUS_FOR_DELTAKELSE_I_KVALIFISERINGSPROGRAM_36("36StatusForDeltakelseIKvalifiseringsprogram"),
    DATO_FOR_AVSLUTTET_PROGRAM_37("37DatoForAvsluttetProgram"),
    FULLFORTE_AVSLUTTEDE_PROGRAM_SITUASJON_38("38FullforteAvsluttedeProgramSituasjon"),
    FULLFORTE_AVSLUTTEDE_PROGRAM_INNTEKTKILDE_39("39FullforteAvsluttedeProgramInntektkilde");
}