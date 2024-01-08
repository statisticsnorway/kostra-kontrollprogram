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
* <a name="telledato">**_telledato**</a> som er 31. desember i **rapporteringsår**, for eksempel **31. desember 2023**
* <a name="forrigetelledato">**forrige_telledato**</a> som er 31. desember i året før **rapporteringsår**, for eksempel **31. desember 2022**

### <a name="avgiver">Avgiver</a>

#### <a name="avgiver_02">Avgiver Kontroll 2: Årgang</a>
Alvorlighetsgrad: FEIL

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Versjon** og [**rapporteringsår**](#rapporteringsar) fra skjema 15F. Barnevern YYYY - filuttrekk er like <br/>
når @Versjon og rapporteringsår er forskjellige<br/>
så gi feilmeldingen "Filen inneholder feil rapporteringsår {@Versjon}, forventet {rapporteringsåret}."

#### <a name="avgiver_03">Avgiver Kontroll 3: Organisasjonnummer</a>
Alvorlighetsgrad: FEIL

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Organisasjonnummer**<br/>
når @Organisasjonnummer er blank<br/>
så gi feilmeldingen "Filen mangler organisasjonsnummer. Oppgitt organisasjonsnummer er '**{organisasjonsnummer}**'"

#### <a name="avgiver_04">Avgiver Kontroll 4: Kommunenummer</a>
Alvorlighetsgrad: FEIL

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Kommunenummer** og [**kommunenummer**](#kommunenummer) fra skjema 15F. Barnevern YYYY - filuttrekk er like <br/>
når **@Kommunenummer** og **kommunenummer** er forskjellige<br/>
så gi feilmeldingen "Filen inneholder feil kommunenummer. Forskjellig kommunenummer i skjema (**{kommunenummer}**) og filuttrekk (**{@Kommunenummer}**)."

#### <a name="avgiver_06">Avgiver Kontroll 6: Kommunenavn</a>
Alvorlighetsgrad: FEIL

Gitt at en skal kontrollere at /Barnevern/Avgiver/**@Kommunenavn** finnes<br/>
når **@Kommunenavn** er blank<br/>
så gi feilmeldingen "Filen mangler kommunenavn."


### Individ

#### <a name="individ_01a">Individ Kontroll 01a: Validering av datoer</a>
Alvorlighetsgrad: FEIL

Gitt at **minimumsdato** er 1. januar 1998 og **maksimumsdato** er 31. desember 2049<br/>
når /Barnevern/Individ/**@StartDato** er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Individ startdato: Dato (**{@StartDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/**@SluttDato** finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Individ sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Melding/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Melding startdato: Dato (**{@StartDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Melding/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Melding sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Undersokelse/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Undersøkelse startdato: Dato (**{@StartDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Undersokelse/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Undersøkelse sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Plan/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Plan startdato: Dato (**{@StartDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Plan/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Plan sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Tiltak/@StartDato er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Tiltak startdato: Dato (**{@StartDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Tiltak/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Tiltak sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

når /Barnevern/Individ/Flytting/@SluttDato finnes og er før **minimumsdato** eller etter **maksimumsdato**<br/>
så gi feilmeldingen "Flytting sluttDato: Dato (**{@SluttDato}**) må være mellom **{minimumsdato}** og **{maksimumsdato}**)"

#### <a name="individ_02a">Individ Kontroll 02a: Startdato etter sluttdato</a>
Alvorlighetsgrad: FEIL

Gitt at /Barnevern/Individ/**@StartDato** og /Barnevern/Individ/**@SluttDato** finnes<br/>
når @StartDato er etter @SluttDato<br/>
så gi feilmeldingen "Individets startdato (**{@StartDato}**) er etter sluttdato (**{@SluttDato}**)"

#### <a name="individ_02b">Individ Kontroll 02b: Sluttdato mot versjon</a>
Alvorlighetsgrad: FEIL

Gitt at /Barnevern/Individ/**@SluttDato** finnes<br/>
når @SluttDato er før **forrige_telledato**<br/>
så gi feilmeldingen "Individets sluttdato (**{@SluttDato}**) er før forrige telletidspunkt (**{forrige_telledato}**)"

#### <a name="individ_02d">Individ Kontroll 02d: Avslutta 31.12 medfører at sluttdato skal være satt</a>
Alvorlighetsgrad: FEIL

Gitt at /Barnevern/Individ/**@Avslutta3112** finnes= 1 (Ja)<br/>
når **@Avslutta3112** = 1 (Ja) og @SluttDato mangler<br/>
så gi feilmeldingen "Individet er avsluttet hos barnevernet og skal dermed være avsluttet. Sluttdato er **{@SluttDato}**. Kode for avsluttet er '1'."


