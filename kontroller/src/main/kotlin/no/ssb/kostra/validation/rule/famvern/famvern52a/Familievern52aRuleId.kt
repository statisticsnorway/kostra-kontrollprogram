package no.ssb.kostra.validation.rule.famvern.famvern52a

enum class Familievern52aRuleId(
    val title: String,
) {
    FAMILIEVERN52A_RULE003("Kontroll 003 : Regionsnummer"),
    FAMILIEVERN52A_RULE004("Kontroll 004 : Kontornummer"),
    FAMILIEVERN52A_RULE005("Kontroll 005 : Manglende samsvar mellom regions- og kontornummer"),
    FAMILIEVERN52A_RULE006("Kontroll 006 : Dubletter"),
    FAMILIEVERN52A_RULE007("Kontroll 007 : Henvendelsesdato"),
    FAMILIEVERN52A_RULE009("Kontroll 009 : Primærklient hatt kontakt med eller vært klient tidligere"),
    FAMILIEVERN52A_RULE011("Kontroll 011 : Primærklientens viktigste begrunnelse for å ta kontakt "),
    FAMILIEVERN52A_RULE013("Kontroll 013 : Primærklientens kjønn"),
    FAMILIEVERN52A_RULE014("Kontroll 014 : Primærklientens fødselsår"),
    FAMILIEVERN52A_RULE015("Kontroll 015 : Primærklientens samlivsstatus"),
    FAMILIEVERN52A_RULE016("Kontroll 016 : Utdypende om primærklientens formelle sivilstand"),
    FAMILIEVERN52A_RULE017("Kontroll 017 : Primærklientens bosituasjon ved opprettelsen"),
    FAMILIEVERN52A_RULE018("Kontroll 018 : Primærklientens tilknytning til utdanning og arbeidsliv"),
    FAMILIEVERN52A_RULE019A("Kontroll 019 A : Varighet på relasjon mellom primærklient og viktigste samtalepartner, partnere"),

    // CR Note: Only in use in tests
    FAMILIEVERN52A_RULE019B1("Kontroll 019 B1 : Tid siden brudd for primærklient og viktigste samtalepartner, ekspartnere, lengde"),
    FAMILIEVERN52A_RULE019B2(
        "Kontroll 019 B2 : Varighet på relasjon mellom primærklient og viktigste samtalepartner, ekspartnere, varighet",
    ),

    FAMILIEVERN52A_RULE020("Kontroll 020 : Dato for første behandlingssamtale"),
    FAMILIEVERN52A_RULE021("Kontroll 021 : Første behandlingssamtale er før henvendelsesdato"),
    FAMILIEVERN52A_RULE022("Kontroll 022 : Områder det har vært arbeidet med i saken"),
    FAMILIEVERN52A_RULE023("Kontroll 023 : Hovedform på behandlingstilbudet"),
    FAMILIEVERN52A_RULE024("Kontroll 024 : Deltagelse i behandlingssamtaler med primærklienten i løpet av året"),
    FAMILIEVERN52A_RULE025("Kontroll 025 : Behandlingssamtaler for de involverte i saken i løpet av året"),
    FAMILIEVERN52A_RULE026("Kontroll 026 : Relasjon mellom antall behandlingssamtaler og hvem som har deltatt"),
    FAMILIEVERN52A_RULE027("Kontroll 027 : Antall behandlingssamtaler for ansatte ved kontoret i løpet av året"),
    FAMILIEVERN52A_RULE028("Kontroll 028 : Antall behandlingssamtaler i løpet av året"),
    FAMILIEVERN52A_RULE029("Kontroll 029 : Antall behandlingssamtaler siden opprettelsen"),
    FAMILIEVERN52A_RULE030("Kontroll 030 : Totalt antall timer i løpet av året"),
    FAMILIEVERN52A_RULE031("Kontroll 031 : Totalt antall timer siden saken ble opprettet"),
    FAMILIEVERN52A_RULE032("Kontroll 032 : Samarbeid med andre instanser siden opprettelsen"),
    FAMILIEVERN52A_RULE033("Kontroll 033 : Status ved året slutt"),
    FAMILIEVERN52A_RULE034("Kontroll 034 : Sakens hovedtema (Fylles ut når saken avsluttes)"),
    FAMILIEVERN52A_RULE035("Kontroll 035 : Saken avsluttet, men ikke satt avslutningsdato"),
    FAMILIEVERN52A_RULE036("Kontroll 036 : Avslutningsdato før første samtale"),
    FAMILIEVERN52A_RULE037("Kontroll 037 : Bekymringsmelding sendt barnevernet"),
    FAMILIEVERN52A_RULE038("Kontroll 038 : Ventetid"),
}
