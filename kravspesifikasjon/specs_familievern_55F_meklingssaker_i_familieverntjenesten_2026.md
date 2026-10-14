# Kravspesifikasjon Meklingssaker i familieverntjenesten, skjema 55F, 2026

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

Filbeskrivelse finnes som [markdown](file_description_55F_2026.md) og [yaml](file_description_55F_2026.yaml)
.
Endringslogg for filbeskrivelse finnes som [markdown](changelog_for_55F_from_2025_to_2026.md).

## Krav

### Kontroll 001: Recordlengde
Se krav i [kravspesifikasjon/felles_posisjonsbaserte_filvedlegg.md](felles_posisjonsbaserte_filvedlegg.md#kontroll-001--recordlengde)


### Kontroll 002: Filbeskrivelse
Se krav i [kravspesifikasjon/felles_posisjonsbaserte_filvedlegg.md](felles_posisjonsbaserte_filvedlegg.md#kontroll-002--filbeskrivelse-mangler-obligatorisk-verdi)



### Kontroll 003: Fylkesnummer
**Gitt** gyldige fylker i [koblingshierarkiet for familievern](mapping_familievern_region_fylke_kontor_2026.md)<br/>
**når** feltet **FYLKE_NR** inneholder ingen av de gyldige fylkene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Fylkesnummeret som er oppgitt i recorden fins ikke i listen med gyldige fylkesnumre. Fant '{FYLKE_NR}', forventet én av : {GYLDIGE_FYLKER}. Feltet er obligatorisk og må fylles ut."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule003Fylkesnummer.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule003FylkesnummerTest.kt)


### Kontroll 005 : Avsluttede meklinger etter tidsbruk, (sumfelt)

| MEKLING  | 1                 | 2                 | 3                 | TOT                  |
|----------|-------------------|-------------------|-------------------|----------------------|
| **SEP**  | MEKLING_SEP_1     | MEKLING_SEP_2     | MEKLING_SEP_3     | **MEKLING_SEP_TOT**  |
| **SAM**  | MEKLING_SAM_1     | MEKLING_SAM_2     | MEKLING_SAM_3     | **MEKLING_SAM_TOT**  |
| **SAK**  | MEKLING_SAK_1     | MEKLING_SAK_2     | MEKLING_SAK_3     | **MEKLING_SAK_TOT**  |
| **TILB** | MEKLING_TILB_1    | MEKLING_TILB_2    | MEKLING_TILB_3    | **MEKLING_TILB_TOT** |
| **FLY**  | MEKLING_FLY_1     | MEKLING_FLY_2     | MEKLING_FLY_3     | **MEKLING_FLY_TOT**  |
| **TOT**  | **MEKLING_TOT_1** | **MEKLING_TOT_2** | **MEKLING_TOT_3** | **MEKLING_TOT_ALLE** |

**For** hver rad og kolonne<br/>
**gitt** feltet for sum, her kalt **SUM**,<br/>
og feltene på raden eller kolonnen, her kalt **LISTE** er fylt ut med tall, <br/>
og summen av **LISTE**, her kalt **LISTESUM**<br/>
**når** **LISTESUM** er forskjellig fra **SUM**<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Summen (**SUM**) med verdi ({**SUM**}) er ulik summen ({**LISTESUM**}) av følgende liste ({**LISTE**})"

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule005AvsluttedeMeklingerEtterTidsbruk.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule005AvsluttedeMeklingerEtterTidsbrukTest.kt)


### Kontroll 006 : Avsluttede meklinger etter deltakere, (sumfelt)

|          | BEGGE         | EN         | TOT             |
|----------|---------------|------------|-----------------|
| **SEP**  | SEP_BEGGE     | SEP_EN     | **SEP_TOT**     |
| **SAM**  | SAM_BEGGE     | SAM_EN     | **SAM_TOT**     |
| **SAK**  | SAK_BEGGE     | SAK_EN     | **SAK_TOT**     |
| **TILB** | TILB_BEGGE    | TILB_EN    | **TILB_TOT**    |
| **FLY**  | FLY_BEGGE     | FLY_EN     | **FLY_TOT**     |
| **TOT**  | **BEGGE_TOT** | **EN_TOT** | **ENBEGGE_TOT** |

**For** hver rad og kolonne<br/>
**gitt** feltet for sum, her kalt **SUM**,<br/>
og feltene på raden eller kolonnen, her kalt **LISTE** er fylt ut med tall, <br/>
og summen av **LISTE**, her kalt **LISTESUM**<br/>
**når** **LISTESUM** er forskjellig fra **SUM**<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Summen (**SUM**) med verdi ({**SUM**}) er ulik summen ({**LISTESUM**}) av følgende liste ({**LISTE**})"

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule006AvsluttedeMeklingerEtterDeltakere.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule006AvsluttedeMeklingerEtterDeltakereTest.kt)


### Kontroll 007 : Avsluttede meklinger etter tidsbruk, (sumfelt)

| VENTETID | 1                  | 2                  | 3                  | 4                  | TOT                   |
|----------|--------------------|--------------------|--------------------|--------------------|-----------------------|
| **SEP**  | VENTETID_SEP_1     | VENTETID_SEP_2     | VENTETID_SEP_3     | VENTETID_SEP_4     | **VENTETID_SEP_TOT**  |
| **SAM**  | VENTETID_SAM_1     | VENTETID_SAM_2     | VENTETID_SAM_3     | VENTETID_SAM_4     | **VENTETID_SAM_TOT**  |
| **SAK**  | VENTETID_SAK_1     | VENTETID_SAK_2     | VENTETID_SAK_3     | VENTETID_SAK_4     | **VENTETID_SAK_TOT**  |
| **TILB** | VENTETID_TILB_1    | VENTETID_TILB_2    | VENTETID_TILB_3    | VENTETID_TILB_4    | **VENTETID_TILB_TOT** |
| **FLY**  | VENTETID_FLY_1     | VENTETID_FLY_2     | VENTETID_FLY_3     | VENTETID_FLY_4     | **VENTETID_FLY_TOT**  |
| **TOT**  | **VENTETID_TOT_1** | **VENTETID_TOT_2** | **VENTETID_TOT_3** | **VENTETID_TOT_4** | **VENTETID_TOT_TOT**  |

**For** hver rad og kolonne<br/>
**gitt** feltet for sum, her kalt **SUM**,<br/>
og feltene på raden eller kolonnen, her kalt **LISTE** er fylt ut med tall, <br/>
og summen av **LISTE**, her kalt **LISTESUM**<br/>
**når** **LISTESUM** er forskjellig fra **SUM**<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Summen (**SUM**) med verdi ({**SUM**}) er ulik summen ({**LISTESUM**}) av følgende liste ({**LISTE**})"

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule007AvsluttedeMeklingerEtterTidsbruk.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule007AvsluttedeMeklingerEtterTidsbrukTest.kt)


### Kontroll 008 : Avsluttede meklinger, ikke overholdt tidsfrist

|         | MEKLER         | KLIENT         | TOT             |
|---------|----------------|----------------|-----------------|
| FORHOLD | FORHOLD_MEKLER | FORHOLD_KLIENT | **FORHOLD_TOT** |

**For** hver rad og kolonne<br/>
**gitt** feltet for sum, her kalt **SUM**,<br/>
og feltene på raden eller kolonnen, her kalt **LISTE** er fylt ut med tall, <br/>
og summen av **LISTE**, her kalt **LISTESUM**<br/>
**når** **LISTESUM** er forskjellig fra **SUM**<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Summen (**SUM**) med verdi ({**SUM**}) er ulik summen ({**LISTESUM**}) av følgende liste ({**LISTE**})"

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule008AvsluttedeMeklingerIkkeOverholdtTidsfrist.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule008AvsluttedeMeklingerIkkeOverholdtTidsfristTest.kt)


### Kontroll 009 : Avsluttede meklinger etter varighet, (sumfelt)

| VARIGHET | 1                  | 2                  | 3                  | TOT                   |
|----------|--------------------|--------------------|--------------------|-----------------------|
| **SEP**  | VARIGHET_SEP_1     | VARIGHET_SEP_2     | VARIGHET_SEP_3     | **VARIGHET_SEP_TOT**  |
| **SAM**  | VARIGHET_SAM_1     | VARIGHET_SAM_2     | VARIGHET_SAM_3     | **VARIGHET_SAM_TOT**  |
| **SAK**  | VARIGHET_SAK_1     | VARIGHET_SAK_2     | VARIGHET_SAK_3     | **VARIGHET_SAK_TOT**  |
| **TILB** | VARIGHET_TILB_1    | VARIGHET_TILB_2    | VARIGHET_TILB_3    | **VARIGHET_TILB_TOT** |
| **FLY**  | VARIGHET_FLY_1     | VARIGHET_FLY_2     | VARIGHET_FLY_3     | **VARIGHET_FLY_TOT**  |
| **TOT**  | **VARIGHET_TOT_1** | **VARIGHET_TOT_2** | **VARIGHET_TOT_3** | **VARIGHET_TOT_TOT**  |

**For** hver rad og kolonne<br/>
**gitt** feltet for sum, her kalt **SUM**,<br/>
og feltene på raden eller kolonnen, her kalt **LISTE** er fylt ut med tall, <br/>
og summen av **LISTE**, her kalt **LISTESUM**<br/>
**når** **LISTESUM** er forskjellig fra **SUM**<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Summen (**SUM**) med verdi ({**SUM**}) er ulik summen ({**LISTESUM**}) av følgende liste ({**LISTE**})"

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule009AvsluttedeMeklingerEtterVarighet.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule009AvsluttedeMeklingerEtterVarighetTest.kt)


### Kontroll 011 : Resultat av avsluttede meklinger, (sumfelt)

|                | 1                | 2                | 3                | TOT                |
|----------------|------------------|------------------|------------------|--------------------|
| **RESULT_TOT** | **RESULT_TOT_1** | **RESULT_TOT_2** | **RESULT_TOT_3** | **RESULT_TOT_TOT** |

**For** hver rad og kolonne<br/>
**gitt** feltet for sum, her kalt **SUM**,<br/>
og feltene på raden eller kolonnen, her kalt **LISTE** er fylt ut med tall, <br/>
og summen av **LISTE**, her kalt **LISTESUM**<br/>
**når** **LISTESUM** er forskjellig fra **SUM**<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Summen (**SUM**) med verdi ({**SUM**}) er ulik summen ({**LISTESUM**}) av følgende liste ({**LISTE**})"

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule011ResultatAvAvsluttedeMeklinger.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule011ResultatAvAvsluttedeMeklingerTest.kt)


### Kontroll 014 : Kontroll av totalsummer for meklinger
**Gitt** feltene **MEKLING_TOT_ALLE**, **ENBEGGE_TOT**, **VENTETID_TOT_TOT**, **VARIGHET_TOT_TOT**, **RESULT_TOT_TOT** og deres verdier<br/>
**når** verdiene i disse feltene er forskjellig<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Én eller flere i følgende liste har ulik verdi i forhold til de andre: {LISTE med feltene og deres verdier}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule014KontrollAvTotalsummerForMeklinger.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule014KontrollAvTotalsummerForMeklingerTest.kt)


### Kontroll 017 : Barn deltatt, (sumfelt)

|          | DELT      | IKKE DELT      | TOT          |
|----------|-----------|----------------|--------------|
| **BARN** | BARN_DELT | BARN_IKKE_DELT | **BARN_TOT** |

**For** hver rad og kolonne<br/>
**gitt** feltet for sum, her kalt **SUM**,<br/>
og feltene på raden eller kolonnen, her kalt **LISTE** er fylt ut med tall, <br/>
og summen av **LISTE**, her kalt **LISTESUM**<br/>
**når** **LISTESUM** er forskjellig fra **SUM**<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Summen (**SUM**) med verdi ({**SUM**}) er ulik summen ({**LISTESUM**}) av følgende liste ({**LISTE**})"

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule017BarnDeltatt.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule017BarnDeltattTest.kt)


### Kontroll 018 : Bekymringsmelding sendt, (sumfelt)

|            | SENDT        | IKKE SENDT        | TOT            |
|------------|--------------|-------------------|----------------|
| **BEKYMR** | BEKYMR_SENDT | BEKYMR_IKKE_SENDT | **BEKYMR_TOT** |

**For** hver rad og kolonne<br/>
**gitt** feltet for sum, her kalt **SUM**,<br/>
og feltene på raden eller kolonnen, her kalt **LISTE** er fylt ut med tall, <br/>
og summen av **LISTE**, her kalt **LISTESUM**<br/>
**når** **LISTESUM** er forskjellig fra **SUM**<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Summen (**SUM**) med verdi ({**SUM**}) er ulik summen ({**LISTESUM**}) av følgende liste ({**LISTE**})"

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule018BekymringsmeldingSendt.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule018BekymringsmeldingSendtTest.kt)


### Kontroll 019 : Vold som tema, (sumfelt)

|          | TEMA      | IKKE TEMA      | TOT          |
|----------|-----------|----------------|--------------|
| **VOLD** | VOLD_TEMA | VOLD_IKKE_TEMA | **VOLD_TOT** |

**For** hver rad og kolonne<br/>
**gitt** feltet for sum, her kalt **SUM**,<br/>
og feltene på raden eller kolonnen, her kalt **LISTE** er fylt ut med tall, <br/>
og summen av **LISTE**, her kalt **LISTESUM**<br/>
**når** **LISTESUM** er forskjellig fra **SUM**<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Summen (**SUM**) med verdi ({**SUM**}) er ulik summen ({**LISTESUM**}) av følgende liste ({**LISTE**})"

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule019VoldSomTema.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern55/Rule019VoldSomTemaTest.kt)


### Kontroll 020 : Kontroll av totalsummer for meklinger
**Gitt** feltene **BARN_TOT**, **BEKYMR_TOT**, **VOLD_TOT** og deres verdier<br/>
**når** verdiene i disse feltene er forskjellig<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Én eller flere i følgende liste har ulik verdi i forhold til de andre: {LISTE med feltene og deres verdier}."
