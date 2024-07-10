# Kravspesifikasjon felles for alle regnskap

## Begrep

### <a name="alvorligehetsgrader">Alvorlighetsgrader</a>

* :skull: **FATAL**, når en kontroll ender med alvorlighetsgraden FATAL så stopper all videre kontrollering umiddelbart.
  FATAL hindrer da andre kontrollene i bli kjørt. Videre så hindrer denne innsending av filvedlegget til SSB.
* :no_entry: **ERROR**, når en kontroll ender med alvorlighetsgraden ERROR så hindrer denne innsending av filvedlegget
  til SSB.
* :warning: **WARNING**, når en kontroll ender med alvorlighetsgraden WARNING så kan filvedlegget bli sendt inn til SSB
  med advarsler.
* :information_source: **INFO**, når en kontroll ender med alvorlighetsgraden INFO så kan filvedlegget bli sendt inn til SSB med
  tilbakemeldinger som er til informasjon for avgiveren.
* :white_check_mark:**OK**, når en kontroll ender med alvorlighetsgraden vises ingen tilbakemelding av noe slag da alt
  er i orden.

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

Filbeskrivelse finnes som en mennesklig- og maskinlesbar
yaml-fil, [lenke til filbeskrivelse](/kontroller/src/main/resources/file_description_Regnskap.yaml).

## Krav

### Kontroll 003 : Skjema

**Gitt at** en har en filbeskrivelse med feltdefinisjon for skjema, en regnskapsfil med verdien for skjema og den
preutfylte verdien for variablen skjema (her kalt 'preutfylt') fra portalen<br/>
**når** verdiene deres er forskjellige<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig skjema '(skjema)'. Korrigér skjema til '(preutfylt)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule003Skjema.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule003SkjemaTest.kt)

### Kontroll 004 : Årgang

**Gitt at** en har en filbeskrivelse med feltdefinisjon for årgang, en regnskapsfil med verdien for årgang og den
preutfylte verdien for variablen årgang (her kalt 'preutfylt') fra portalen<br/>
**når** verdiene deres er forskjellige<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig årgang '(årgang)'. Korrigér årgang til '(preutfylt)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule003Skjema.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule003SkjemaTest.kt)

### Kontroll 005 : Kvartal

**Gitt at** en har en filbeskrivelse med feltdefinisjon for kvartal, en regnskapsfil med verdien for kvartal og den
preutfylte verdien for variablen kvartal (her kalt 'preutfylt') fra portalen<br/>
**når** verdiene deres er forskjellige<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig kvartal '(kvartal)'. Korrigér kvartal til '(preutfylt)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule005Kvartal.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule005KvartalTest.kt)

### Kontroll 006 : Region

**Gitt at** en har en filbeskrivelse med feltdefinisjon for region, en regnskapsfil med verdien for region og den
preutfylte verdien for variablen region (her kalt 'preutfylt') fra portalen<br/>
**når** verdiene deres er forskjellige<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig region '(region)'. Korrigér region til '(preutfylt)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule006Region.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule006RegionTest.kt)

### Kontroll 007 : Organisasjonsnummer

**Gitt at** en har en filbeskrivelse med feltdefinisjon for orgnr, en regnskapsfil med verdien for orgnr, den preutfylte
verdien for variablen orgnr (her kalt 'preutfylt') fra portalen<br/>
**når** verdiene deres er forskjellige<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig orgnr '(orgnr)'. Korrigér orgnr til én av '(preutfylt)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule007Organisasjonsnummer.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule007OrganisasjonsnummerTest.kt)

### Kontroll 008 : Foretaksnummer

**Gitt at** en har en filbeskrivelse med feltdefinisjon for foretaksnummer, en regnskapsfil med verdien for
foretaksnummer og den preutfylte verdien for variablen foretaksnummer (her kalt 'preutfylt') fra portalen<br/>
**når** verdiene deres er forskjellige<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig foretaksnummer '(foretaksnummer)'. Korrigér foretaksnummer
til '(preutfylt)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule008Foretaksnummer.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule008ForetaksnummerTest.kt)

### Kontroll 009 : Kontoklasse

| skjema | kontoklasser |
|--------|--------------|
| 0A     | 0 og 1       |
| 0B     | 2            |
| 0C     | 0 og 1       |
| 0D     | 2            |
| 0F     | 3 og 4       |
| 0G     | 5            |
| 0I     | 3 og 4       |
| 0J     | 5            |
| 0K     | 3 og 4       |
| 0L     | 5            |
| 0M     | 3 og 4       |
| 0N     | 5            |
| 0P     | 3 og 4       |
| 0Q     | 5            |
| 0X     | ' '          |
| 0Y     | ' '          |

**Gitt at** en har en filbeskrivelse med feltdefinisjon for kontoklasse, en regnskapsfil med verdien for kontoklasse, den preutfylte verdien for variablen skjema (her kalt 'preutfylt') og koblingstabellen mellom skjema og kontoklasser<br/> 
**når** kontoklasse og ikke en av kodene i kontoklasser der '(skjema)' er lik '(preutfylt)'<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig kontoklasse '(kontoklasse)'. Korrigér kontoklasse til én av '(kontoklasser)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule009Kontoklasse.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule009KontoklasseTest.kt)

### Kontroll 010 : Funksjon

**Gitt at** skjema er én av 0A, 0C, 0I, 0K, 0M, 0P og 0X, samt 0AK* og 0CK*, en har en filbeskrivelse med feltdefinisjon for funksjon, en regnskapsfil med verdien for funksjon og en liste med gyldige funksjoner (de er oppgitt i underside/kravspek)<br/>
**når** funksjon avviker fra gyldige funksjoner og kvartal er 1 eller 2<br/>
**så** gi en :warning:**ADVARSEL** med meldingen "Fant ugyldig funksjon '(funksjon)'. Korrigér funksjon til én av '(funksjoner)'"

**når** funksjon avviker fra gyldige funksjoner og kvartal er 3, 4 eller blank for årsregnskap<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig funksjon '(funksjon)'. Korrigér funksjon til én av '(funksjoner)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule010Funksjon.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule010FunksjonTest.kt)

### Kontroll 011 : Kapittel

**Gitt at** skjema er én av 0B, 0D, 0J, 0L, 0N, 0Q og 0Y, samt 0BK* og 0DK*, en har en filbeskrivelse med feltdefinisjon for kapittel, en regnskapsfil med verdien for kapittel og en liste med gyldige kapitler (de er oppgitt i underside/kravspek)<br/>
**når** kapittel avviker fra gyldige kapitler og kvartal er 1 eller 2<br/>
**så** gi en :warning:**ADVARSEL** med meldingen "Fant ugyldig kapittel '(kapittel)'. Korrigér kapittel til én av '(kapitler)'"

**når** kapittel avviker fra gyldige kapitler<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig kapittel '(kapittel)'. Korrigér kapittel til én av '(kapitler)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule011Kapittel.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule011KapittelTest.kt)

### Kontroll 012 : Art

**Gitt at** skjema er én av 0A, 0C, 0I, 0K, 0M, 0P og 0X, samt 0AK* og 0CK*, en har en filbeskrivelse med feltdefinisjon for art, en regnskapsfil med verdien for art og en liste med gyldige arter (de er oppgitt i underside/kravspek)<br/>
**når** art avviker fra gyldige arter og kvartal er 1 eller 2<br/>
**så** gi en :warning:**ADVARSEL** med meldingen "Fant ugyldig art '(art)'. Korrigér art til én av '(arter)'"

**når** art avviker fra gyldige arter og kvartal er 3, 4 eller blank for årsregnskap<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig art '(art)'. Korrigér art til én av '(arter)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule012Art.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule012ArtTest.kt)

### Kontroll 013 : Sektor

**Gitt at** skjema er én av 0B, 0D, 0J, 0L, 0N, 0Q og 0Y, samt 0BK* og 0DK*, en har en filbeskrivelse med feltdefinisjon for sektor, en regnskapsfil med verdien for sektor og en liste med gyldige sektorer (de er oppgitt i underside/kravspek)<br/>
**når** sektor avviker fra gyldige sektorer og kvartal er 1 eller 2<br/>
**så** gi en :warning:**ADVARSEL** med meldingen "Fant ugyldig sektor '(sektor)'. Korrigér sektor til én av '(sektorer)'"

**når** sektor avviker fra gyldige sektorer<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig sektor '(sektor)'. Korrigér sektor til én av '(sektorer)'"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule013Sektor.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule013SektorTest.kt)

### Kontroll 014 : Beløp

**Gitt at** en har en filbeskrivelse med feltdefinisjon for beløp, en regnskapsfil med verdien for beløp<br/>
**når** beløp inneholder tabulator-tegn eller ikke matcher: starter med 0 eller flere mellomrom og eventuelt minus-tegn og avslutter med 1 eller flere sifre<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Fant ugyldig beløp '(beløp)'. Korrigér beløp"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule014Belop.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule014BelopTest.kt)

### Kontroll 015 : Dubletter

**Gitt at** en har en filbeskrivelse med feltdefinisjoner, en regnskapsfil med verdier og en liste med felter (her kalt dublettfelter)<br/>
**når** skjema er én av 0A, 0B, 0C, 0D, 0F, 0G, 0I, 0J, 0K, 0L, 0M, 0N, 0P og 0Q, samt 0AK*, 0BK*, 0CK* og 0DK* og dublettfelter er (kontoklasse), (funksjon_kapittel) og (art_sektor) har samme koder (heretter kalt kombo)<br/>
**så** gi en :warning:**ADVARSEL** med meldingen "Det er oppgitt flere beløp på samme kombinasjon av (kombo) med beløpene (liste med beløp).<br/>
Hvis dette er riktig, kan du sende inn filen, og beløpene summeres hos SSB.
Dersom dette er feil må recordene korrigeres før innsending til SSB."

**når** skjema er 0X og dublettfelter er (foretaksnr), (funksjon_kapittel) og (art_sektor) har samme koder (heretter kalt kombo)<br/>
**så** gi en :warning:**ADVARSEL** med meldingen "Det er oppgitt flere beløp på samme kombinasjon av (kombo) med beløpene (liste med beløp).<br/>
Hvis dette er riktig, kan du sende inn filen, og beløpene summeres hos SSB.
Dersom dette er feil må recordene korrigeres før innsending til SSB."

**når** skjema er 0Y og dublettfelter er (art_sektor) har samme koder (heretter kalt kombo)<br/>
**så** gi en :warning:**ADVARSEL** med meldingen "Det er oppgitt flere beløp på samme kombinasjon av (kombo) med beløpene (liste med beløp).<br/>
Hvis dette er riktig, kan du sende inn filen, og beløpene summeres hos SSB.
Dersom dette er feil må recordene korrigeres før innsending til SSB."

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule015Duplicates.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/regnskap/Rule015DuplicatesTest.kt)
