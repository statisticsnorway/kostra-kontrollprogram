package no.ssb.kostra.validation.rule.sosial.kvalifisering

enum class KvalifiseringRuleId(val title: String) {
    KVALIFISERING_07_ALDER_ER_OVER_68_AAR("Kontroll 07 : Alder er 68 år eller over"),
    REG_DATO_14("Kontroll 14 : Dato for registrert søknad ved NAV-kontoret"),
    VEDTAK_DATO_15("Kontroll 15 : Dato for fattet vedtak om program (søknad innvilget)"),
    BEGYNT_DATO_16("Kontroll 16 : Dato for når deltakeren begynte i program (iverksettelse)"),
    KVALIFISERINGSPROGRAM_I_ANNEN_KOMMUNE_19("Kontroll 19 : Kvalifiseringsprogram i annen kommune"),
    KVALIFISERINGSPROGRAM_I_ANNEN_KOMMUNE_KOMMUNENUMMER_20("Kontroll 20 : Kvalifiseringsprogram i annen kommune. Kommunenummer"),
    YTELSER_21("Kontroll 21 : Ytelser de to siste månedene før registrert søknad ved NAV-kontoret"),
    MOTTATT_STOTTE_26("Kontroll 26 : Mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av ÅÅÅÅ"),
    MOTTATT_OKONOMISK_SOSIALHJELP_27("Kontroll 27 : Mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av ÅÅÅÅ. Svaralternativer"),
    MAANEDER_MED_KVALIFISERINGSSTONAD_28("Kontroll 28 : Måneder med kvalifiseringsstønad. Gyldige koder"),
    KVALIFISERINGSSUM_MANGLER_29("Kontroll 29 : Kvalifiseringssum mangler eller har ugyldige tegn"),
    HAR_VARIGHET_MEN_MANGLER_KVALIFISERINGSSUM_30("Kontroll 30 : Har varighet, men mangler kvalifiseringssum"),
    HAR_KVALIFISERINGSSUM_MEN_MANGLER_VARIGHET_31("Kontroll 31 : Har kvalifiseringssum, men mangler varighet"),
    KVALIFISERINGSSUM_OVER_MAKSIMUM_32("Kontroll 32 : Kvalifiseringssum på kr 600000,- eller mer"),
    STATUS_FOR_DELTAKELSE_I_KVALIFISERINGSPROGRAM_36("Kontroll 36 : Status for deltakelse i kvalifiseringsprogram per 31.12.ÅÅÅÅ. Gyldige verdier"),
    DATO_FOR_AVSLUTTET_PROGRAM_37("Kontroll 37 : Dato for avsluttet program (gjelder fullførte, avsluttede etter avtale og varig avbrutte program, ikke for permisjoner) (DDMMÅÅ)"),
    FULLFORTE_AVSLUTTEDE_PROGRAM_SITUASJON_38("Kontroll 38 : Fullførte/avsluttede program – til hvilken situasjon gikk deltakeren? Gyldige verdier"),
    FULLFORTE_AVSLUTTEDE_PROGRAM_INNTEKTKILDE_39("Kontroll 39 : Fullførte/avsluttede program – til hvilken inntektskilde gikk deltakeren? Gyldige verdier");
}