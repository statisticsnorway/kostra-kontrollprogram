# Filbeskrivelse: 52BF Gruppeskjema for familievernet (2025)

Filbeskrivelse for 52BF Gruppeskjema for familievernet for 2025

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `REGION_NR_B` | Familievernregionnummer | 6 | 1‑6 | STRING_TYPE | ☑️ |  | `667200`: Region øst<br/>`667300`: Region sør<br/>`667400`: Region vest<br/>`667500`: Region Midt-Norge<br/>`667600`: Region nord |
| `KONTOR_NR_B` | Familievernkontornummer | 3 | 7‑9 | STRING_TYPE | ☑️ |  | `017`: Familievernkontoret Østfold<br/>`023`: Familievernkontoret Asker Bærum<br/>`024`: Familievernkontoret Follo<br/>`025`: Familievernkontoret Nedre Romerike<br/>`027`: Familievernkontoret Øvre Romerike Glåmdal<br/>`030`: Familiekontoret Oslo Nord<br/>`037`: Familievernkontoret Homansbyen<br/>`038`: Familievernkontoret Enerhaugen<br/>`039`: Familievernkontoret Chrisiania<br/>`046`: Familievernkontoret Innlandet vest<br/>`047`: Familievernkontoret Innlandet øst<br/>`052`: Familievernkontoret Otta SKF<br/>`061`: Familievernkontoret i Buskerud<br/>`065`: Familievernkontoret Ringerike - Hallingdal<br/>`071`: Familievernkontoret i Vestfold<br/>`073`: Familievernkontoret i Søndre Vestfold<br/>`081`: Grenland familievernkontor<br/>`082`: Familiekontoret Øvre Telemark<br/>`091`: Familievernkontoret i Arendal<br/>`101`: Familiekontoret i Vest-Agder<br/>`111`: Familievernkontoret i Sør-Rogaland<br/>`112`: Familievernkontoret Haugalandet<br/>`125`: Bergen og omland familiekontor<br/>`127`: Bjørgvin familiekontor<br/>`141`: Familiekontora for Sunnfjord og Sogn<br/>`142`: Nordfjord familiekontor<br/>`151`: Familievernkontoret i Romsdal<br/>`152`: Familievernkontoret Sunnmøre<br/>`153`: Familievernkontoret Nordmøre<br/>`162`: Familiervernkontoret i Trondheim<br/>`171`: Familievernkontoret Innherred<br/>`172`: Familievernkontoret Namdalen<br/>`181`: Bodø familievernkontor<br/>`183`: Familievernkontoret i Mo i Rana<br/>`184`: Familievernkontoret i Mosjøen<br/>`185`: Lofoten og Vesterålen Familievernkontor<br/>`192`: Tromsø familievernkontor<br/>`193`: Finnsnes familievernkontor<br/>`194`: Harstad og Narvik familievernkontor<br/>`202`: Familievernkontoret i Øst-Finnmark<br/>`203`: Indre Finnmark familievernkontor - Sis Finnmarkku Bearassuojalanguovddas<br/>`205`: Alta og Hammerfest familievernkontor |
| `GRUPPE_NR_B` | Gruppenummer | 8 | 10‑17 | STRING_TYPE | ☑️ |  |  |
| `GRUPPE_NAVN_B` | Gruppenavn | 30 | 18‑47 | STRING_TYPE |  |  |  |
| `DATO_GRSTART_B` | Dato for gruppebehandlingens start (ddmmåååå) | 8 | 48‑55 | DATE_TYPE | ☑️ | ddMMyyyy |  |
| `STRUKTUR_GR_B` | Målgruppe | 1 | 56‑56 | STRING_TYPE | ☑️ |  | `1`: Par<br/>`2`: Barn (under 18 år)<br/>`3`: Individ<br/>`4`: Familie<br/>`5`: Foreldre |
| `HOVEDI_GR_B` | Hvilket hovedinnhold/tema har gruppen? | 2 | 57‑58 | STRING_TYPE | ☑️ |  | `01`: Samlivskurs<br/>`02`: Samlivsbrudd<br/>`03`: Samarbeid om barn etter brudd<br/>`04`: Barn som har opplevd brudd i familien<br/>`05`: Vold/overgrep<br/>`06`: Sinnemestring<br/>`07`: Kultur-/Minoritetsspørsmål<br/>`08`: Foreldreveiledning<br/>`09`: Foreldre som har mistet omsorgen for egne barn<br/>`10`: Andre alvorlige hendelser<br/>`11`: Annet, spesifiser |
| `ANTMOTERTOT_IARET_B` | Antall gruppemøter gjennomført totalt i løpet av året | 3 | 59‑61 | INTEGER_TYPE |  |  |  |
| `ANTMOTERTOT_OPPR_B` | Antall gruppemøter gjennomført totalt siden opprettelsen | 3 | 62‑64 | INTEGER_TYPE |  |  |  |
| `TIMERTOT_IARET_B` | Antall timer anvendt i gruppen totalt i løpet av året | 3 | 65‑67 | INTEGER_TYPE |  |  |  |
| `TIMERTOT_OPPR_B` | Antall timer anvendt i gruppen totalt siden opprettelsen | 3 | 68‑70 | INTEGER_TYPE |  |  |  |
| `ANTDELT_IARET_B` | Antall deltakere i gruppen i løpet av året | 3 | 71‑73 | INTEGER_TYPE |  |  |  |
| `ANTDELT_OPPR_B` | Antall deltakere i gruppen siden opprettelsen | 3 | 74‑76 | INTEGER_TYPE |  |  |  |
| `ANTTER_GRUPPEB_B` | Antall terapauter involvert i gruppebehandlingen | 2 | 77‑78 | INTEGER_TYPE |  |  |  |
| `TOLK_B` | Tolk benyttet i forbindelse med en eller flere gruppesamtaler siden gruppen ble opprettet | 1 | 79‑79 | STRING_TYPE | ☑️ |  | `1`: Ja<br/>`2`: Nei |
| `STATUS_ARETSSL_B` | Status for gruppen ved årets slutt ved årets slutt? | 1 | 80‑80 | STRING_TYPE | ☑️ |  | `1`: Gruppebehandlingen ikke avsluttet i inneværende år<br/>`2`: Avsluttet |
| `DATO_GRAVSLUTN_B` | Dato for gruppens avslutning (ddmmåååå) | 8 | 81‑88 | DATE_TYPE |  | ddMMyyyy |  |
