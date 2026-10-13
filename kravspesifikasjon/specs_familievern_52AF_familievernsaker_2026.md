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
**når** feltet **KONTAKT_TIDL_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut om primærklienten har vært i kontakt med/klient ved familievernet tidligere, eller feil kode er benyttet. Fant '{KONTAKT_TIDL_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule009KontaktTidligere.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule009KontaktTidligereTest.kt)


### Kotroll 011: Primærklientens viktigste begrunnelse for å ta kontakt
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **HENV_GRUNN_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke oppgitt hva som er primærklientens viktigste ønsker med kontakten, eller feltet har ugyldig format. Fant '{HENV_GRUNN_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule011HenvendelsesBegrunnelse.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule011HenvendelsesBegrunnelseTest.kt)


### Kontroll 013: Primærklientens kjønn
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_KJONN_A** inneholder ingen av de gyldige kodene i listen,<br/>
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
**når** feltet **PRIMK_SIVILS_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Primærklientens samlivsstatus ved sakens opprettelse er ikke fylt ut eller feil kode er benyttet. Fant '{PRIMK_SIVILS_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule015Samlivsstatus.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule015SamlivsstatusTest.kt)


### Kontroll 016: Utdypende om primærklientens formelle sivilstand
**Gitt** gyldige koder for feltet **FORMELL_SIVILS_A** fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_SIVILS_A** er utfylt med kode `3 = Samboer` eller `4 = Lever ikke i samliv`, og feltet **FORMELL_SIVILS_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er oppgitt at primærklientens samlivsstatus er Samboer eller at primærklient ikke lever i samliv, men det er ikke fylt ut hva som er primærklientens korrekt sivilstand. Fant '{FORMELL_SIVILS_A}', forventet én av: {GYLDIGE KODER}."   

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule016FormellSivilstand.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule016FormellSivilstandTest.kt)


### Kontroll 017: Primærklientens bosituasjon ved opprettelsen
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_SAMBO_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut om primærklienten bor sammen med andre ved sakens opprettelse eller feil kode er benyttet. Fant '{PRIMK_SAMBO_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut.

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule017Bosituasjon.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule017BosituasjonTest.kt)


### Kontroll 018: Primærklientens tilknytning til utdanning og arbeidsliv
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_UTDANN_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke krysset av for primærklientens tilknytning til arbeidslivet ved sakens opprettelse eller feil kode er benyttet. Fant '{PRIMK_UTDANN_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule018Arbeidssituasjon.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule018ArbeidssituasjonTest.kt)


### Kontroll 019 A : Varighet på relasjon mellom primærklient og viktigste samtalepartner, partnere
**Gitt** gyldige koder for feltet **PART_LENGDE_A** fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_VSRELASJ_A** er utfylt med kode `1 = Partner`, og feltet **PART_LENGDE_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er oppgitt at primærklientens relasjon til viktigste deltager er partner, men det er ikke oppgitt hvor lenge partene har vært gift, samboere eller registrerte partnere. Fant '{PART_LENGDE_A}', forventet én av: {GYLDIGE KODER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule019AVarighetSamtalepartner.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule019AVarighetSamtalepartnerTest.kt)


### Kontroll 019 B1 : Tid siden brudd for primærklient og viktigste samtalepartner, ekspartnere, lengde
**Gitt** gyldige koder for feltet **EKSPART_LENGDE_A** fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_VSRELASJ_A** er utfylt med kode `2 = Ekspartner`, og feltet **EKSPART_LENGDE_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er oppgitt at primærklientens relasjon til viktigste deltager er ekspartner, men det er ikke oppgitt tid siden brudd. Fant '{EKSPART_LENGDE_A}', forventet én av: {GYLDIGE KODER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule019B1VarighetSidenBrudd.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule019B1VarighetSidenBruddTest.kt)


### Kontroll 019 B2 : Varighet på relasjon mellom primærklient og viktigste samtalepartner, ekspartnere, varighet
**Gitt** gyldige koder for feltet **EKSPART_VARIGH_A** fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **PRIMK_VSRELASJ_A** er utfylt med kode `2 = Ekspartner`, og feltet **EKSPART_VARIGH_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er oppgitt at primærklientens relasjon til viktigste deltager er ekspartner, men det er ikke oppgitt hvor lenge partene var gift, samboere eller registrerte partnere.  Fant '{EKSPART_VARIGH_A}', forventet én av: {GYLDIGE KODER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule019B2VarighetEkspartner.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule019B2VarighetEkspartnerTest.kt)


### Kontroll 020 : Dato for første behandlingssamtale
**Gitt** feltet **FORSTE_SAMT_A**<br/>
**når** feltet **FORSTE_SAMT_A** ikke er en gyldig dato i formatet **ddMMyyyy**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke oppgitt dato for første behandlingssamtale eller feltet har ugyldig format. Fant '{FORSTE_SAMT_A}'. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule020DatoForsteBehandlingssamtale.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule020DatoForsteBehandlingssamtaleTest.kt)


### Kontroll 021 : Første behandlingssamtale er før henvendelsesdato
**Gitt** feltene **HENV_DATO_A** og **FORSTE_SAMT_A** er utfylt med gyldige datoer<br/>
**når** feltet **FORSTE_SAMT_A** er før feltet **HENV_DATO_A**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Dato for første behandlingssamtale '{FORSTE_SAMT_A}' er før dato for primærklientens henvendelse '{HENV_DATO_A}' til familievernkontoret."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule021DatoForsteBehandlingssamtaleEtterHenvendelse.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule021DatoForsteBehandlingssamtaleEtterHenvendelseTest.kt)


### Kontroll 022 : Områder det har vært arbeidet med i saken
**Gitt** feltene 
**TEMA_PARREL_A**,
**TEMA_AVKLAR_A**,
**TEMA_SAMLBRUDD_A**,
**TEMA_SAMSPILL_A**,
**TEMA_BARNSIT_A**,
**TEMA_BARNFOR_A**,
**TEMA_BOSTED_A**,
**TEMA_FORELDRE_A**,
**TEMA_FORBARN_A**,
**TEMA_FLERGEN_A**,
**TEMA_SAMBARN_A**,
**TEMA_SAERBARN_A**,
**TEMA_KULTUR_A**,
**TEMA_TVANG_A**,
**TEMA_RUS_A**,
**TEMA_SYKD_A**,
**TEMA_VOLD_A**,
**TEMA_ALVH_A**
**når** ingen av feltene er utfylt med kode `1 = Ja`,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvilke områder det har vært arbeidet med siden saken ble opprettet. Feltet er obligatorisk å fylle ut, og kan inneholde mer enn ett område."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule022OmraaderArbeidet.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule022OmraaderArbeidetTest.kt)


### Kontroll 023 : Hovedform på behandlingstilbudet
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **HOVEDF_BEHAND_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke krysset av for hva som har vært hovedformen på behandlingstilbudet siden saken ble opprettet, eller feil kode er benyttet. Fant '{HOVEDF_BEHAND_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut.

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule023HovedformPaaBehandlingstilbudet.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule023HovedformPaaBehandlingstilbudetTest.kt)


### Kontroll 024 : Deltagelse i behandlingssamtaler med primærklienten i løpet av året
**Gitt** feltene
**DELT_PARTNER_A**,
**DELT_EKSPART_A**,
**DELT_BARNU18_A**,
**DELT_BARNO18_A**,
**DELT_FORELDRE_A**,
**DELT_OVRFAM_A**,
**DELT_VENN_A**,
**DELT_ANDR_A**
**når** ingen av feltene er utfylt med kode `1 = Ja`,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke krysset av for om andre deltakere i saken har deltatt i samtaler med primærklienten i løpet av rapporteringsåret. Feltene er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule024DeltagelseBehandlingssamtaler.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule024DeltagelseBehandlingssamtalerTest.kt)


### Kontroll 025 : Behandlingssamtaler for de involverte i saken i løpet av året
**Gitt** feltene
**SAMT_PRIMK_A**,
**SAMT_PARTNER_A**,
**SAMT_EKSPART_A**,
**SAMT_BARNU18_A**,
**SAMT_BARNO18_A**,
**SAMT_FORELDRE_A**,
**SAMT_OVRFAM_A**,
**SAMT_VENN_A**,
**SAMT_ANDRE_A**
**når** ingen av feltene er utfylt med antall timer større enn 0,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange behandlingssamtaler de ulike deltakerne i saken har deltatt i gjennom året. Feltet er obligatorisk å fylle ut, og kan inneholde mer enn ett område."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule025Behandlingssamtaler.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule025BehandlingssamtalerTest.kt)


### Kontroll 026 : Relasjon mellom antall behandlingssamtaler og hvem som har deltatt
| Kategori         | Deltagelse        | Samtale           |
|------------------|-------------------|-------------------|
| Partner          | `DELT_PARTNER_A`  | `SAMT_PARTNER_A`  |
| Ekspartner       | `DELT_EKSPART_A`  | `SAMT_EKSPART_A`  |
| Barn under 18 år | `DELT_BARNU18_A`  | `SAMT_BARNU18_A`  |
| Barn over 18 år  | `DELT_BARNO18_A`  | `SAMT_BARNO18_A`  |
| Foreldre         | `DELT_FORELDRE_A` | `SAMT_FORELDRE_A` |
| Øvrige familie   | `DELT_OVRFAM_A`   | `SAMT_OVRFAM_A`   |
| Venner           | `DELT_VENN_A`     | `SAMT_VENN_A`     |
| Andre            | `DELT_ANDR_A`     | `SAMT_ANDRE_A`    |
**For** hver kategori
**Gitt** feltene for deltagelse og antall samtaler for hver kategori<br/>
**når** feltet for **Deltagelse** er utfylt med kode `1 = Ja`, men feltet for antall samtaler er utfylt med `0` eller ikke utfylt,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er oppgitt at andre personer ({**Kategori**}) har deltatt i samtaler med primærklient i løpet av året, men det er ikke oppgitt hvor mange behandlingssamtaler ({**Samtale**}) de ulike personene har deltatt i gjennom av året."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule026RelasjonBehandlingssamtalerDeltatt.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule026RelasjonBehandlingssamtalerDeltattTest.kt)


### Kontroll 027 : Antall behandlingssamtaler for ansatte ved kontoret i løpet av året
**Gitt** feltene
**ANTSAMT_HOVEDT_A**,
**ANTSAMT_ANDREANS_A**
**når** ingen av feltene er utfylt med antall timer større enn 0,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke oppgitt hvor mange behandlingssamtaler hovedterapeut eller andre ansatte har deltatt i gjennom året. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule027AntallBehandlingssamtalerForAnsatteVedKontoret.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule027AntallBehandlingssamtalerForAnsatteVedKontoretTest.kt)


### Kontroll 028 : Antall behandlingssamtaler i løpet av året
**Gitt** feltet **ANTSAMT_IARET_A**<br/>
**når** feltet **ANTSAMT_IARET_A** inneholder ingen timer,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange behandlingssamtaler det er gjennomført i saken i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule028AntallBehandlingssamtaler.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule028AntallBehandlingssamtalerTest.kt)


### Kontroll 029 : Antall behandlingssamtaler siden opprettelsen
**Gitt** feltet **ANTSAMT_OPPR_A**<br/>
**når** feltet **ANTSAMT_OPPR_A** inneholder ingen timer,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange behandlingssamtaler det er gjennomført siden saken ble opprettet. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule029AntallBehandlingssamtalerSidenOpprettelsen.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule029AntallBehandlingssamtalerSidenOpprettelsenTest.kt)


### Kontroll 030 : Totalt antall timer i løpet av året
**Gitt** feltet **TIMER_IARET_A**<br/>
**når** feltet **TIMER_IARET_A** inneholder ingen timer,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre ved kontoret har anvendt på saken (timer benyttet til gruppesamtaler skal holdes utenfor) i løpet av året (for og etterarbeid skal ikke regnes med). Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule030TotaltAntallTimer.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule030TotaltAntallTimerTest.kt)


### Kontroll 031 : Totalt antall timer siden saken ble opprettet
**Gitt** feltet **TIMER_OPPR_A**<br/>
**når** feltet **TIMER_OPPR_A** inneholder ingen timer,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre ved kontoret har anvendt siden saken ble opprettet (for og etterarbeid skal ikke regnes med). Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule031TotaltAntallTimerSidenSakenBleOpprettet.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule031TotaltAntallTimerSidenSakenBleOpprettetTest.kt)


### Kontroll 032 : Samarbeid med andre instanser siden opprettelsen
**Gitt** feltene
**SAMARB_INGEN_A**,
**SAMARB_LEGE_A**,
**SAMARB_HELSE_A**,
**SAMARB_PSYKH_A**,
**SAMARB_JURIST_A**,
**SAMARB_KRISES_A**,
**SAMARB_SKOLE_A**,
**SAMARB_SOS_A**,
**SAMARB_KOMMB_A**,
**SAMARB_STATB_A**,
**SAMARB_ANDRE_A**,
**når** ingen av feltene er utfylt med antall timer større enn 0,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre ved kontoret har anvendt siden saken ble opprettet (for og etterarbeid skal ikke regnes med). Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule032SamarbeidMedAndreInstanserSidenOpprettelsen.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule032SamarbeidMedAndreInstanserSidenOpprettelsenTest.kt)


### Kontroll 033 : Status ved året slutt
**Gitt** gyldige koder fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **STATUS_ARETSSL_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hva som er sakens status ved utgangen av året. Fant '{STATUS_ARETSSL_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule033StatusVedAretsSlutt.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule033StatusVedAretsSluttTest.kt)


### Kontroll 034 : Sakens hovedtema (Fylles ut når saken avsluttes)
**Gitt** gyldige koder for feltet **HOVEDTEMA_A** fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **STATUS_ARETSSL_A** er utfylt med kode `1 = Avsluttet etter avtale med klient` eller `2 = Klient uteblitt`, og feltet **HOVEDTEMA_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er krysset av for at saken er avsluttet i rapporteringsåret, men " +
"ikke fylt ut hovedtema for saken, eller feltet har ugyldig format. Fant '{HOVEDTEMA_A}', forventet én av: {GYLDIGE KODER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule034SakensHovedtema.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule034SakensHovedtemaTest.kt)


### Kontroll 035 : Saken avsluttet, men ikke satt avslutningsdato
**Gitt** feltet **STATUS_ARETSSL_A** er utfylt med kode `1 = Avsluttet etter avtale med klient` eller `2 = Klient uteblitt`<br/>
**når** feltet **DATO_AVSL_A** ikke er en gyldig dato i formatet **ddMMyyyy**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er krysset av for at saken er avsluttet i rapporteringsåret, men ikke fylt ut dato '{DATO_AVSL_A}' for avslutning av saken."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule035SakenAvsluttetManglerAvslutningsdato.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule035SakenAvsluttetManglerAvslutningsdatoTest.kt)


### Kontroll 036 : Avslutningsdato før første samtale
**Gitt** feltene **FORSTE_SAMT_A** og **DATO_AVSL_A** er utfylt med gyldige datoer<br/>
**når** feltet **DATO_AVSL_A** er før feltet **FORSTE_SAMT_A**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Dato for avslutting av saken '{DATO_AVSL_A}' kommer før dato for første behandlingssamtale '{FORSTE_SAMT_A}'."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule036AvslutningsdatoForForsteSamtale.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule036AvslutningsdatoForForsteSamtaleTest.kt)


### Kontroll 037 : Bekymringsmelding sendt barnevernet
**Gitt** gyldige kode fra [filbeskrivelsen for skjema 52AF](file_description_52AF_2026.md)<br/>
**når** feltet **BEKYMR_MELD_A** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke svart på hvorvidt bekymringsmelding er sendt barnevernet eller ei, eller feil kode er benyttet. Fant '{BEKYMR_MELD_A}', forventet én av: {GYLDIGE KODER}. Feltet er obligatorisk å fylle ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule037BekymringsmeldingSendtBarnevernet.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule037BekymringsmeldingSendtBarnevernetTest.kt)


### Kontroll 038 : Ventetid
**Gitt** feltene **HENV_DATO_A** og **FORSTE_SAMT_A** er utfylt med gyldige datoer<br/>
**når** feltet **HENV_DATO_A** er mer enn 1 år før feltet **FORSTE_SAMT_A**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Dato for primærklientens henvendelse '{HENV_DATO_A}' til familievernkontoret er mer enn 1 år før første behandlingssamtale '{FORSTE_SAMT_A}'.

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule038Ventetid.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52a/Rule038VentetidTest.kt)


