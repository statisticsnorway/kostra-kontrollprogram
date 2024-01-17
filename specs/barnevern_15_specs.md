# Kravspesifikasjon til kontrollene for KOSTRA-Barnevern


## Innhold


## <a name="omfang">Omfang</a>
Kontaktepost: **ssb-barnevern@ssb.no**

Generell beskrivelse: Barnevernsstatistikken er en individstatistikk som hentes inn fra alle kommunale barnevernstjenester som et filuttrekk fra kommunens fagsystem for barnevern. Oppbyggingen er slik:

1. Opplysninger om kommunen
2. Opplysninger om klienten
3. Meldinger
4. Undersøkelser
5. Tiltak etter lov om barneverntjenester
6. Plantyper
7. Flyttinger
8. Er barnet klient ved utgangen av rapporteringsåret?

### <a name="kontrollrapport">Advarsler og feil</a>
Kontrollprogrammet lager en kontrollrapport som er en liste med advarsler,og feil som hindrer innsending. Advarsler skal kommunens barnevernstjeneste sjekke og om mulig rette opp. Feil som hindrer innsending må rettes for å få sendt inn fil. I kontrollrapporten vil identifikasjon av hvilket individ kontrollen slår ut for være journalnummer. Journalnummer kan derfor ikke være noe som kan identifisere individet, for eksempel fødsels- og personnummer eller DUF-nummer.


## Definisjoner

### <a name="bvl">Barnevernloven</a>
Barnevernloven brukes i to forskjellige versjoner:
- Versjonen fra 1992 refereres som **<a name="bvl">BVL</a>** av historiske årsaker, eventuelt **<a name="bvl1992">BVL1992</a>** i dokumentasjonssammenheng, og gjelder til 31. desember 2022.<br/>
- Versjonen fra 2021 refereres som **<a name="bvl2021">BVL2021</a>** og gjelder fra 1. januar 2023.


### <a name="innrapporteringskanaler">Innrapporteringskanaler</a>
Rapportering til SSB skjer i 2 forskjellige kanaler:
- **<a name="fagsystem_kanal">FAGSYSTEM</a>**, direkte rapportering fra fagsystem via Fiks til SSB.
- **<a name="kostra_kanal">KOSTRA</a>**, klassisk og indirekte rapportering ved hjelp av filuttrekk.


### <a name="omsorgstiltak">Omsorgstiltak</a>
Et Tiltak er en Omsorgstiltak dersom en av følgende:
- Lovhjemmel/Lov = **[BVL](#bvl)** og Lovhjemmel/Kapittel = 4 og Lovhjemmel/Paragraf = 12
- Lovhjemmel/Lov = **[BVL](#bvl)** og Lovhjemmel/Kapittel = 4 og Lovhjemmel/Paragraf = 8 og Lovhjemmel/Ledd er én av 2 eller 3
- Lovhjemmel/Lov = **[BVL](#bvl)** og Lovhjemmel/Kapittel = 4 og Lovhjemmel/Paragraf = 8 og én av JmfrLovhjemmel/Kapittel = 4 og JmfrLovhjemmel/Paragraf = 12
- Lovhjemmel/Lov = **[BVL2021](#bvl2021)** og Lovhjemmel/Kapittel = 5 og Lovhjemmel/Paragraf = 1


### <a name="plasseringstiltak">Plasseringstiltak</a>
Et Tiltak er et plasseringstiltak dersom Kategori/@Kode er en av følgende koder:<br/>
1.1, 1.2, 1.99, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.99 eller 8.2<br/>

### <a name="xsd">XSD</a>
XML Schema Definition eller XSD beskriver strukturen og innholdet i et XML-dokument. 
Den brukes først og fremst til å definere elementene, attributtene og datatypene dokumentet kan inneholde. 
Informasjonen i XSD brukes til å verifisere om hvert element, attributt eller datatype i dokumentet samsvarer med beskrivelsen.


## Filbeskrivelse
Filbeskrivelsen består av en 3-delt [XSD](#xsd). Filene er [KostraBarnevern.xsd](/kostra-barnevern/src/main/resources/KostraBarnevern.xsd), [Avgiver.xsd](/kostra-barnevern/src/main/resources/Avgiver.xsd) og [Individ.xsd](/kostra-barnevern/src/main/resources/Individ.xsd) og finnes under [/kostra-barnevern/src/main/resources/](/kostra-barnevern/src/main/resources/)


## Kontroller
I forbindelse med kontrollering så blir noen variabler sendt med fra portalen og blir benyttet i noen av kontrollene. Disse er:
* <a name="rapporteringsar">**rapporteringsår**</a>, for eksempel **2023**
* <a name="kommunenummer">**kommunenummer**</a>, for eksempel **0301**
* <a name="kommunenavn">**kommunenavn**</a>, for eksempel **Oslo**
* <a name="organisasjonsnummer">**organisasjonsnummer**</a>, for eksempel **958935420**

Andre variabler:
* <a name="telledato">**telledato**</a> som er 31. desember i **rapporteringsår**, for eksempel **31. desember 2023**
* <a name="forrigetelledato">**forrige_telledato**</a> som er 31. desember i året før **rapporteringsår**, for eksempel **31. desember 2022**


### <a name="avgiver">Avgiver</a>

#### <a name="avgiver_02">Avgiver Kontroll 2: Årgang</a>

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Versjon** og [**rapporteringsår**](#rapporteringsar) fra skjema 15F. Barnevern YYYY - filuttrekk er like <br/>
når @Versjon og rapporteringsår er forskjellige<br/>
så gi en :no_entry:**FEIL** med meldingen "Filen inneholder feil rapporteringsår {@Versjon}, forventet {rapporteringsåret}."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver02.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver02Test.kt)

#### <a name="avgiver_03">Avgiver Kontroll 3: Organisasjonnummer</a>

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Organisasjonnummer**<br/>
når @Organisasjonnummer er blank<br/>
så gi en :no_entry:**FEIL** med meldingen "Filen mangler organisasjonsnummer. Oppgitt organisasjonsnummer er '{**organisasjonsnummer**}'"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver03.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver03Test.kt)

#### <a name="avgiver_04">Avgiver Kontroll 4: Kommunenummer</a>

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Kommunenummer** og [**kommunenummer**](#kommunenummer) fra skjema 15F. Barnevern YYYY - filuttrekk er like <br/>
når **@Kommunenummer** og **kommunenummer** er forskjellige<br/>
så gi en :no_entry:**FEIL** med meldingen "Filen inneholder feil kommunenummer. Forskjellig kommunenummer i skjema ({**kommunenummer**}) og filuttrekk ({**@Kommunenummer**})."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver04.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver04Test.kt)

#### <a name="avgiver_06">Avgiver Kontroll 6: Kommunenavn</a>

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Kommunenavn** finnes<br/>
når **@Kommunenavn** er blank<br/>
så gi en :no_entry:**FEIL** med meldingen "Filen mangler kommunenavn."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver06.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver06Test.kt)


### Individ

#### <a name="individ_01a">Individ Kontroll 01a: Validering av datoer</a>

Gitt at **minimumsdato** er 1. januar 1998 og **maksimumsdato** er 31. desember 2049<br/>
når /Barnevern/Individ/**@StartDato** er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Individ startdato: Dato ({**@StartDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"

når /Barnevern/Individ/**@SluttDato** finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Individ sluttDato: Dato ({**@SluttDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"

når /Barnevern/Individ/Melding/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Melding startdato: Dato ({**@StartDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"

når /Barnevern/Individ/Melding/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Melding sluttDato: Dato ({**@SluttDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"

når /Barnevern/Individ/Undersokelse/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersøkelse startdato: Dato ({**@StartDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"

når /Barnevern/Individ/Undersokelse/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersøkelse sluttDato: Dato ({**@SluttDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"

når /Barnevern/Individ/Plan/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Plan startdato: Dato ({**@StartDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"

når /Barnevern/Individ/Plan/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Plan sluttDato: Dato ({**@SluttDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"

når /Barnevern/Individ/Tiltak/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak startdato: Dato ({**@StartDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"

når /Barnevern/Individ/Tiltak/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak sluttDato: Dato ({**@SluttDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"

når /Barnevern/Individ/Flytting/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Flytting sluttDato: Dato ({**@SluttDato**}) må være mellom {**minimumsdato**} og {**maksimumsdato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ01a.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ01aTest.kt)

#### <a name="individ_02a">Individ Kontroll 02a: Startdato etter sluttdato</a>
Gitt at /Barnevern/Individ/**@StartDato** og /Barnevern/Individ/**@SluttDato** finnes<br/>
når @StartDato er etter @SluttDato<br/>
så gi en :no_entry:**FEIL** med meldingen "Individets startdato ({**@StartDato**}) er etter sluttdato ({**@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ02a.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ02aTest.kt)

#### <a name="individ_02b">Individ Kontroll 02b: Sluttdato mot versjon</a>
Gitt at /Barnevern/Individ/**@SluttDato** finnes<br/>
når @SluttDato er før **forrige_telledato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Individets sluttdato ({**@SluttDato**}) er før forrige telletidspunkt ({**forrige_telledato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ02b.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ02bTest.kt)

#### <a name="individ_02d">Individ Kontroll 02d: Avslutta 31.12 medfører at sluttdato skal være satt</a>
Gitt at /Barnevern/Individ/**@Avslutta3112** finnes<br/>
når **@Avslutta3112** = "1" (Ja) og @SluttDato mangler<br/>
så gi en :no_entry:**FEIL** med meldingen "Individet er avsluttet hos barnevernet og skal dermed være avsluttet. Sluttdato er {**@SluttDato**}. Kode for avsluttet er '1'."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ02d.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ02dTest.kt)

#### <a name="individ_03">Individ Kontroll 03: Fødselsnummer og DUF-nummer</a>
Gitt /Barnevern/Individ/**@Fodselsnummer** finnes<br/>
når **@Fodselsnummer** ikke oppfyller noen følgende av definisjoner:<br/>
* Fødselsnummer, beskrevet på  [https://www.udi.no/ord-og-begreper/fodselsnummer/](https://www.udi.no/ord-og-begreper/fodselsnummer/), 
* D-nummer, beskrevet på [https://www.udi.no/ord-og-begreper/d-nummer/](https://www.udi.no/ord-og-begreper/d-nummer/),
* Fødselsdato (DDMMÅÅ) + 00100,
* Fødselsdato (DDMMÅÅ) + 00200,
* Fødselsdato (DDMMYY) + 55555
* Termindato (DDMMYY) + 99999

så gi en :no_entry:**FEIL** med meldingen "Feil i fødselsnummer. Kan ikke identifisere individet."

Gitt at /Barnevern/Individ/**@Fodselsnummer** mangler og /Barnevern/Individ/**@DUFnummer** finnes <br/>
når modulo11-sjekk av **@DUFnummer** feiler<br/>
så gi en :no_entry:**FEIL** med meldingen "DUF-nummer mangler. Kan ikke identifisere individet."

Gitt at /Barnevern/Individ/**@Fodselsnummer** mangler og /Barnevern/Individ/**@DUFnummer** mangler <br/>
så gi en :no_entry:**FEIL** med meldingen "Fødselsnummer og DUF-nummer mangler. Kan ikke identifisere individet."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ03.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ03Test.kt)

#### <a name="individ_04">Individ Kontroll 04: Dublett på fødselsnummer</a>
Gitt Individ Kontroll 07: Klient over 25 år avsluttes finnes og oppfyller én av følgende definisjoner:<br/>
* Fødselsnummer, beskrevet på  [https://www.udi.no/ord-og-begreper/fodselsnummer/](https://www.udi.no/ord-og-begreper/fodselsnummer/),
* D-nummer, beskrevet på [https://www.udi.no/ord-og-begreper/d-nummer/](https://www.udi.no/ord-og-begreper/d-nummer/),

når **@Fodselsnummer** forekommer flere ganger<br/>
så gi en :no_entry:**FEIL** med meldingen "Fødselsnummeret i journalnummer {**@Journalnummer**} fins også i journalene {journalnummerliste}"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/BarnevernValidator.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/BarnevernValidatorTest.kt)

#### <a name="individ_05">Individ Kontroll 05: Dublett på journalnummer</a>
Gitt /Barnevern/Individ/**@Journalnummer** finnes<br/>
når **@Journalnummer** forekommer flere ganger<br/>
så gi en :no_entry:**FEIL** med meldingen "Journalnummer {**@Journalnummer**} forekommer {antall} ganger."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/BarnevernValidator.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/BarnevernValidatorTest.kt)

#### <a name="individ_06">Individ Kontroll 06: Har meldinger, planer eller tiltak</a>
Gitt at /Barnevern/Individ finnes<br/>
når ingen /Barnevern/Individ/Melding finnes og ingen /Barnevern/Individ/Plan finnes og ingen /Barnevern/Individ/Tiltak finnes<br/>
så gi en :no_entry:**FEIL** med meldingen "Individet har ingen meldinger, planer eller tiltak i løpet av året"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ06.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ06Test.kt)

#### <a name="individ_07">Individ Kontroll 07: Klient over 25 år avsluttes</a>
Gitt at **alder** i år er utledet fra forskjellen mellom telledato og dato-delen i /Barnevern/Individ/**@Fodselsnummer**<br/>
når alder er større enn 25 år<br/>
så gi en :no_entry:**FEIL** med meldingen "Individet er {**alder**} år og skal avsluttes som klient"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ07.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ07Test.kt)

#### <a name="individ_08">Individ Kontroll 08: Alder i forhold til tiltak</a>
Gitt at **alder** i år er utledet fra forskjellen mellom telledato og dato-delen i /Barnevern/Individ/**@Fodselsnummer**<br/>
når alder er større enn 17 år og /Barnevern/Individ/**Tiltak** mangler<br/>
så gi en :warning:**ADVARSEL** med meldingen "Individet er over 18 år og skal dermed ha tiltak"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ08.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ08Test.kt)

#### <a name="individ_09">Individ Kontroll 09: Bydelsnummer</a>
Gitt at **kommunenummer** starter med 0301 og /Barnevern/Individ/**@Bydelsnummer** mangler<br/>
så gi en :no_entry:**FEIL** med meldingen "Filen mangler bydelsnummer."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ09.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ09Test.kt)

#### <a name="individ_10">Individ Kontroll 10: Bydelsnavn</a>
Gitt at **kommunenummer** starter med 0301 og /Barnevern/Individ/**@Bydelsnavn** mangler<br/>
så gi en :no_entry:**FEIL** med meldingen "Filen mangler bydelsnavn."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ10.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ10Test.kt)

#### <a name="individ_11">Individ Kontroll 11: Fødselsnummer</a>
Gitt at /Barnevern/Individ/**@Fodselsnummer** mangler eller er blank, eller at /Barnevern/Individ/**@Fodselsnummer** er et ugyldig fødselsnummer<br/>
så gi en :no_entry:**FEIL** med meldingen "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ11.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ11Test.kt)

#### <a name="individ_12">Individ Kontroll 12: Fødselsnummer</a>
Gitt at /Barnevern/Individ/**@Fodselsnummer** mangler, eller at /Barnevern/Individ/**@Fodselsnummer** er et ugyldig fødselsnummer<br/>
så gi en :warning:**ADVARSEL** med meldingen "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ12.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ12Test.kt)

#### <a name="individ_19">Individ Kontroll 19: DUF-nummer</a>
Gitt at /Barnevern/Individ/**@DUFnummer** finnes<br/>
når **@DUFnummer** er ugyldig<br/>
så gi en :no_entry:**FEIL** med meldingen "Individet har ufullstendig DUF-nummer. Korriger DUF-nummer."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ19.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ19Test.kt)


### <a name="melding">Melding</a>

#### <a name="melding_02a">Melding Kontroll 2a: Startdato etter sluttdato</a>
Gitt at /Barnevern/Individ/Melding/**@Id**, <br/>/Barnevern/Individ/Melding/**@StartDato** og <br/>/Barnevern/Individ/Melding/**@SluttDato** finnes<br/>
når @StartDato er etter @SluttDato<br/>
så gi en :no_entry:**FEIL** med meldingen "Melding ({**@Id**}). Meldingens startdato ({**@StartDato**}) er etter meldingens sluttdato ({**@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding02a.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding02aTest.kt)

#### <a name="melding_02c">Melding Kontroll 2c: Sluttdato mot individets sluttdato</a>
Gitt at /Barnevern/**Individ/@SluttDato**, <br/>
/Barnevern/Individ/**Melding/@Id**, <br/>
/Barnevern/Individ/**Melding/@SluttDato** og <br/>
/Barnevern/Individ/**Melding/@Konklusjon** finnes<br/>
når **Melding/@SluttDato** er etter **Individ/@SluttDato** og **Melding/@Konklusjon** = "1" (Henlagt)<br/>
så gi en :no_entry:**FEIL** med meldingen "Melding ({**Melding/@Id**}}). Meldingens sluttdato ({**Melding/@SluttDato**}) er etter individets sluttdato ({**Individ/@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding02c.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding02cTest.kt)

#### <a name="melding_02d">Melding Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på</a>
Gitt at /Barnevern/Individ/**@Avslutta3112** = "1" (Avsluttet)<br/>
for hver Melding i /Barnevern/Individ/<br/>
når Melding/@SluttDato mangler <br/>
eller /Barnevern/Individ/@SluttDato mangler <br/>
eller Melding/@SluttDato og er etter **telledato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Melding ({**Melding/@Id**}}). Individet er avsluttet hos barnevernet og dets meldinger skal dermed være avsluttet. Sluttdato  er {**Individ/@SluttDato** eller "uoppgitt"}"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding02d.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding02dTest.kt)

#### <a name="melding_02e">Melding Kontroll 2e: Startdato mot individets startdato</a>
Gitt at /Barnevern/**Individ/@StartDato** og <br/>/Barnevern/Individ/**Melding/@StartDato** finnes<br/>
når **Melding/@StartDato** er før **Individ/@StartDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Melding ({**Melding/@Id**}). Meldingens startdato ({**Melding/@StartDato**}) skal være lik eller etter individets startdato ({**Individ/@StartDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding02e.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding02eTest.kt)

#### <a name="melding_03">Melding Kontroll 3: Behandlingstid av melding</a>
Gitt at /Barnevern/Individ/Melding/**@StartDato** og /Barnevern/Individ/Melding/**@SluttDato** finnes<br/>
når **@SluttDato** er 8 eller flere dager etter **@StartDato**<br/>
så gi en :warning:**ADVARSEL** med meldingen "Melding ({**@Id**}). Fristoverskridelse på behandlingstid for melding, ({**@StartDato**} -> {**@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding03.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding03Test.kt)

#### <a name="melding_04">Melding Kontroll 4: Kontroll av konkludert melding, melder</a>
Gitt at /Barnevern/Individ/Melding/**@SluttDato** finnes<br/>
når **@SluttDato** er før **forrige_telledato**<br/>
og @Konklusjon finnes<br/>
og @Konklusjon er enten "1" eller "2"<br/>
og Melding/Melder/@Kode mangler<br/>
så gi en :no_entry:**FEIL** med meldingen "Melding ({**Melding/@Id**}}). Konkludert melding mangler melder(e)."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding04.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding04Test.kt)

#### <a name="melding_05">Melding Kontroll 5: Kontroll av konkludert melding, saksinnhold</a>
Gitt at /Barnevern/Individ/Melding/**@SluttDato** finnes<br/>
når **@SluttDato** er før **forrige_telledato**<br/>
og @Konklusjon finnes<br/>
og @Konklusjon er enten "1" eller "2"<br/>
og Melding/Saksinnhold/@Kode mangler<br/>
så gi en :no_entry:**FEIL** med meldingen "Melding ({**Melding/@Id**}}). Konkludert melding mangler saksinnhold."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding05.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melding05Test.kt)

#### <a name="melder_02">Melder Kontroll 2: Kontroll av kode og presisering</a>
Gitt at /Barnevern/Individ/**Melding/Melder** finnes<br/>
for hver **Melder** i **Melding/Melder**<br/>
når **Melder/@Kode** = "22" (Andre offentlige instanser)<br/>
så gi en :no_entry:**FEIL** med meldingen "Melder med kode ({**Melder/@Kode**}) mangler presisering"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melder02.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Melder02Test.kt)

#### <a name="saksinnhold_02">Saksinnhold Kontroll 2: Kontroll av kode og presisering</a>
Gitt at /Barnevern/Individ/**Melding/Saksinnhold** finnes<br/>
for hver **Saksinnhold** i **Melding/Saksinnhold**<br/>
når **Saksinnhold/@Kode** er enten "18" (Andre forhold ved foreldre/ familien) eller "19" (Andre forhold ved barnets situasjon)<br/>
så gi en :no_entry:**FEIL** med meldingen "Saksinnhold med kode ({**Saksinnhold/@Kode**}) mangler presisering"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Saksinnhold02.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Saksinnhold02Test.kt)


### <a name="undersokelse">Undersøkelse</a>

#### <a name="undersokelse_02a">Undersøkelse Kontroll 2a: Startdato etter sluttdato</a>
Gitt at /Barnevern/Individ/Undersokelse/**@Id**, <br/>
/Barnevern/Individ/Undersokelse/**@StartDato** og <br/>
/Barnevern/Individ/Undersokelse/**@SluttDato** finnes<br/>
når **@StartDato** er etter **@SluttDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersokelse ({**@Id**}). Undersøkelsens startdato ({**@StartDato**}) er etter undersøkelsens sluttdato ({**@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse02a.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse02aTest.kt)

#### <a name="undersokelse_02b">Undersøkelse Kontroll 2b: Sluttdato mot rapporteringsår</a>
Gitt at /Barnevern/Individ/Undersokelse/**@SluttDato** finnes<br/>
når året i **@SluttDato** og **rapporteringsår** er forskjellige<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersøkelse ({**@Id**}). Undersøkelsens sluttdato ({**@SluttDato**}) er ikke i rapporteringsåret ({**rapporteringsår**}) "<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse02b.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse02bTest.kt)

#### <a name="undersokelse_02c">Undersøkelse Kontroll 2c: Sluttdato mot individets sluttdato</a>
Gitt at /Barnevern/**Individ/@SluttDato**, <br/>
/Barnevern/Individ/**Undersokelse/@Id**, <br/>
/Barnevern/Individ/**Undersokelse/@SluttDato** og <br/>
/Barnevern/Individ/**Undersokelse/@Konklusjon** finnes<br/>
når **Undersokelse/@SluttDato** er etter **Individ/@SluttDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersokelse ({**Undersokelse/@Id**}}). Undersøkelsens sluttdato ({**Undersokelse/@SluttDato**}) er etter individets sluttdato ({**Individ/@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse02c.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse02cTest.kt)

#### <a name="undersokelse_02d">Undersøkelse Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på undersøkelsen</a>
Gitt at /Barnevern/Individ/**@Avslutta3112** = "1" (Avsluttet)<br/>
for hver Undersokelse i /Barnevern/Individ/<br/>
når Undersokelse/@SluttDato mangler <br/>
eller /Barnevern/Individ/@SluttDato mangler <br/>
eller Undersokelse/@SluttDato og er etter **telledato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersokelse ({**Undersokelse/@Id**}}). Individet er avsluttet hos barnevernet og dets undersøkelser skal dermed være avsluttet. Sluttdato er {**Undersokelse/@SluttDato** eller "uoppgitt"}"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse02d.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse02dTest.kt)

#### <a name="undersokelse_02e">Undersøkelse Kontroll 2e: Startdato mot individets startdato</a>
Gitt at /Barnevern/**Individ/@StartDato** og <br/>
/Barnevern/Individ/**Undersokelse/@StartDato** finnes<br/>
når **Undersokelse/@StartDato** er før **Individ/@StartDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersokelse ({**Undersokelse/@Id**}). Undersøkelsens startdato ({**Undersokelse/@StartDato**}) skal være lik eller etter individets startdato ({**Individ/@StartDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse02e.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse02eTest.kt)

#### <a name="undersokelse_03">Undersøkelse Kontroll 3: Kontroll av kode og presisering</a>
Gitt at /Barnevern/Individ/**Melding/Undersokelse** finnes<br/>
for hver **Undersokelse** i **Melding/Undersokelse**<br/>
når **Undersokelse/@Kode** er 5 (Undersøkelsen henlagt som følge av flytting)<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersokelse ({**Undersokelse/@Id**}). Undersøkelse der kode for konklusjon er {**Undersokelse/@Konklusjon**} mangler presisering"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse03.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse03Test.kt)

#### <a name="undersokelse_04">Undersøkelse Kontroll 4: Konklusjon av undersøkelse</a>
Gitt at /Barnevern/Individ/**Melding/Undersokelse** finnes<br/>
for hver **Undersokelse** i **Melding/Undersokelse**<br/>
når **Undersokelse/@SluttDato** finnes og **Undersokelse/@Konklusjon** mangler<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersokelse ({**Undersokelse/@Id**}). Avsluttet undersøkelse mangler konklusjon"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse04.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse04Test.kt)

#### <a name="undersokelse_07">Undersøkelse Kontroll 7: Konkludert undersøkelse skal ha vedtaksgrunnlag</a>
Gitt at /Barnevern/Individ/**Melding/Undersokelse** finnes<br/>
for hver **Undersokelse** i **Melding/Undersokelse**<br/>
når **Undersokelse/@Konklusjon** er 1 (Barneverntjenesten fatter vedtak om tiltak) eller 2 (Begjæring om tiltak for fylkesnemnda)<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersokelse ({**Undersokelse/@Id**}). Undersøkelse konkludert med kode  {**Undersokelse/@Konklusjon**} skal ha vedtaksgrunnlag"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse07.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse07Test.kt)

#### <a name="undersokelse_08">Undersøkelse Kontroll 8: Ukonkludert undersøkelse påbegynt før 1. juli er ikke konkludert</a>
Gitt at /Barnevern/Individ/**Melding/Undersokelse** finnes<br/>
for hver **Undersokelse** i **Melding/Undersokelse**<br/>
når **Undersokelse/@StartDato** er før 1. juli i **rapporteringsår** og **Undersokelse/@SluttDato** mangler<br/>
så gi en :no_entry:**FEIL** med meldingen "Undersokelse ({**Undersokelse/@Id**}). Undersøkelsen startet {**Undersokelse/@SluttDato**} og skal konkluderes da den har pågått i mer enn 6 måneder"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse08.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Undersokelse08Test.kt)

#### <a name="vedtaksgrunnlag_02">Vedtaksgrunnlag Kontroll 2: Kontroll av kode og presisering</a>
Gitt at /Barnevern/Individ/**Melding/Undersokelse/Vedtaksgrunnlag** finnes<br/>
for hver **Vedtaksgrunnlag** i **Melding/Undersokelse/Vedtaksgrunnlag**<br/>
når **Vedtaksgrunnlag/@Kode** er enten "18" (Andre forhold ved foreldre/ familien) eller "19" (Andre forhold ved barnets situasjon)<br/>
så gi en :no_entry:**FEIL** med meldingen "Vedtaksgrunnlag med kode ({**Vedtaksgrunnlag/@Kode**}) mangler presisering"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Vedtak02.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Vedtak02Test.kt)


### <a name="plan">Plan</a>

#### <a name="plan_02a">Plan Kontroll 2a: Startdato etter sluttdato</a>
Gitt at /Barnevern/Individ/Plan/**@Id**, <br/>
/Barnevern/Individ/Plan/**@StartDato** og <br/>
/Barnevern/Individ/Plan/**@SluttDato** finnes<br/>
når **@StartDato** er etter **@SluttDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Plan ({**@Id**}). Planens startdato ({**@StartDato**}) er etter planens sluttdato ({**@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Plan02a.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Plan02aTest.kt)

#### <a name="plan_02b">Plan Kontroll 2b: Sluttdato mot rapporteringsår</a>
Gitt at /Barnevern/Individ/Plan/**@SluttDato** finnes<br/>
når året i **@SluttDato** og **rapporteringsår** er forskjellige<br/>
så gi en :no_entry:**FEIL** med meldingen "Plan ({**@Id**}). Planens sluttdato ({**@SluttDato**}) er ikke i rapporteringsåret ({**rapporteringsår**}) "<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Plan02b.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Plan02bTest.kt)

#### <a name="plan_02c">Plan Kontroll 2c: Sluttdato mot individets sluttdato</a>
Gitt at /Barnevern/**Individ/@SluttDato**, <br/>
/Barnevern/Individ/**Plan/@Id**, <br/>
/Barnevern/Individ/**Plan/@SluttDato** finnes<br/>
når **Plan/@SluttDato** er etter **Individ/@SluttDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Plan ({**Plan/@Id**}}). Planens sluttdato ({**Plan/@SluttDato**}) er etter individets sluttdato ({**Individ/@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Plan02c.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Plan02cTest.kt)

#### <a name="plan_02d">Plan Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på plann</a>
Gitt at /Barnevern/Individ/**@Avslutta3112** = "1" (Avsluttet)<br/>
for hver Plan i /Barnevern/Individ/<br/>
når Plan/@SluttDato mangler <br/>
eller /Barnevern/Individ/@SluttDato mangler <br/>
eller Plan/@SluttDato og er etter **telledato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Plan ({**Plan/@Id**}}). Individet er avsluttet hos barnevernet og dets planer skal dermed være avsluttet. Sluttdato er {**Plan/@SluttDato** eller "uoppgitt"}"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Plan02d.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Plan02dTest.kt)

#### <a name="plan_02e">Plan Kontroll 2e: Startdato mot individets startdato</a>
Gitt at /Barnevern/**Individ/@StartDato** og <br/>
/Barnevern/Individ/**Plan/@StartDato** finnes<br/>
når **Plan/@StartDato** er før **Individ/@StartDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Plan ({**Plan/@Id**}). Planens startdato ({**Plan/@StartDato**}) skal være lik eller etter individets startdato ({**Individ/@StartDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Plan02e.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Plan02eTest.kt)


### <a name="tiltak">Tiltak</a>

#### <a name="tiltak_02a">Tiltak Kontroll 2a: Startdato etter sluttdato</a>
Gitt at /Barnevern/Individ/Tiltak/**@Id**, <br/>
/Barnevern/Individ/Tiltak/**@StartDato** og <br/>
/Barnevern/Individ/Tiltak/**@SluttDato** finnes<br/>
når **@StartDato** er etter **@SluttDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**@Id**}). Tiltakets startdato ({**@StartDato**}) er etter tiltakets sluttdato ({**@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak02a.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak02aTest.kt)

#### <a name="tiltak_02b">Tiltak Kontroll 2b: Sluttdato mot rapporteringsår</a>
Gitt at /Barnevern/Individ/Tiltak/**@SluttDato** finnes<br/>
når året i **@SluttDato** og **rapporteringsår** er forskjellige<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**@Id**}). Tiltakets sluttdato ({**@SluttDato**}) er ikke i rapporteringsåret ({**rapporteringsår**}) "<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak02b.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak02bTest.kt)

#### <a name="tiltak_02c">Tiltak Kontroll 2c: Sluttdato mot individets sluttdato</a>
Gitt at /Barnevern/**Individ/@SluttDato**, <br/>
/Barnevern/Individ/**Tiltak/@Id**, <br/>
/Barnevern/Individ/**Tiltak/@SluttDato** finnes<br/>
når **Tiltak/@SluttDato** er etter **Individ/@SluttDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**Tiltak/@Id**}}). Tiltakets sluttdato ({**Tiltak/@SluttDato**}) er etter individets sluttdato ({**Individ/@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak02c.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak02cTest.kt)

#### <a name="tiltak_02d">Tiltak Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på tiltakn</a>
Gitt at /Barnevern/Individ/**@Avslutta3112** = "1" (Avsluttet)<br/>
for hver Tiltak i /Barnevern/Individ/<br/>
når Tiltak/@SluttDato mangler <br/>
eller /Barnevern/Individ/@SluttDato mangler <br/>
eller Tiltak/@SluttDato og er etter **telledato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**Tiltak/@Id**}}). Individet er avsluttet hos barnevernet og dets tiltak skal dermed være avsluttet. Sluttdato er {**Tiltak/@SluttDato** eller "uoppgitt"}"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak02d.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak02dTest.kt)

#### <a name="tiltak_02e">Tiltak Kontroll 2e: Startdato mot individets startdato</a>
Gitt at /Barnevern/**Individ/@StartDato** og <br/>
/Barnevern/Individ/**Tiltak/@StartDato** finnes<br/>
når **Tiltak/@StartDato** er før **Individ/@StartDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**Tiltak/@Id**}). Tiltakets startdato ({**Tiltak/@StartDato**}) skal være lik eller etter individets startdato ({**Individ/@StartDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak02e.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak02eTest.kt)

#### <a name="tiltak_04">Tiltak Kontroll 4: Omsorgstiltak med sluttdato krever årsak til opphevelse</a>
Gitt at /Barnevern/Individ/**Tiltak** finnes<br/>
når **@SluttDato** finnes og **Tiltak** er en [Omsorgstiltak](#omsorgstiltak) og **@Opphevelse** mangler<br/>
så gi en :warning:**ADVARSEL** med meldingen "Tiltak ({**@Id**}}). Omsorgstiltak med sluttdato {**@SluttDato**} krever kode for opphevelse"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak04.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak04Test.kt)

#### <a name="tiltak_05">Tiltak Kontroll 5: Barn over 7 år og i barnehage</a>
Gitt at **alder** i år er utledet fra forskjellen mellom telledato og dato-delen i /Barnevern/Individ/**@Fodselsnummer** og /Barnevern/Individ/**Tiltak** finnes<br/>
når alder er 7 år eller eldre og **Tiltak/Kategori/@Kode** er "4.1"<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**@Id**}}). Barnet er over 7 år og i barnehage. Barnets alder er {**alder**} år"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak05.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak05Test.kt)

#### <a name="tiltak_06">Tiltak Kontroll 6: Barn over 11 år og i SFO</a>
Gitt at **alder** i år er utledet fra forskjellen mellom telledato og dato-delen i /Barnevern/Individ/**@Fodselsnummer** og /Barnevern/Individ/**Tiltak** finnes<br/>
når alder er 11 år eller eldre og **Tiltak/Kategori/@Kode** er "4.2"<br/>
så gi en :warning:**ADVARSEL** med meldingen "Tiltak ({**@Id**}}). Barnet er over 11 år og i SFO. Barnets alder er {**alder**} år"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak06.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak06Test.kt)

#### <a name="tiltak_07">Tiltak Kontroll 7: Kontroll av manglende presisering for tiltakskategori</a>
Gitt at /Barnevern/Individ/**Tiltak** finnes<br/>
når **Tiltak/Kategori/@Kode** er en av "1.99", "2.99", "3.7", "3.99", "4.99", "5.99", "6.99", "7.99", "8.99"<br/>
og **Tiltak/Kategori/@Presisering** mangler
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**@Id**}}). Tiltakskategori {**Tiltak/Kategori/@Kode**} mangler presisering"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak07.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak07Test.kt)

#### <a name="tiltak_08">Tiltak Kontroll 8: Kontroll av kode og presisering for opphevelse</a>
Gitt at /Barnevern/Individ/**Tiltak** finnes<br/>
når **Tiltak/Opphevelse/@Kode** er "4" (Annet) og **Tiltak/Opphevelse/@Presisering** mangler<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**@Id**}}). Tiltaksopphevelse {**Tiltak/Opphevelse/@Kode**} mangler presisering"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak08.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak08Test.kt)

#### <a name="tiltak_09">Tiltak Kontroll 9: Flere plasseringstiltak i samme periode</a>
Gitt at /Barnevern/Individ/**Tiltak** finnes, og 2 eller flere **Tiltak** er [Plasseringstiltak](#plasseringstiltak)<br/>
når 2 **Plasseringstiltak** overlapper med mer enn 3 måneder<br/>
så gi en :warning:**ADVARSEL** med meldingen "Plasseringstiltak kan ikke overlappe med mer enn 3 måneder"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak09.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak09Test.kt)

#### <a name="tiltak_24">Tiltak Kontroll 24: Lovhjemmel refererer til feil barnevernlov</a>
Gitt at **terskeldato** er 31. desember 2022 og /Barnevern/Individ/**Tiltak** finnes<br/>

når **Tiltak/@StartDato** er lik eller før **terskeldato**<br/>
og (**Tiltak/Lovhjemmel/@Lov** er ulik "BVL" eller **Tiltak/JmfrLovhjemmel/@Lov** er ulik "BVL")<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**@Id**}}). Tiltak opprettet før 01.01.2023 krever lov = 'BVL'"<br/>

når **Tiltak/@StartDato** er etter **terskeldato**<br/>
og (**Tiltak/Lovhjemmel/@Lov** er ingen av "BVL", "BVL2021" eller **Tiltak/JmfrLovhjemmel/@Lov** er ingen av "BVL", "BVL2021")<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**@Id**}}). Tiltak opprettet 01.01.2023 eller etter, krever lov = 'BVL' eller 'BVL2021'"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak24.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Tiltak24Test.kt)

#### <a name="lovhjemmel_03">Lovhjemmel Kontroll 3: Individet er over 18 år og har omsorgstiltak</a>
Gitt at **alder** i år er utledet fra forskjellen mellom telledato og dato-delen i /Barnevern/Individ/**@Fodselsnummer** og /Barnevern/Individ/**Tiltak** finnes<br/>
når alder er 18 år eller eldre og **Tiltak** er en [Omsorgstiltak](#omsorgstiltak)<br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**@Id**}}). Individet er {**alder**} år år og skal dermed ikke ha omsorgstiltak"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Lovhjemmel03.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Lovhjemmel03Test.kt)

#### <a name="lovhjemmel_04">Lovhjemmel Kontroll 4: Lovhjemmel</a>
Gitt at /Barnevern/Individ/**Tiltak** finnes<br/>
for alle **@Kapittel** eller **@Paragraf** i **Tiltak//@Paragraf**
når **@Kapittel** er "0" eller **@Paragraf** er "0" <br/>
så gi en :no_entry:**FEIL** med meldingen "Tiltak ({**@Id**}}). Kapittel ({**@Kapittel**}) eller paragraf ({**@Paragraf**}) er rapportert med den ugyldige koden '0'"


### <a name="flytting">Flytting</a>

#### <a name="flytting_02c">Flytting Kontroll 2c: Sluttdato mot individets sluttdato</a>
Gitt at /Barnevern/**Individ/@SluttDato**, <br/>
/Barnevern/Individ/**Flytting/@Id**, <br/>
/Barnevern/Individ/**Flytting/@SluttDato** finnes<br/>
når **Flytting/@SluttDato** er etter **Individ/@SluttDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Flytting ({**Flytting/@Id**}}). Flyttingens sluttdato ({**Flytting/@SluttDato**}) er etter individets sluttdato ({**Individ/@SluttDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Flytting02c.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Flytting02cTest.kt)

#### <a name="flytting_02f">Flytting Kontroll 2f: Sluttdato mot individets startdato</a>
Gitt at /Barnevern/**Individ/@StartDato**, <br/>
/Barnevern/Individ/**Flytting/@Id**, <br/>
/Barnevern/Individ/**Flytting/@SluttDato** finnes<br/>
når **Flytting/@SluttDato** er før **Individ/@StartDato**<br/>
så gi en :no_entry:**FEIL** med meldingen "Flytting ({**Flytting/@Id**}}). Flyttingens sluttdato ({**Flytting/@SluttDato**}) er før individets startdato ({**Individ/@StartDato**})"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Flytting02f.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Flytting02fTest.kt)
