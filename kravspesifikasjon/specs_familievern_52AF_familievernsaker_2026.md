# Kravspesifikasjon Familievernssaker, skjema 52AF, 2026

## Begrep

### <a name="alvorligehetsgrader">Alvorlighetsgrader</a>
* :skull: **FATAL**, når en kontroll ender med alvorlighetsgraden FATAL så stopper all videre kontrollering umiddelbart. FATAL hindrer da andre kontrollene i bli kjørt. Videre så hindrer denne innsending av filvedlegget til SSB.
* :no_entry: **ERROR**, når en kontroll ender med alvorlighetsgraden ERROR så hindrer denne innsending av filvedlegget til SSB.
* :warning: **WARNING**, når en kontroll ender med alvorlighetsgraden WARNING så kan filvedlegget bli sendt inn til SSB med advarsler.
* :information_source: **INFO**, når en kontroll ender med alvorlighetsgraden INFO så kan filvedlegget bli sendt inn til SSB med tilbakemeldinger som er til informasjon for avgiveren.
* :white_check_mark:**OK**, når en kontroll ender med alvorlighetsgraden vises ingen tilbakemelding av noe slag da alt er i orden.

### <a name="variabler">Variabler fra skjema</a>

I forbindelse med kontrollering så blir noen variabler sendt med fra portalen og blir benyttet i noen av kontrollene.
Disse er:

* <a name="skjemanummer">**skjemanummer**</a>, for eksempel **0A**, **11F** og **55F**
* <a name="rapporteringsar">**rapporteringsår**</a>, for eksempel **2024**
* <a name="kommunenummer">**kommunenummer**</a>, for eksempel **0301**
* <a name="kommunenavn">**kommunenavn**</a>, for eksempel **Oslo**
* <a name="organisasjonsnummer">**organisasjonsnummer**</a>, for eksempel **958935420**
* <a name="foretaksnummer">**foretakssjonsnummer**</a>, for eksempel **958935420**
* <a name="skal_rapportere">**skal rapportere**</a>, for eksempel **Ja / Nei**

## Filbeskrivelse

Filbeskrivelse finnes som [markdown](file_description_52AF_2026.md) og [yaml](file_description_52AF_2026.yaml)
. 
Endringslogg for filbeskrivelse finnes som [markdown](file_description_52AF_2026_changelog.md).

## Krav

### Kontroll 001: Recordlengde
Se krav i [kravspesifikasjon/felles_posisjonsbaserte_filvedlegg.md](kravspesifikasjon/felles_posisjonsbaserte_filvedlegg.md#kontroll-001--recordlengde)


### Kontroll 002: Filbeskrivelse
Se krav i [kravspesifikasjon/felles_posisjonsbaserte_filvedlegg.md](kravspesifikasjon/felles_posisjonsbaserte_filvedlegg.md#kontroll-002--filbeskrivelse-mangler-obligatorisk-verdi)


### Kontroll 003: Regionsnummer
**Gitt** gyldige regioner i [koblingshierarkiet for familievern](mapping_familievern_region_fylke_kontor_2026.md)<br/> 
**når** feltet **REGION_NR_A** inneholder ingen av de gyldige regionene i listen,<br/> 
**så** gi :warning: **ADVARSEL** med meldingen "Regionsnummeret som er oppgitt i recorden fins ikke i listen med gyldige regionsnumre. Fant '{REGION_NR_A}', forventet én av : {GYLDIGE_REGIONER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule003Regionsnummer.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule003RegionsnummerTest.kt)


### Kontroll 004: Kontornummer
**Gitt** gyldige kontornumre i [koblingshierarkiet for familievern](mapping_familievern_region_fylke_kontor_2026.md)<br/>
**når** feltet **KONTOR_NR_A** inneholder ingen av de gyldige kontornumrene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Kontornummeret som er oppgitt i recorden fins ikke i listen med gyldige kontornumre. Fant '{KONTOR_NR_A}', forventet én av : {GYLDIGE_KONTORER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule004Kontornummer.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule004KontornummerTest.kt)


### Kontroll 005: Manglende samsvar mellom regions- og kontornummer
**Gitt** koblingene mellom regioner og kontornumre i [koblingshierarkiet for familievern](mapping_familievern_region_fylke_kontor_2026.md)<br/>
**når** feltet **REGION_NR_A** og **KONTOR_NR_A** ikke samsvarer med koblingene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Regionsnummer '{REGION_NR_A}' og kontornummer '{KONTOR_NR_A}' stemmer ikke overens."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule005RegionsnummerKontornummer.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule005RegionsnummerKontornummerTest.kt)


### Kontroll 006: Dubletter
**Gitt** feltene **KONTOR_NR_A** og **JOURNAL_NR_A**<br/>
**når** feltet **JOURNAL_NR_A** forekommer mer enn én gang per **KONTOR_NR_A**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Journalnummeret er benyttet på mer enn en sak ({ANTALL_SAKER} stk). Dubletter på kontornummer '{KONTOR_NR_A}' - journalnummer '{KONTOR_NR_A}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule006Dubletter.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule006DubletterTest.kt)


### Kontroll 007: Henvendelsesdato
**Gitt** feltet **HENV_DATO_A**<br/>
**når** feltet **HENV_DATO_A** ikke er en gyldig dato i formatet **ddMMyyyy**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke oppgitt dato ({HENV_DATO_A}) for når primærklienten henvendte seg til familievernkontoret eller feltet har ugyldig format (DDMMÅÅÅÅ). Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule007Henvendelsesdato.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule007HenvendelsesdatoTest.kt)


### Kontroll 009: Primærklient hatt kontakt med eller vært klient tidligere
**Gitt** gyldige kode fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **KONTAKT_TIDL_A** inneholder ingen av de gyldige kode i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut om primærklienten har vært i kontakt med/klient ved familievernet tidligere, eller feil kode er benyttet. Fant '{KONTAKT_TIDL_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule009KontaktTidligere.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule009KontaktTidligereTest.kt)


### Kotroll 011: Primærklientens viktigste begrunnelse for å ta kontakt
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **HENV_GRUNN_A** inneholder ingen av de gyldige kode i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke oppgitt hva som er primærklientens viktigste ønsker med kontakten, eller feltet har ugyldig format. Fant '{HENV_GRUNN_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule011HenvendelsesBegrunnelse.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule011HenvendelsesBegrunnelseTest.kt)


### Kontroll 013: Primærklientens kjønn
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_KJONN_A** inneholder ingen av de gyldige kode i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Primærklientens kjønn er ikke fylt ut eller feil kode er benyttet. Fant '{PRIMK_KJONN_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut." 

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule013Kjonn.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule013KjonnTest.kt)


### Kontroll 014: Primærklientens fødselsår
**Gitt** årgangen til filuttrekket **ÅRGANG** og feltet **PRIMK_FODT_A** er utfylt og inneholder et 4-sifret årstall<br/>
**når** feltet **PRIMK_FODT_A** er utenfor intervallet {**ÅRGANG** - 100 .. **ÅRGANG**},<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Dette er ikke oppgitt fødselsår på primærklienten eller feltet har ugyldig format, årgang gir negativ alder, eller alder større enn 100 år. Fant '{PRIMK_FODT_A}'. Feltet er obligatorisk å fylle ut. Fyll inn fødselsår eller sjekk at fødselsårsfeltet har korrekt format og logisk verdi i forhold til alder."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule014Fodselsaar.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule014FodselsaarTest.kt)


### Kontroll 015: Primærklientens samlivsstatus
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_SIVILS_A** inneholder ingen av de gyldige kode i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Primærklientens samlivsstatus ved sakens opprettelse er ikke fylt ut eller feil kode er benyttet. Fant '{PRIMK_SIVILS_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule015Samlivsstatus.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule015SamlivsstatusTest.kt)


### Kontroll 016: Utdypende om primærklientens formelle sivilstand
**Gitt** gyldige koder for feltet **FORMELL_SIVILS_A** fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_SIVILS_A** er utfylt med kode 3 eller 4, og feltet **FORMELL_SIVILS_A** inneholder ingen av de gyldige kode i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er oppgitt at primærklientens samlivsstatus er Samboer eller at primærklient ikke lever i samliv, men det er ikke fylt ut hva som er primærklientens korrekt sivilstand. Fant '{FORMELL_SIVILS_A}', forventet én av: {GYLDIGE KODER}."   

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule016FormellSivilstand.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule016FormellSivilstandTest.kt)


### Kontroll 017: Primærklientens bosituasjon ved opprettelsen
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_SAMBO_A** inneholder ingen av de gyldige kode i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut om primærklienten bor sammen med andre ved sakens opprettelse eller feil kode er benyttet. Fant '{PRIMK_SAMBO_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut.

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule017Bosituasjon.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule017BosituasjonTest.kt)


### Kontroll 018: Primærklientens tilknytning til utdanning og arbeidsliv
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_UTDANN_A** inneholder ingen av de gyldige kode i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke krysset av for primærklientens tilknytning til arbeidslivet ved sakens opprettelse eller feil kode er benyttet. Fant '{PRIMK_UTDANN_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule018Arbeidssituasjon.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule018ArbeidssituasjonTest.kt)




