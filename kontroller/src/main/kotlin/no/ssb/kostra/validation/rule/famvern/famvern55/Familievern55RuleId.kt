package no.ssb.kostra.validation.rule.famvern.famvern55

enum class Familievern55RuleId(
    val title: String,
) {
    FAMILIEVERN55_RULE003("Kontroll 003 : Fylkesnummer"),
    FAMILIEVERN55_RULE005("Kontroll 005 : Avsluttede meklinger etter tidsbruk, (sumfelt)"),
    FAMILIEVERN55_RULE006("Kontroll 006 : Avsluttede meklinger etter deltakere, (sumfelt)"),
    FAMILIEVERN55_RULE007("Kontroll 007 : Avsluttede meklinger etter tidsbruk, (sumfelt)"),
    FAMILIEVERN55_RULE008("Kontroll 008 : Avsluttede meklinger, ikke overholdt tidsfrist"),
    FAMILIEVERN55_RULE009("Kontroll 009 : Avsluttede meklinger etter varighet, (sumfelt)"),
    FAMILIEVERN55_RULE010("Kontroll 010 : Avsluttede meklinger hvor barn har deltatt, (sumfelt)"),
    FAMILIEVERN55_RULE011("Kontroll 011 : Resultat av avsluttede meklinger, (sumfelt)"),
    FAMILIEVERN55_RULE012("Kontroll 012 : Antall avsluttede meklinger, skriftlig avtale etter resultat, (sumfelt)"),
    FAMILIEVERN55_RULE013("Kontroll 013 : Avsluttede meklinger og bekymringsmeldinger, (sumfelt)"),
    FAMILIEVERN55_RULE014("Kontroll 014 : Kontroll av totalsummer for meklinger"),
    FAMILIEVERN55_RULE015("Kontroll 015 : Kontroll av totalsummer for skriftlige avtaler"),
    FAMILIEVERN55_RULE016("Kontroll 016 : Avsluttede meklinger uten oppmøte, (sumfelt)"),
}
