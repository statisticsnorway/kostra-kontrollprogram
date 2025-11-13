# Kravspesifikasjon for posisjonsbaserte filvedlegg

## Begrep

### <a name="alvorligehetsgrader">Alvorlighetsgrader</a>
* :skull: **FATAL**, når en kontroll ender med alvorlighetsgraden FATAL så stopper all videre kontrollering umiddelbart. FATAL hindrer da andre kontrollene i bli kjørt. Videre så hindrer denne innsending av filvedlegget til SSB.
* :no_entry: **ERROR**, når en kontroll ender med alvorlighetsgraden ERROR så hindrer denne innsending av filvedlegget til SSB.
* :warning: **WARNING**, når en kontroll ender med alvorlighetsgraden WARNING så kan filvedlegget bli sendt inn til SSB med advarsler.
* :information_source: **INFO**, når en kontroll ender med alvorlighetsgraden INFO så kan filvedlegget bli sendt inn til SSB med tilbakemeldinger som er til informasjon for avgiveren.
* :white_check_mark:**OK**, når en kontroll ender med alvorlighetsgraden vises ingen tilbakemelding av noe slag da alt er i orden.

### <a name="variabler">Variabler fra skjema</a>
I forbindelse med kontrollering så blir noen variabler sendt med fra portalen og blir benyttet i noen av kontrollene. Disse er:
* <a name="skjema">**skjema**</a>, for eksempel **0A**, **11F** og **55F**
* <a name="rapporteringsar">**rapporteringsår**</a>, for eksempel **2024**
* <a name="kommunenummer">**kommunenummer**</a>, for eksempel **0301**
* <a name="kommunenavn">**kommunenavn**</a>, for eksempel **Oslo**
* <a name="organisasjonsnummer">**organisasjonsnummer**</a>, for eksempel **958935420**
* <a name="foretaksnummer">**foretakssjonsnummer**</a>, for eksempel **958935420**
* <a name="skal_rapportere">**skal rapportere**</a>, for eksempel **Ja / Nei**

## Krav

<a id="000"></a>
### Kontroll 000 : Skal levere filuttrekk

**Gitt at** en i Kostra-skjemaet har merket av for at man skal rapportere<br/>
**når** filvedlegget med data finnes<br/>
**så** skal ingenting skje

**når** filvedlegget mangler<br/>
**så** gi en :skull:**FATAL** med meldingen "Det er krysset av i skjemaet at det finnes deltakere, men fil som kun inneholder et mellomrom er levert."

**Gitt at** en har i Kostra-skjemaet merket av for at man **ikke** skal rapportere<br/>
**når** filvedlegget inneholder noe annet enn kun et mellomrom<br/>
**så** gi en :skull:**FATAL** med meldingen "Det er krysset av i skjemaet at det ikke finnes deltakere, men filen som er levert har annet innhold enn ett mellomrom.<br/>
Kryptert fil uten innhold kan lastes ned fra https://www.ssb.no/innrapportering/kostra-innrapportering<br/>
-> Kontrollprogram og programmer til fagsystem for kommuner og leverandører<br/>
-> Kvalifiseringsstønad<br/>
-> Tom, kryptert fil (for dem som ikke har noen mottakere av kvalifiseringsstønad i **{rapporteringsår}**)"

**når** filvedlegget inneholder kun et mellomrom<br/>
**så** gi en :white_check_mark:**OK** med meldingen "Det er krysset av i skjemaet at det ikke finnes deltakere og fil som kun inneholder et mellomrom er levert."

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/Rule000HasAttachment.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/Rule000HasAttachmentTest.kt)

<a id="001"></a>
### Kontroll 001 : Recordlengde

**Gitt at** en har en filbeskrivelse og et filvedlegg med data<br/>
**når** en eller flere linjer i filvedlegget har en annen lengde enn beskrevet i filbeskrivelsen eller inneholder andre blanke tegn enn mellomrom<br/>
**så** gi en :skull:**FATAL** med meldingen "Korrigér filen slik at alle records er på (filbeskrivelse.lengde) tegn, mellomrom brukes for alle blanke posisjoner og avslutter med linjeskift. Denne fatale feilen hindrer de andre kontrollene i å bli kjørt. Gjelder for linjene: (liste med linjenummer)"

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/Rule001RecordLength.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/Rule001RecordLengthTest.kt)


<a id="002"></a>
### Kontroll 002 : Filbeskrivelse, mangler obligatorisk verdi

**Gitt at** en har en filbeskrivelse med feltdefinisjon som sier at feltet er obligatorisk å fylle ut<br/>
**når** feltet er blankt / mangler<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Korrigér felt '{feltdefinisjon.navn}', posisjon fra og med {feltdefinisjon.fra} til og med {feltdefinisjon.til}, mangler obligatorisk verdi."

### Kontroll 002 : Filbeskrivelse, feil i heltall-felt

**Gitt at** en har en filbeskrivelse med feltdefinisjon som sier at feltet er et tall<br/>
**når** feltet er fyllt ut med noe annet enn et heltall<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Korrigér felt '{fieldDefinition.navn}', posisjon fra og med ${feltdefinisjon.fra} til og med ${feltdefinisjon.til}, er et tallfelt, men inneholder '{record[feltdefinisjon.navn]}'."

### Kontroll 002 : Filbeskrivelse, feil i dato-felt

**Gitt at** en har en filbeskrivelse med feltdefinisjon som sier at feltet er et dato der datoformat er definert<br/>
**når** feltet er fyllt ut med noe annet enn enn formattert dato<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Korrigér felt '{fieldDefinition.navn}', posisjon fra og med ${feltdefinisjon.fra} til og med ${feltdefinisjon.til}, er et datofelt med datomønster '{feltdefinisjon.datomønster}', men inneholder '{record[feltdefinisjon.navn]}'."


### Kontroll 002 : Filbeskrivelse, feil kode

**Gitt at** en har en filbeskrivelse med feltdefinisjon som sier at feltet er en tekst der kodeliste er definert<br/>
**når** feltet er fyllt ut med noe annet enn en kode fra kodelisten<br/>
**så** gi en :no_entry:**FEIL** med meldingen "Korrigér felt '{fieldDefinition.navn}', posisjon fra og med ${feltdefinisjon.fra} til og med ${feltdefinisjon.til}, sin kode '{record[feltdefinisjon.navn]}' fins ikke i {feltdefinisjon.kodeliste}."

