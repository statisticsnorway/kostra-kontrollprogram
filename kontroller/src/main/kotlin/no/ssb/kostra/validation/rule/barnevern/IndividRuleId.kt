package no.ssb.kostra.validation.rule.barnevern

enum class IndividRuleId(val title: String) {
    INDIVID_00("Individ Kontroll 00: Individ mangler"),
    INDIVID_01("Individ Kontroll 01: Validering av individ"),
    INDIVID_01A("Individ Kontroll 01a: Validering av datoer"),
    INDIVID_02A("Individ Kontroll 02a: Startdato etter sluttdato"),
    INDIVID_02B("Individ Kontroll 02b: Sluttdato mot versjon"),
    INDIVID_02D("Individ Kontroll 02d: Avslutta 31 12 medfører at sluttdato skal være satt"),
    INDIVID_03("Individ Kontroll 03: Fødselsnummer og DUF-nummer"),
    INDIVID_04("Individ Kontroll 04: Dublett på fødselsnummer"),
    INDIVID_05("Individ Kontroll 05: Dublett på journalnummer"),
    INDIVID_06("Individ Kontroll 06: Har meldinger, planer eller tiltak"),
    INDIVID_07("Individ Kontroll 07: Klient over 25 år avsluttes"),
    INDIVID_08("Individ Kontroll 08: Alder i forhold til tiltak"),
    INDIVID_09("Individ Kontroll 09: Bydelsnummer"),
    INDIVID_10("Individ Kontroll 10: Bydelsnavn"),
    INDIVID_11("Individ Kontroll 11: Fødselsnummer"),
    INDIVID_12("Individ Kontroll 12: Fødselsnummer"),
    INDIVID_19("Individ Kontroll 19: DUF-nummer"),

    MELDING_02A("Melding Kontroll 2a: Startdato etter sluttdato"),
    MELDING_02C("Melding Kontroll 2c: Sluttdato mot individets sluttdato"),
    MELDING_02D("Melding Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på"),
    MELDING_02E("Melding Kontroll 2e: Startdato mot individets startdato"),
    MELDING_03("Melding Kontroll 3: Behandlingstid av melding"),
    MELDING_04("Melding Kontroll 4: Kontroll av konkludert melding, melder"),
    MELDING_05("Melding Kontroll 5: Kontroll av konkludert melding, saksinnhold"),

    MELDER_02("Melder Kontroll 2: Kontroll av kode og presisering"),

    SAKSINNHOLD_02("Saksinnhold Kontroll 2: Kontroll av kode og presisering"),

    UNDERSOKELSE_02A("Undersøkelse Kontroll 2a: Startdato etter sluttdato"),
    UNDERSOKELSE_02B("Undersøkelse Kontroll 2b: Sluttdato mot rapporteringsår"),
    UNDERSOKELSE_02C("Undersøkelse Kontroll 2c: Sluttdato mot individets sluttdato"),
    UNDERSOKELSE_02D("Undersøkelse Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på undersøkelsen"),
    UNDERSOKELSE_02E("Undersøkelse Kontroll 2e: Startdato mot individets startdato"),
    UNDERSOKELSE_03("Undersøkelse Kontroll 3: Kontroll av kode og presisering"),
    UNDERSOKELSE_04("Undersøkelse Kontroll 4: Konklusjon av undersøkelse"),
    UNDERSOKELSE_07("Undersøkelse Kontroll 7: Konkludert undersøkelse skal ha vedtaksgrunnlag"),
    UNDERSOKELSE_08("Undersøkelse Kontroll 8: Ukonkludert undersøkelse påbegynt før 1. juli er ikke konkludert"),

    VEDTAK_02("Vedtaksgrunnlag Kontroll 2: Kontroll av kode og presisering"),

    PLAN_02A("Plan Kontroll 2a: Startdato etter sluttdato"),
    PLAN_02B("Plan Kontroll 2b: Sluttdato mot rapporteringsår"),
    PLAN_02C("Plan Kontroll 2c: Sluttdato mot individets sluttdato"),
    PLAN_02D("Plan Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på"),
    PLAN_02E("Plan Kontroll 2e: Startdato mot individets startdato"),

    TILTAK_02A("Tiltak Kontroll 2a: Startdato etter sluttdato"),
    TILTAK_02B("Tiltak Kontroll 2b: Sluttdato mot rapporteringsår"),
    TILTAK_02C("Tiltak Kontroll 2c: Sluttdato mot individets sluttdato"),
    TILTAK_02D("Tiltak Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på"),
    TILTAK_02E("Tiltak Kontroll 2e: Startdato mot individets startdato"),
    TILTAK_04("Tiltak Kontroll 4: Omsorgstiltak med sluttdato krever årsak til opphevelse"),
    TILTAK_05("Tiltak Kontroll 5: Barn over 7 år og i barnehage"),
    TILTAK_06("Tiltak Kontroll 6: Barn over 11 år og i SFO"),
    TILTAK_07("Tiltak Kontroll 7: Kontroll av manglende presisering for tiltakskategori"),
    TILTAK_08("Tiltak Kontroll 8: Kontroll av kode og presisering for opphevelse"),
    TILTAK_09("Tiltak Kontroll 9: Flere plasseringstiltak i samme periode"),

    LOVHJEMMEL_02("Lovhjemmel Kontroll 2: Omsorgstiltak med sluttdato krever årsak til opphevelse"),
    LOVHJEMMEL_03("Lovhjemmel Kontroll 3: Individet er over 18 år og har omsorgstiltak"),
    LOVHJEMMEL_04("Lovhjemmel Kontroll 4: Lovhjemmel");
}