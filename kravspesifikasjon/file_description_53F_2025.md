# Filbeskrivelse: 53F Utadrettet virksomhet i familieverntjenesten (2025)

Filbeskrivelse for 53F Utadrettet virksomhet i familieverntjenesten for 2025

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `FYLKE_NR` | Fylkesnummer | 2 | 1‑2 | STRING_TYPE | ☑️ |  | `03`: Oslo<br/>`11`: Rogaland<br/>`15`: Møre og Romsdal<br/>`18`: Nordland - Nordlánnda<br/>`31`: Østfold<br/>`32`: Akershus<br/>`33`: Buskerud<br/>`34`: Innlandet<br/>`39`: Vestfold<br/>`40`: Telemark<br/>`42`: Agder<br/>`46`: Vestland<br/>`50`: Trøndelag - Trööndelage<br/>`55`: Troms - Romsa - Tromssa<br/>`56`: Finnmark - Finnmárku - Finmarkku<br/>`99`: Uoppgitt |
| `KONTORNR` | Familievernkontornummer | 3 | 3‑5 | STRING_TYPE | ☑️ |  | `017`: Familievernkontoret Østfold<br/>`023`: Familievernkontoret Asker Bærum<br/>`024`: Familievernkontoret Follo<br/>`025`: Familievernkontoret Nedre Romerike<br/>`027`: Familievernkontoret Øvre Romerike Glåmdal<br/>`030`: Familiekontoret Oslo Nord<br/>`037`: Familievernkontoret Homansbyen<br/>`038`: Familievernkontoret Enerhaugen<br/>`039`: Familievernkontoret Chrisiania<br/>`046`: Familievernkontoret Innlandet vest<br/>`047`: Familievernkontoret Innlandet øst<br/>`052`: Familievernkontoret Otta SKF<br/>`061`: Familievernkontoret i Buskerud<br/>`065`: Familievernkontoret Ringerike - Hallingdal<br/>`071`: Familievernkontoret i Vestfold<br/>`073`: Familievernkontoret i Søndre Vestfold<br/>`081`: Grenland familievernkontor<br/>`082`: Familiekontoret Øvre Telemark<br/>`091`: Familievernkontoret i Arendal<br/>`101`: Familiekontoret i Vest-Agder<br/>`111`: Familievernkontoret i Sør-Rogaland<br/>`112`: Familievernkontoret Haugalandet<br/>`125`: Bergen og omland familiekontor<br/>`127`: Bjørgvin familiekontor<br/>`141`: Familiekontora for Sunnfjord og Sogn<br/>`142`: Nordfjord familiekontor<br/>`151`: Familievernkontoret i Romsdal<br/>`152`: Familievernkontoret Sunnmøre<br/>`153`: Familievernkontoret Nordmøre<br/>`162`: Familiervernkontoret i Trondheim<br/>`171`: Familievernkontoret Innherred<br/>`172`: Familievernkontoret Namdalen<br/>`181`: Bodø familievernkontor<br/>`183`: Familievernkontoret i Mo i Rana<br/>`184`: Familievernkontoret i Mosjøen<br/>`185`: Lofoten og Vesterålen Familievernkontor<br/>`192`: Tromsø familievernkontor<br/>`193`: Finnsnes familievernkontor<br/>`194`: Harstad og Narvik familievernkontor<br/>`202`: Familievernkontoret i Øst-Finnmark<br/>`203`: Indre Finnmark familievernkontor - Sis Finnmarkku Bearassuojalanguovddas<br/>`205`: Alta og Hammerfest familievernkontor |
| `TILTAK_PUBLIKUM_TILTAK` | Andre tiltak mot publikum publikum, tiltak | 4 | 6‑9 | INTEGER_TYPE |  |  |  |
| `TILTAK_PUBLIKUM_TIMER` | Andre tiltak mot publikum publikum, timer | 4 | 10‑13 | INTEGER_TYPE |  |  |  |
| `VEILEDNING_STUDENTER_TILTAK` | Undervisning/veiledning av studenter, tiltak | 4 | 14‑17 | INTEGER_TYPE |  |  |  |
| `VEILEDNING_STUDENTER_TIMER` | Undervisning/veiledning av studenter, timer | 4 | 18‑21 | INTEGER_TYPE |  |  |  |
| `VEILEDNING_HJELPEAP_TILTAK` | Veiledning/konsultasjon av hjelpeapparatet, tiltak | 4 | 22‑25 | INTEGER_TYPE |  |  |  |
| `VEILEDNING_HJELPEAP_TIMER` | Veiledning/konsultasjon av hjelpeapparatet, timer | 4 | 26‑29 | INTEGER_TYPE |  |  |  |
| `INFO_MEDIA_TILTAK` | Informasjon i media, tiltak | 4 | 30‑33 | INTEGER_TYPE |  |  |  |
| `INFO_MEDIA_TIMER` | Informasjon i media, timer | 4 | 34‑37 | INTEGER_TYPE |  |  |  |
| `TILSYN_TILTAK` | Tilsyn, tiltak | 4 | 38‑41 | INTEGER_TYPE |  |  |  |
| `TILSYN_TIMER` | Tilsyn, timer | 4 | 42‑45 | INTEGER_TYPE |  |  |  |
| `FORELDREVEIL_TILTAK` | Undervisning/kurs/samarbeidsmøter hjelpeapparat – foreldreveiledning | 4 | 46‑49 | INTEGER_TYPE |  |  |  |
| `FORELDREVEIL_TIMER` | Undervisning/kurs/samarbeidsmøter hjelpeapparat – foreldreveiledning | 4 | 50‑53 | INTEGER_TYPE |  |  |  |
| `ANNET_TILTAK` | Undervisning/kurs/samarbeidsmøter hjelpeapparat – annet - tiltak | 4 | 54‑57 | INTEGER_TYPE |  |  |  |
| `ANNET_TIMER` | Undervisning/kurs/samarbeidsmøter hjelpeapparat – annet - timer | 4 | 58‑61 | INTEGER_TYPE |  |  |  |
