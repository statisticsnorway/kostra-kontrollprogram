package no.ssb.kostra.validation.rule.sosial.sosialhjelp

enum class SosialhjelpRuleId(val title: String) {
    SOSIALHJELP_K014_VKLO_GYLDIGE_VERDIER("Kontroll 14: Viktigste kilde til livsopphold. Gyldige verdier"),
    SOSIALHJELP_K015_VKLO_ARBEIDSINNTEKT("Kontroll 15: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Arbeidsinntekt"),
    SOSIALHJELP_K016_VKLO_KURS("Kontroll 16: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Kursstønad/lønn i arbeidsmarkedstiltak."),
    SOSIALHJELP_K017_VKLO_STIPEND("Kontroll 17: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Stipend/lån."),
    SOSIALHJELP_K018_VKLO_INTRODUKSJONSTONAD("Kontroll 18: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Introduksjonsstøtte."),
    SOSIALHJELP_K019_KVALIFISERINGSTONAD("Kontroll 19: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Kvalifiseringsstønad"),
    SOSIALHJELP_K020A_TRYGD("Kontroll 20_A: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Trygd/pensjon"),
    SOSIALHJELP_K020B_TRYGD("Kontroll 20_B: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Trygd/pensjon"),
    SOSIALHJELP_K021_SOSIALHJELP("Kontroll 21: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Sosialhjelp."),
    SOSIALHJELP_K022_TRYGDESYSTEMET_ALDER("Kontroll 22: Tilknytning til trygdesystemet og alder. 62 år eller yngre med alderspensjon."),
    SOSIALHJELP_K023_TRYGDESYSTEMET_BARN("Kontroll 23: Tilknytning til trygdesystemet og barn. Overgangsstønad."),
    SOSIALHJELP_K024_TRYGDESYSTEMET_ARBEIDSSITUASJON("Kontroll 24: Tilknytning til trygdesystemet og arbeidssituasjon. Uføretrygd/alderspensjon og ikke arbeidssøker."),
    SOSIALHJELP_K024B_TRYGDESYSTEMET_ARBEIDSSITUASJON("Kontroll 24B: Tilknytning til trygdesystemet og arbeidssituasjon. Arbeidsavklaringspenger."),
    SOSIALHJELP_K025_ARBEIDSSITUASJON("Kontroll 25: Arbeidssituasjon. Gyldige koder."),
    SOSIALHJELP_K026_STONADSMAANEDER("Kontroll 26: Stønadsmåneder. Gyldige koder."),
    SOSIALHJELP_K027_STONADSSUM("Kontroll 27: Stønadssum mangler eller har ugyldige tegn."),
    SOSIALHJELP_K030_STONADSSUMMAX("Kontroll 30: Stønadssum på kr 600 000,- eller mer."),
    SOSIALHJELP_K031_STONADSSUMMIN("Kontroll 31: Stønadssum på kr 200,- eller lavere."),
    SOSIALHJELP_K032_OKONIMISKRAADGIVNING("Kontroll 32: Økonomiskrådgivning. Gyldige koder."),
    SOSIALHJELP_K033_INDIVIDUELLPLAN("Kontroll 33: Utarbeidelse av individuell plan. Gyldige koder."),
    SOSIALHJELP_K035_BOLIGSITUASJON("Kontroll 35: Boligsituasjon. Gyldige koder."),
    SOSIALHJELP_K036_BIDRAGSUM("Kontroll 36: Bidrag fordelt på måneder"),
    SOSIALHJELP_K037_LAANSUM("Kontroll 37: Lån fordelt på måneder"),
    SOSIALHJELP_K038_DUFNUMMER("Kontroll 38: DUF-nummer"),
    SOSIALHJELP_K039_VILKAR("Kontroll 39: Første vilkår i året, vilkår"),
    SOSIALHJELP_K040_VILKARSAMEKT("Kontroll 40: Første vilkår i året, vilkår til søkerens samboer/ektefelle"),
    SOSIALHJELP_K041_UTBETALINGSVEDTAK("Kontroll 41: Dato for utbetalingsvedtak"),
    SOSIALHJELP_K042_UTBETALINGSVEDTAK("Kontroll 42: Til og med Dato for utbetalingsvedtak"),
    SOSIALHJELP_K043_TYPEVILKAR("Kontroll 43: Type vilkår det stilles til mottakeren")
}