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


