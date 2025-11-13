# Kravspesifikasjon Kvalifiseringsstønad, skjema 11CF

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

Filbeskrivelse finnes som en mennesklig- og maskinlesbar
yaml-fil, [lenke til filbeskrivelse](/kontroller/src/main/resources/file_description_11CF_2025.yaml).

## Krav

### Kontroll 000: Skal levere filuttrekk

Definert i [Kravspesifikasjon for alle posisjonsbaserte filvedlegg]( specs_posisjonsbaserte_filvedlegg_felles.md#kontroll-000--skal-levere-filuttrekk)

### Kontroll 001: Recordlengde

Definert i [Kravspesifikasjon for alle posisjonsbaserte filvedlegg]( specs_posisjonsbaserte_filvedlegg_felles.md#kontroll-001--recordlengde)

### Kontroll 003: Kommunenummer

Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-003--kommunenummer)

### Kontroll 003: Bydelsnummer

Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-003--bydelsnummer)

### Kontroll 003: Kommunenummer

Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-003--kommunenummer)

### Kontroll 004 : Oppgaveår

Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-004--oppgaveår)

### Kontroll 004A : Fødselsdato

Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-004a--fødselsdato)

### Kontroll 005 : Fødselsdato
Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-005--fødselsdato)

### Kontroll 05A : Fødselsnummer, dubletter

Definisjon:
OSLO = "0301"

**Gitt at** en har en filbeskrivelse med feltdefinisjon for KOMMUNE_NR, PERSON_FODSELSNR, PERSON_JOURNALNR og STATUS som har en kodeliste med gyldige koder, en datafil med verdi for KOMMUNE_NR, PERSON_FODSELSNR og STATUS i alle rader<br/>
**når** {KOMMUNE_NR} er ulik {OSLO} og {PERSON_FODSELSNR} er matematisk gyldig, gruppér så på {PERSON_FODSELSNR} + {STATUS} og finn alle PERSON_JOURNALNR der antall er større enn 1 og legge de inn i PERSON_JOURNALNR_LISTE<br/>
**så** gi en :no_entry:**FEIL** med meldingen **"Fødselsnummeret i journalnummer {PERSON_JOURNALNR} fins også i journalene {PERSON_JOURNALNR_LISTE}."** for hver rad hvor dette inntreffer

[Kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/kvalifisering/rule/Rule005aFoedselsnummerDubletter.kt)
[Test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/kvalifisering/rule/Rule005aFoedselsnummerDubletterTest.kt)



### Kontroll 008 : Kjønn
Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-008--kjønn)

### Kontroll 009 : Sivilstand
Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-009--sivilstand)

### Kontroll 010 : Har barn under 18 år
Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-010--har-barn-under-18-år)

### Kontroll 011 : Det bor barn under 18 år i husholdningen. Mangler antall barn.
Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-011--det-bor-barn-under-18-år-i-husholdningen-mangler-antall-barn)

### Kontroll 012 : Det bor barn under 18 år i husholdningen. Feil i antall barn.
Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-012---det-bor-barn-under-18-år-i-husholdningen-feil-i-antall-barn)

### Kontroll 013 : Mange barn under 18 år i husholdningen.
Definert i [Kravspesifikasjon felles for Sosialtjenesten]( specs_sosialtjenesten_felles_2025.md#kontroll-013--mange-barn-under-18-år-i-husholdningen)




