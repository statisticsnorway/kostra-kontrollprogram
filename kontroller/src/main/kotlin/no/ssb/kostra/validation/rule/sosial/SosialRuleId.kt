package no.ssb.kostra.validation.rule.sosial

enum class SosialRuleId(val title: String) {
    KOMMUNENUMMER_03("Kontroll 03 : Kommunenummer"),
    BYDELSNUMMER_03("Kontroll 03 : Bydelsnummer"),
    OPPGAVE_AAR_04("Kontroll 04 : Oppgaveår"),
    FODSELSNUMMER_05("Kontroll 05 : Fødselsnummer"),

    FODSELSNUMMER_DUBLETTER_05A("Kontroll 05A : Fødselsnummer, dubletter"),
    JOURNALNUMMER_DUBLETTER_05B("Kontroll 05B : Journalnummer, dubletter"),

    ALDER_UNDER_18_AAR_06("Kontroll 06 : Alder under 18 år"),
    ALDER_ER_96_AAR_ELLER_OVER_07("Kontroll 07 : Alder over 67 år eller over"),
    KJONN_08("Kontroll 08 : Kjønn"),
    SIVILSTAND_09("Kontroll 09 : Sivilstand"),

    BU_18_10("Kontroll 10 : Forsørgerplikt for barn under 18 år i husholdningen. Gyldige verdier"),
    BU_18_ANT_BU_18_11("Kontroll 11 : Det bor barn under 18 år i husholdningen. Mangler antall barn."),
    ANT_BU_18_BU_18_12("Kontroll 12 : Det bor barn under 18 år i husholdningen."),
    ANT_BU_18_13("Kontroll 13 : Mange barn under 18 år i husholdningen.")
}