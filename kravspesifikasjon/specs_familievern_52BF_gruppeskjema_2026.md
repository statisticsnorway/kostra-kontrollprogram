# Kravspesifikasjon Gruppeskjema for familievernet, skjema 52BF, 2026

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

Filbeskrivelse finnes som [markdown](file_description_52AF_2026.md) og [yaml](file_description_52AF_2026.yaml)
.
Endringslogg for filbeskrivelse finnes som [markdown](changelog_for_52BF_from_2025_to_2026.md).

## Krav

### Kontroll 001: Recordlengde
Se krav i [kravspesifikasjon/felles_posisjonsbaserte_filvedlegg.md](felles_posisjonsbaserte_filvedlegg.md#kontroll-001--recordlengde)


### Kontroll 002: Filbeskrivelse
Se krav i [kravspesifikasjon/felles_posisjonsbaserte_filvedlegg.md](felles_posisjonsbaserte_filvedlegg.md#kontroll-002--filbeskrivelse-mangler-obligatorisk-verdi)



### Kontroll 003 : Regionsnummer
**Gitt** gyldige regioner i [koblingshierarkiet for familievern](mapping_familievern_region_fylke_kontor_2026.md)<br/>
**når** feltet **REGION_NR_B** inneholder ingen av de gyldige regionene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Regionsnummeret som er oppgitt i recorden fins ikke i listen med gyldige regionsnumre. Fant '{REGION_NR_B}', forventet én av : {GYLDIGE_REGIONER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule003Regionsnummer.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule003RegionsnummerTest.kt)


### Kontroll 004 : Kontornummer
**Gitt** gyldige kontornumre i [koblingshierarkiet for familievern](mapping_familievern_region_fylke_kontor_2026.md)<br/>
**når** feltet **KONTOR_NR_B** inneholder ingen av de gyldige kontornumrene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Kontornummeret som er oppgitt i recorden fins ikke i listen med gyldige kontornumre. Fant '{KONTOR_NR_B}', forventet én av : {GYLDIGE_KONTORER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule004Kontornummer.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule004KontornummerTest.kt)


### Kontroll 005 : Manglende samsvar mellom regions- og kontornummer
**Gitt** koblingene mellom regioner og kontornumre i [koblingshierarkiet for familievern](mapping_familievern_region_fylke_kontor_2026.md)<br/>
**når** feltet **REGION_NR_B** og **KONTOR_NR_B** ikke samsvarer med koblingene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Regionsnummer '{REGION_NR_B}' og kontornummer '{KONTOR_NR_B}' stemmer ikke overens."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule005RegionsnummerKontornummer.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule005RegionsnummerKontornummerTest.kt)


### Kontroll 006 : Dubletter
**Gitt** feltene **KONTOR_NR_B** og **GRUPPE_NR_B**<br/>
**når** feltet **GRUPPE_NR_** forekommer mer enn én gang per **KONTOR_NR_B**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Gruppenummeret er benyttet på mer enn en sak ({ANTALL_SAKER} stk). Dubletter på kontornummer '{KONTOR_NR_B}' - gruppenummer '{GRUPPE_NR_B}'.

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule006Dubletter.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule006DubletterTest.kt)


### Kontroll 007 : Gruppenavn
**Gitt** feltet **GRUPPE_NAVN_B**<br/>
**når** feltet **GRUPPE_NAVN_B** er tomt eller inneholder kun blanke tegn,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke oppgitt navn på gruppen. Tekstfeltet skal ha maksimalt 30 posisjoner."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule007Gruppenavn.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule007GruppenavnTest.kt)


### Kontroll 008 : Start dato
**Gitt** feltet **DATO_GRSTART_B**<br/>
**når** feltet **DATO_GRSTART_B** ikke er en gyldig dato i formatet **ddMMyyyy**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke oppgitt dato for gruppebehandlingens start på formatet DDMMÅÅÅÅ. Fant '{DATO_GRSTART_B}'."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule008StartDato.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule008StartDatoTest.kt)


### Kontroll 009 : Målgruppe
**Gitt** gyldige kode fra [filbeskrivelsen for skjema 52BF](file_description_52BF_2026.md)<br/>
**når** feltet **STRUKTUR_GR_B** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hva som er målgruppe for behandlingen. Fant '{STRUKTUR_GR_B}', forventet én av: {GYLDIGE KODER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule009Malgruppe.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule009MalgruppeTest.kt)


### Kontroll 010 : Gruppens hovedtema
**Gitt** gyldige kode fra [filbeskrivelsen for skjema 52BF](file_description_52BF_2026.md)<br/>
**når** feltet **HOVEDI_GR_B** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hva som er hovedtema for behandlingen. Fant '{HOVEDI_GR_B}', forventet én av: {GYLDIGE KODER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule010GruppensHovedtema.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule010GruppensHovedtemaTest.kt)


### Kontroll 011 : Antall gruppemøter gjennomført totalt i løpet av året
**Gitt** feltet **ANTMOTERTOT_IARET_B**<br/>
**når** feltet **ANTMOTERTOT_IARET_B** inneholder ingen timer,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt i løpet av rapporteringsåret."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule011AntallGruppemoterGjennomfortILA.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule011AntallGruppemoterGjennomfortILATest.kt)


### Kontroll 012 : Antall gruppemøter gjennomført totalt siden opprettelsen
**Gitt** feltet **ANTMOTERTOT_OPPR_B**<br/>
**når** feltet **ANTMOTERTOT_OPPR_B** inneholder ingen gruppemøter,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt siden gruppen ble opprettet."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule012AntallGruppemoterGjennomfortSO.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule012AntallGruppemoterGjennomfortSOTest.kt)


### Kontroll 013 : Antall timer anvendt i gruppen totalt i løpet av året
**Gitt** feltet **TIMERTOT_IARET_B**<br/>
**når** feltet **TIMERTOT_IARET_B** inneholder ingen timer,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen i løpet av året. (For og etterarbeid skal ikke regnes med)."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule013AntallTimerAnvendtIGruppenTotaltILA.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule013AntallTimerAnvendtIGruppenTotaltILATest.kt)


### Kontroll 014 : Antall timer anvendt i gruppen totalt siden opprettelsen
**Gitt** feltet **TIMERTOT_OPPR_B**<br/>
**når** feltet **TIMERTOT_OPPR_B** inneholder ingen timer,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen siden opprettelsen"

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule014AntallTimerAnvendtIGruppenTotaltSO.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule014AntallTimerAnvendtIGruppenTotaltSOTest.kt)


### Kontroll 015 : Antall deltakere i gruppen i løpet av året
**Gitt** feltet **ANTDELT_IARET_B**<br/>
**når** feltet **ANTDELT_IARET_B** inneholder ingen deltakere,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange som har deltatt i gruppen i løpet av året. Terapeuter holdes utenom."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule015AntallDeltagereIGruppenILA.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule015AntallDeltagereIGruppenILATest.kt)


### Kontroll 016 : Antall deltakere i gruppen siden opprettelsen
**Gitt** feltet **ANTDELT_OPPR_B**<br/>
**når** feltet **ANTDELT_OPPR_B** inneholder ingen deltakere,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hvor mange som har deltatt i gruppen siden opprettelsen. Terapeuter holdes utenom."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule016AntallDeltagereIGruppenSO.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule016AntallDeltagereIGruppenSOTest.kt)


### Kontroll 017 : Antall terapeuter involvert i gruppebehandlingen
**Gitt** feltet **ANTTER_GRUPPEB_B**<br/>
**når** feltet **ANTTER_GRUPPEB_B** inneholder ingen terapeuter,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke oppgitt hvor mange hovedterapeuter eller andre ansatte som har deltatt i gruppen."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule017AntallTerapeuterInvolvertIGruppebehandlingen.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule017AntallTerapeuterInvolvertIGruppebehandlingenTest.kt)


### Kontroll 018 : Er det benyttet tolk i minst én gruppesamtale?
**Gitt** gyldige kode fra [filbeskrivelsen for skjema 52BF](file_description_52BF_2026.md)<br/>
**når** feltet **TOLK_B** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Kontroller at feltet er utfylt. Fant '{TOLK_B}', forventet én av: {GYLDIGE KODER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule018Tolk.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule018TolkTest.kt)


### Kontroll 019 : Status ved året slutt
**Gitt** gyldige kode fra [filbeskrivelsen for skjema 52BF](file_description_52BF_2026.md)<br/>
**når** feltet **STATUS_ARETSSL_B** inneholder ingen av de gyldige kodene i listen,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er ikke fylt ut hva som er gruppens status ved utgangen av året. Fant '{STATUS_ARETSSL_B}', forventet én av: {GYLDIGE KODER}."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule019StatusVedAretsSlutt.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule019StatusVedAretsSluttTest.kt)


### Kontroll 020 : Gruppebehandlingen er avsluttet, men avslutningsdato mangler
**Gitt** feltene **STATUS_ARETSSL_B** og **DATO_GRAVSLUTN_B**<br/>
**når** feltet **STATUS_ARETSSL_B** er utfylt med kode `2 = Avsluttet`, og feltet **DATO_GRAVSLUTN_B** ikke er en gyldig dato i formatet **ddMMyyyy**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Det er krysset av for at gruppebehandlingen er avsluttet i rapporteringsåret, men ikke fylt ut dato '{DATO_GRAVSLUTN_B}' for avslutning av saken."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule020GruppebehandlingenAvsluttetManglerAvslutningsdato.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule020GruppebehandlingenAvsluttetManglerAvslutningsdatoTest.kt)


### Kontroll 021 : Avslutningsdato før første samtale
**Gitt** feltene **DATO_GRSTART_B** og **DATO_GRAVSLUTN_B** er utfylt med gyldige datoer<br/>
**når** feltet **DATO_GRAVSLUTN_B** er før feltet **DATO_GRSTART_B**,<br/>
**så** gi :warning: **ADVARSEL** med meldingen "Dato for avslutting av gruppebehandlingen '{DATO_GRAVSLUTN_B}' kommer før dato for gruppebehandlingens start '{DATO_GRSTART_B}'."

[kode](/kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule021AvslutningsdatoForForsteSamtale.kt)
[test](/kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/famvern/famvern52b/Rule021AvslutningsdatoForForsteSamtaleTest.kt)

