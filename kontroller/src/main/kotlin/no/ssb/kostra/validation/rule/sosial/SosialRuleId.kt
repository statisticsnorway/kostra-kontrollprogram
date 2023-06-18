package no.ssb.kostra.validation.rule.sosial

enum class SosialRuleId(val title: String) {
    KOMMUNENUMMER_03("03Kommunenummer"),
    BYDELSNUMMER_03("03Bydelsnummer"),
    OPPGAVE_AAR_04("04OppgaveAar"),
    FODSELSNUMMER_05("05Fodselsnummer"),

    FODSELSNUMMER_DUBLETTER_05A("Kontroll 05A Fødselsnummer, dubletter"),
    JOURNALNUMMER_DUBLETTER_05B("Kontroll 05B Journalnummer, dubletter"),

    ALDER_UNDER_18_AAR_06("06AlderUnder18Aar"),
    ALDER_ER_96_AAR_ELLER_OVER_07("07AlderEr96AarEllerOver"),
    KJONN_08("08Kjonn"),
    SIVILSTAND_09("09Sivilstand");
}