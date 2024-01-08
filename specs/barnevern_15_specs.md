# Kravspesifikasjon til kontrollene for KOSTRA-Barnevern

## Innhold

## <a name="omfang">Omfang</a>
Gjelder filuttrekk: Barnevern 2018

Gjelder rapporteringsår: 2018

Kontaktperson: Tone Dyrhaug, tlf. 21 09 47 71, e-post: tone.dyrhaug@ssb.no

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

### <a name="barnevernloven">Barnevernloven</a>
Barnevernloven brukes i to forskjellige versjoner:
- Versjonen fra 1992 refereres som **<a name="bvl">BVL</a>** av historiske årsaker, eventuelt **<a name="bvl1992">BVL1992</a>** i dokumentasjonssammenheng, og gjelder til 31. desember 2022.<br/>
- Versjonen fra 2021 refereres som **<a name="bvl2021">BVL2021</a>** og gjelder fra 1. januar 2023.


### <a name="innrapporteringskanaler">Innrapporteringskanaler</a>
Rapportering til SSB skjer i 2 forskjellige kanaler:
- **<a name="fagsystem_kanal">FAGSYSTEM</a>**, direkte rapportering fra fagsystem via Fiks til SSB.
- **<a name="kostra_kanal">KOSTRA</a>**, klassisk og indirekte rapportering ved hjelp av filuttrekk.


### <a name="omsorgsovertakelse">Omsorgsovertakelse</a>
Et Tiltak er en Omsorgsovertakelse dersom en av følgende:
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
I forbindelse med kontrollering så blir noen varibler sendt med fra portalen og blir benyttet i noen av kontrollene. Disse er:
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
så gi en **FEIL** med meldingen "Filen inneholder feil rapporteringsår {@Versjon}, forventet {rapporteringsåret}."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver02.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver02Test.kt)

#### <a name="avgiver_03">Avgiver Kontroll 3: Organisasjonnummer</a>

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Organisasjonnummer**<br/>
når @Organisasjonnummer er blank<br/>
så gi en **FEIL** med meldingen "Filen mangler organisasjonsnummer. Oppgitt organisasjonsnummer er '**{organisasjonsnummer}**'"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver03.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver03Test.kt)

#### <a name="avgiver_04">Avgiver Kontroll 4: Kommunenummer</a>

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Kommunenummer** og [**kommunenummer**](#kommunenummer) fra skjema 15F. Barnevern YYYY - filuttrekk er like <br/>
når **@Kommunenummer** og **kommunenummer** er forskjellige<br/>
så gi en **FEIL** med meldingen "Filen inneholder feil kommunenummer. Forskjellig kommunenummer i skjema (**{kommunenummer}**) og filuttrekk (**{@Kommunenummer}**)."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver04.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver04Test.kt)

#### <a name="avgiver_06">Avgiver Kontroll 6: Kommunenavn</a>

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Kommunenavn** finnes<br/>
når **@Kommunenavn** er blank<br/>
så gi en **FEIL** med meldingen "Filen mangler kommunenavn."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver06.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/avgiverrule/Avgiver06Test.kt)


### Individ

#### <a name="individ_01a">Individ Kontroll 01a: Validering av datoer</a>

Gitt at **minimumsdato** er 1. januar 1998 og **maksimumsdato** er 31. desember 2049<br/>
når /Barnevern/Individ/**@StartDato** er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Individ startdato: Dato (**{@StartDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/**@SluttDato** finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Individ sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Melding/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Melding startdato: Dato (**{@StartDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Melding/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Melding sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Undersokelse/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Undersøkelse startdato: Dato (**{@StartDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Undersokelse/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Undersøkelse sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Plan/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Plan startdato: Dato (**{@StartDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Plan/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Plan sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Tiltak/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Tiltak startdato: Dato (**{@StartDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Tiltak/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Tiltak sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Flytting/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi en **FEIL** med meldingen "Flytting sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ01a.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ01aTest.kt)

#### <a name="individ_02a">Individ Kontroll 02a: Startdato etter sluttdato</a>
Gitt at /Barnevern/Individ/**@StartDato** og /Barnevern/Individ/**@SluttDato** finnes<br/>
når @StartDato er etter @SluttDato<br/>
så gi en **FEIL** med meldingen "Individets startdato (**{@StartDato}**) er etter sluttdato (**{@SluttDato}**)"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ02a.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ02aTest.kt)

#### <a name="individ_02b">Individ Kontroll 02b: Sluttdato mot versjon</a>
Gitt at /Barnevern/Individ/**@SluttDato** finnes<br/>
når @SluttDato er før **forrige_telledato**<br/>
så gi en **FEIL** med meldingen "Individets sluttdato (**{@SluttDato}**) er før forrige telletidspunkt (**{forrige_telledato}**)"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ02b.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ02bTest.kt)

#### <a name="individ_02d">Individ Kontroll 02d: Avslutta 31.12 medfører at sluttdato skal være satt</a>
Gitt at /Barnevern/Individ/**@Avslutta3112** finnes<br/>
når **@Avslutta3112** = 1 (Ja) og @SluttDato mangler<br/>
så gi en **FEIL** med meldingen "Individet er avsluttet hos barnevernet og skal dermed være avsluttet. Sluttdato er **{@SluttDato}**. Kode for avsluttet er '1'."<br/>
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

så gi en **FEIL** med meldingen "Feil i fødselsnummer. Kan ikke identifisere individet."

Gitt at /Barnevern/Individ/**@Fodselsnummer** mangler og /Barnevern/Individ/**@DUFnummer** finnes <br/>
når modulo11-sjekk av **@DUFnummer** feiler<br/>
så gi en **FEIL** med meldingen "DUF-nummer mangler. Kan ikke identifisere individet."

Gitt at /Barnevern/Individ/**@Fodselsnummer** mangler og /Barnevern/Individ/**@DUFnummer** mangler <br/>
så gi en **FEIL** med meldingen "Fødselsnummer og DUF-nummer mangler. Kan ikke identifisere individet."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ03.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ03Test.kt)

#### <a name="individ_04">Individ Kontroll 04: Dublett på fødselsnummer</a>
Gitt Individ Kontroll 07: Klient over 25 år avsluttes finnes og oppfyller én av følgende definisjoner:<br/>
* Fødselsnummer, beskrevet på  [https://www.udi.no/ord-og-begreper/fodselsnummer/](https://www.udi.no/ord-og-begreper/fodselsnummer/),
* D-nummer, beskrevet på [https://www.udi.no/ord-og-begreper/d-nummer/](https://www.udi.no/ord-og-begreper/d-nummer/),

når **@Fodselsnummer** forekommer flere ganger<br/>
så gi en **FEIL** med meldingen "Fødselsnummeret i journalnummer {**@Journalnummer**} fins også i journalene {journalnummerliste}"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/BarnevernValidator.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/BarnevernValidatorTest.kt)

#### <a name="individ_05">Individ Kontroll 05: Dublett på journalnummer</a>
Gitt /Barnevern/Individ/**@Journalnummer** finnes<br/>
når **@Journalnummer** forekommer flere ganger<br/>
så gi en **FEIL** med meldingen "Journalnummer {**@Journalnummer**} forekommer {antall} ganger."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/BarnevernValidator.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/BarnevernValidatorTest.kt)

#### <a name="individ_06">Individ Kontroll 06: Har meldinger, planer eller tiltak</a>
Gitt at /Barnevern/Individ finnes<br/>
når ingen /Barnevern/Individ/Melding finnes og ingen /Barnevern/Individ/Plan finnes og ingen /Barnevern/Individ/Tiltak finnes
så gi en **FEIL** med meldingen "Individet har ingen meldinger, planer eller tiltak i løpet av året"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ06.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ06Test.kt)

#### <a name="individ_07">Individ Kontroll 07: Klient over 25 år avsluttes</a>
Gitt at **alder** i år er utledet fra forskjellen mellom telledato og dato-delen i /Barnevern/Individ/**@Fodselsnummer**<br/>
når alder er større enn 25 år<br/>
så gi en **FEIL** med meldingen "Individet er **{alder}** år og skal avsluttes som klient"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ07.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ07Test.kt)

#### <a name="individ_08">Individ Kontroll 08: Alder i forhold til tiltak</a>
Gitt at **alder** i år er utledet fra forskjellen mellom telledato og dato-delen i /Barnevern/Individ/**@Fodselsnummer**<br/>
når alder er større enn 17 år og /Barnevern/Individ/**Tiltak** mangler<br/>
så gi en **FEIL** med meldingen "Individet er over 18 år og skal dermed ha tiltak"<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ08.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ08Test.kt)

#### <a name="individ_09">Individ Kontroll 09: Bydelsnummer</a>
Gitt at **kommunenummer** starter med 0301 og /Barnevern/Individ/**@Bydelsnummer** mangler
så gi en **FEIL** med meldingen "Filen mangler bydelsnummer."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ09.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ09Test.kt)

#### <a name="individ_10">Individ Kontroll 10: Bydelsnavn</a>
Gitt at **kommunenummer** starter med 0301 og /Barnevern/Individ/**@Bydelsnavn** mangler
så gi en **FEIL** med meldingen "Filen mangler bydelsnavn."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ10.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ10Test.kt)

#### <a name="individ_11">Individ Kontroll 11: Fødselsnummer</a>
Gitt at /Barnevern/Individ/**@Fodselsnummer** mangler eller er blank, eller at /Barnevern/Individ/**@Fodselsnummer** er et ugyldig fødselsnummer<br/>
så gi en **FEIL** med meldingen "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ11.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ11Test.kt)

#### <a name="individ_12">Individ Kontroll 12: Fødselsnummer</a>
Gitt at /Barnevern/Individ/**@Fodselsnummer** mangler, eller at /Barnevern/Individ/**@Fodselsnummer** er et ugyldig fødselsnummer<br/>
så gi en **FEIL** med meldingen "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ12.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ12Test.kt)

#### <a name="individ_19">Individ Kontroll 19: DUF-nummer</a>
Gitt at /Barnevern/Individ/**@DUFnummer** finnes<br/>
når **@DUFnummer** er ugyldig<br/>
så gi en **FEIL** med meldingen "Individet har ufullstendig DUF-nummer. Korriger DUF-nummer."<br/>
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ19.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/barnevern/individrule/Individ19Test.kt)

