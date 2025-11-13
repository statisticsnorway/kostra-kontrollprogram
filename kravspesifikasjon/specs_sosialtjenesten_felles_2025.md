# Kravspesifikasjon felles for Sosialtjenesten

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


## Krav

<a id="003"></a>
### Kontroll 003 : Kommunenummer

**Gitt at** en har en filbeskrivelse med feltdefinisjon for KOMMUNE_NR, en datafil med verdi for KOMMUNE_NR i alle rader og den
preutfylte verdien for variablen KOMMUNE_NR (her kalt 'kommunenummer') fra skjemaet i portalen<br/>
**når** KOMMUNE_NR er ulik kommunenummer<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Korrigér kommunenummeret. Fant {KOMMUNE_NR}, forventet {kommunenummer}."** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule003Kommunenummer.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule003KommunenummerTest.kt)

### Kontroll 003 : Bydelsnummer

Definisjon:<br/>
OSLO = "0301"<br/>
OSLO_DISTRICTS = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15"]<br/>

**Gitt at** en har en filbeskrivelse med feltdefinisjon for KOMMUNE_NR og BYDELSNR, en datafil med verdi for KOMMUNE_NR og BYDELSNR i alle rader og den
preutfylte verdien for variablen KOMMUNE_NR (her kalt 'kommunenummer') og BYDELSNR (her kalt 'bydelsnummer') fra skjemaet i portalen<br/>
**når** {KOMMUNE_NR} er lik {OSLO} og {BYDELSNR} er ingen av kodene i {OSLO_BYDELER}<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Korrigér bydel. Fant {BYDELSNR}, forventet en av ${OSLO_DISTRICTS}."** for hver rad hvor dette inntreffer

**når** {KOMMUNE_NR} er ulik {OSLO} og BYDELSNR er ulik blank<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Korrigér bydel. Fant {BYDELSNR}, forventet blank / '  '."** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule003Bydelsnummer.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule003BydelsnummerTest.kt)

<a id="004"></a>
### Kontroll 004 : Oppgaveår

**Gitt at** en har en filbeskrivelse med feltdefinisjon for VERSION, en datafil med verdi for VERSION i alle rader og den
preutfylte verdien for variablen AARGANG fra skjemaet i portalen<br/>
**når** {VERSION} er ulik {AARGANG}<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Korrigér årgang. Fant {VERSION}, forventet {AARGANG}."** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule004OppgaveAar.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule004OppgaveAarTest.kt)

<a id="004A"></a>
### Kontroll 004A : Fødselsdato

**Gitt at** en har en filbeskrivelse med feltdefinisjon for FODSELSDATO, en datafil med verdi for FODSELSDATO i alle rader<br/>
**når** {FODSELSDATO} er en gyldig dato eller inneholder ugyldige tegn<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Det er ikke oppgitt fødselsdato eller fødselsdato er ugyldig. Korriger fødselsdato til en gyldig dato. Fant ({FODSELSDATO}), forventet (DDMMÅÅÅÅ)"** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule004AFodselsDato.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule004AFodselsDatoTest.kt)

<a id="005"></a>
### Kontroll 005 : Fødselsdato

**Gitt at** en har en filbeskrivelse med feltdefinisjon for PERSON_FODSELSNR, en datafil med verdi for PERSON_FODSELSNR i alle rader<br/>
**når** {PERSON_FODSELSNR} er matematisk ugyldig<br/>
**så** gi en :warning:**Advarsel** med meldingen **"Det er ikke oppgitt fødselsnummer/d-nummer på deltakeren eller fødselsnummeret/d-nummeret inneholder feil. Med mindre det er snakk om en utenlandsk statsborger som ikke er tildelt norsk personnummer eller d-nummer, skal feltet inneholde deltakeren fødselsnummer/d-nummer (11 siffer)."** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule005Fodselsnummer.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule005FodselsnummerTest.kt)

<a id="008"></a>
### Kontroll 008 : Kjønn

**Gitt at** en har en filbeskrivelse med feltdefinisjon for KJONN som har en kodeliste med gyldige koder, en datafil med verdi for KJONN i alle rader<br/>
**når** {KJONN} er utfylt med en annen kode enn de som i den gyldige kodelista<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Korrigér kjønn. Fant '{KJONN}', forventet én av {KJONN.kodeliste}. Mottakerens kjønn er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule008Kjonn.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule008KjonnTest.kt)

<a id="009"></a>
### Kontroll 009 : Sivilstand

**Gitt at** en har en filbeskrivelse med feltdefinisjon for EKTSTAT (sivilstand) som har en kodeliste med gyldige koder, en datafil med verdi for EKTSTAT i alle rader<br/>
**når** {EKTSTAT} er utfylt med en annen kode enn de som i den gyldige kodelista<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Korrigér sivilstand. Fant '{EKTSTAT}', forventet én av {EKTSTAT.kodeliste}. Mottakerens sivilstand/sivilstatus ved siste kontakt med sosial-/NAV-kontoret er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule009Sivilstand.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule009SivilstandTest.kt)

<a id="010"></a>
### Kontroll 010 : Har barn under 18 år

**Gitt at** en har en filbeskrivelse med feltdefinisjon for BU18 (har barn under 18 år) som har en kodeliste med gyldige koder, en datafil med verdi for BU18 i alle rader<br/>
**når** {BU18} er utfylt med en annen kode enn de som i den gyldige kodelista<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Korrigér forsørgerplikt. Fant '{BU18}', forventet én av {BU18.kodeliste}'. Det er ikke krysset av for om deltakeren har barn under 18 år, som deltakeren (eventuelt ektefelle/samboer) har forsørgerplikt for, og som bor i husholdningen ved siste kontakt. Feltet er obligatorisk å fylle ut."** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule010HarBarnUnder18.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule010HarBarnUnder18Test.kt)

<a id="011"></a>
### Kontroll 011 : Det bor barn under 18 år i husholdningen. Mangler antall barn.

**Gitt at** en har en filbeskrivelse med feltdefinisjon for BU18 (har barn under 18 år) og ANTBU18 (antall barn under 18 år), en datafil med verdi for BU18 og ANTBU18<br/>
**når** {BU18} er lik (1 = Ja) og {ANTBU18} ikke er større enn 0<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Det er krysset av for at det bor barn under 18 år i husholdningen som mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke oppgitt hvor mange barn '({ANTBU18})' som bor i husholdningen. Feltet er obligatorisk å fylle ut når det er oppgitt at det bor barn under 18 år i husholdningen."** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule011HarBarnUnder18MotAntallBarnUnder18.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule011HarBarnUnder18MotAntallBarnUnder18Test.kt)

<a id="012"></a>
### Kontroll 012 : Det bor barn under 18 år i husholdningen.

**Gitt at** en har en filbeskrivelse med feltdefinisjon for BU18 (har barn under 18 år) og ANTBU18 (antall barn under 18 år), en datafil med verdi for BU18 og ANTBU18<br/>
**når** {ANTBU18} er større enn 0 og {BU18} er ulik (1 = Ja)<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Det er oppgitt {ANTBU18} barn under 18 år som bor i husholdningen som mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke oppgitt at det bor barn i husholdningen. Feltet er obligatorisk å fylle ut når det er oppgitt antall barn under 18 år som bor i husholdningen."** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule012AntallBarnUnder18MotHarBarnUnder18.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule012AntallBarnUnder18MotHarBarnUnder18Test.kt)

<a id="013"></a>
### Kontroll 013 : Mange barn under 18 år i husholdningen.

Definisjon:<br/>
CHILD_COUNT_THRESHOLD = 14

**Gitt at** en har en filbeskrivelse med feltdefinisjon for ANTBU18 (antall barn under 18 år), en datafil med verdi for ANTBU18<br/>
**når** {ANTBU18} er lik eller større enn {CHILD_COUNT_THRESHOLD}<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Antall barn ({ANTBU18}) under 18 år i husholdningen er {CHILD_COUNT_THRESHOLD} eller flere, er dette riktig?"** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule013AntallBarnUnder18.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule013AntallBarnUnder18Test.kt)




