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

