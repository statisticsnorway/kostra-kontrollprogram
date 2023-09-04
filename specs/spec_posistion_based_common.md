# Kravpesisfikasjon for posisjonsbaserte filvedlegg

## Begrep
### Alvorligehetsgrader
* FATAL, når en kontroll ender med alvorlighetsgraden FATAL så stopper all videre kontrollering umiddelbart. FATAL hindrer da andre kontrollene i bli kjørt. Videre så hindrer denne innsending av filvedlegget til SSB.
* ERROR, når en kontroll ender med alvorlighetsgraden ERROR så hindrer denne innsending av filvedlegget til SSB.
* WARNING, når en kontroll ender med alvorlighetsgraden WARNING så kan filvedlegget bli sendt inn til SSB med advarsler.
* INFO, når en kontroll ender med alvorlighetsgraden INFO så kan filvedlegget bli sendt inn til SSB med tilbakemeldinger som er til informasjon for avgiveren.
* OK, når en kontroll ender med alvorlighetsgraden vises ingentilbakemelding av noe slag da alt er i orden.

## Krav

<details>
<summary>Kontroll 001 : Recordlengde</summary>

### Beskrivelse
**Gitt at** en har en filbeskrivelse og et filvedlegg med data<br/>
**når** en eller flere linjer i filvedlegget har en annen lengde enn beskrevet i filbeskrivelsen eller inneholder andre blanke tegn enn mellomrom<br/>
**så** gi feilmelding 'Korrigér filen slik at alle records er på (filbeskrivelse.lengde) tegn, mellomrom brukes for alle blanke posisjoner og avslutter med linjeskift. Denne feilen hindrer de andre kontrollene i å bli kjørt. Gjelder for linjene: (liste med linjenummer)'

#### Alvorlighetsgrad: FATAL

#### [Kildekode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/Rule001RecordLength.kt)
#### [Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/Rule001RecordLengthTest.kt)
</details>

