package no.ssb.kostra.validation.rule.famvern.famvern52b

enum class Familievern52bRuleId(val title: String) {
    FAMILIEVERN52B_RULE003("Kontroll 003 : Regionsnummer"),
    FAMILIEVERN52B_RULE004("Kontroll 004 : Kontornummer"),
    FAMILIEVERN52B_RULE005("Kontroll 005 : Manglende samsvar mellom regions- og kontornummer"),
    FAMILIEVERN52B_RULE006("Kontroll 006 : Dubletter"),
    FAMILIEVERN52B_RULE007("Kontroll 007 : Gruppenavn"),
    FAMILIEVERN52B_RULE008("Kontroll 008 : Start dato"),
    FAMILIEVERN52B_RULE009("Kontroll 009 : Målgruppe"),
    FAMILIEVERN52B_RULE010("Kontroll 010 : Gruppens hovedtema"),
    FAMILIEVERN52B_RULE011("Kontroll 011 : Antall gruppemøter gjennomført totalt i løpet av året"),
    FAMILIEVERN52B_RULE012("Kontroll 012 : Antall gruppemøter gjennomført siden opprettelsen"),
    FAMILIEVERN52B_RULE013("Kontroll 013 : Antall timer anvendt i gruppen totalt i løpet av året"),
    FAMILIEVERN52B_RULE014("Kontroll 014 : Antall timer anvendt i gruppen totalt siden opprettelsen"),
    FAMILIEVERN52B_RULE015("Kontroll 015 : Antall deltagere i gruppen i løpet av året"),
    FAMILIEVERN52B_RULE016("Kontroll 016 : Antall deltagere i gruppen siden opprettelsen"),
    FAMILIEVERN52B_RULE017("Kontroll 017 : Antall terapeuter involvert i gruppebehandlingen"),
    FAMILIEVERN52B_RULE018("Kontroll 018 : Er det benyttet tolk i minst én gruppesamtale?"),
    FAMILIEVERN52B_RULE019("Kontroll 019 : Status ved året slutt"),
    FAMILIEVERN52B_RULE020("Kontroll 020 : Gruppebehandlingen er avsluttet, men avslutningsdato mangler"),
    FAMILIEVERN52B_RULE021("Kontroll 021 : Avslutningsdato før første samtale")
}