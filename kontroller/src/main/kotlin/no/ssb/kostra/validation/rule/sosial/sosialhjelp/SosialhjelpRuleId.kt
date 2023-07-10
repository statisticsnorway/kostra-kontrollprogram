package no.ssb.kostra.validation.rule.sosial.sosialhjelp

enum class SosialhjelpRuleId(val title: String) {
    SOSIAL_K014_VKLO_GYLDIGE_VERDIER("Kontroll 14: Viktigste kilde til livsopphold. Gyldige verdier"),
    SOSIAL_K015_VKLO_ARBEIDSINNTEKT("Kontroll 15: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Arbeidsinntekt"),
    SOSIAL_K016_VKLO_KURS("Kontroll 16: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Kursstønad/lønn i arbeidsmarkedstiltak."),
    SOSIAL_K017_VKLO_STIPEND("Kontroll 17: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Stipend/lån."),
    SOSIAL_K018_VKLO_INTRODUKSJONSTONAD("Kontroll 18: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Introduksjonsstøtte."),
    SOSIAL_K019_KVALIFISERINGSTONAD("Kontroll 19: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Kvalifiseringsstønad"),
    SOSIAL_K020_TRYGD("Kontroll 20: Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Trygd/pensjon"),
    SOSIAL_K021_SOSIALHJELP("Kontroll 21 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Sosialhjelp."),
    SOSIAL_K022_TRYGDESYSTEMET_ALDER("Kontroll 22 Tilknytning til trygdesystemet og alder. 62 år eller yngre med alderspensjon."),
    SOSIAL_K023_TRYGDESYSTEMET_BARN("Kontroll 23 Tilknytning til trygdesystemet og barn. Overgangsstønad.")
}