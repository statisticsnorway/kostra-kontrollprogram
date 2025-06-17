# Filbeskrivelse: 55F Meklingssaker i familieverntjenesten (2025)

Filbeskrivelse for 55F Meklingssaker i familieverntjenesten for 2025

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `FYLKE_NR` | Fylkesnummer | 2 | 1‑2 | STRING_TYPE | ☑️ |  | `03`: Oslo<br/>`11`: Rogaland<br/>`15`: Møre og Romsdal<br/>`18`: Nordland - Nordlánnda<br/>`31`: Østfold<br/>`32`: Akershus<br/>`33`: Buskerud<br/>`34`: Innlandet<br/>`39`: Vestfold<br/>`40`: Telemark<br/>`42`: Agder<br/>`46`: Vestland<br/>`50`: Trøndelag - Trööndelage<br/>`55`: Troms - Romsa - Tromssa<br/>`56`: Finnmark - Finnmárku - Finmarkku<br/>`99`: Uoppgitt |
| `FYLKE_NAVN` | Fylkesnavn | 34 | 3‑36 | STRING_TYPE | ☑️ |  |  |
| `KONTOR_NR` | Familievernkontornummer | 3 | 37‑39 | STRING_TYPE | ☑️ |  | `017`: Familievernkontoret Østfold<br/>`023`: Familievernkontoret Asker Bærum<br/>`024`: Familievernkontoret Follo<br/>`025`: Familievernkontoret Nedre Romerike<br/>`027`: Familievernkontoret Øvre Romerike Glåmdal<br/>`030`: Familiekontoret Oslo Nord<br/>`037`: Familievernkontoret Homansbyen<br/>`038`: Familievernkontoret Enerhaugen<br/>`039`: Familievernkontoret Chrisiania<br/>`046`: Familievernkontoret Innlandet vest<br/>`047`: Familievernkontoret Innlandet øst<br/>`052`: Familievernkontoret Otta SKF<br/>`061`: Familievernkontoret i Buskerud<br/>`065`: Familievernkontoret Ringerike - Hallingdal<br/>`071`: Familievernkontoret i Vestfold<br/>`073`: Familievernkontoret i Søndre Vestfold<br/>`081`: Grenland familievernkontor<br/>`082`: Familiekontoret Øvre Telemark<br/>`091`: Familievernkontoret i Arendal<br/>`101`: Familiekontoret i Vest-Agder<br/>`111`: Familievernkontoret i Sør-Rogaland<br/>`112`: Familievernkontoret Haugalandet<br/>`125`: Bergen og omland familiekontor<br/>`127`: Bjørgvin familiekontor<br/>`141`: Familiekontora for Sunnfjord og Sogn<br/>`142`: Nordfjord familiekontor<br/>`151`: Familievernkontoret i Romsdal<br/>`152`: Familievernkontoret Sunnmøre<br/>`153`: Familievernkontoret Nordmøre<br/>`162`: Familiervernkontoret i Trondheim<br/>`171`: Familievernkontoret Innherred<br/>`172`: Familievernkontoret Namdalen<br/>`181`: Bodø familievernkontor<br/>`183`: Familievernkontoret i Mo i Rana<br/>`184`: Familievernkontoret i Mosjøen<br/>`185`: Lofoten og Vesterålen Familievernkontor<br/>`192`: Tromsø familievernkontor<br/>`193`: Finnsnes familievernkontor<br/>`194`: Harstad og Narvik familievernkontor<br/>`202`: Familievernkontoret i Øst-Finnmark<br/>`203`: Indre Finnmark familievernkontor - Sis Finnmarkku Bearassuojalanguovddas<br/>`205`: Alta og Hammerfest familievernkontor |
| `MEKLING_SEP_1` | Avsluttede meklinger, separasjon/skilsmisse, 1 time | 5 | 40‑44 | INTEGER_TYPE |  |  |  |
| `MEKLING_SEP_2` | Avsluttede meklinger, separasjon/skilsmisse, 2-4 timer | 5 | 45‑49 | INTEGER_TYPE |  |  |  |
| `MEKLING_SEP_3` | Avsluttede meklinger, separasjon/skilsmisse, 5-7 timer | 5 | 50‑54 | INTEGER_TYPE |  |  |  |
| `MEKLING_SEP_TOT` | Avsluttede meklinger, separasjon/skilsmisse, totalt | 6 | 55‑60 | INTEGER_TYPE |  |  |  |
| `MEKLING_SEP_AVLYST` | Avsluttede meklinger, separasjon/skilsmisse, avlyste | 4 | 61‑64 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAM_1` | Avsluttede meklinger, samboerbrudd, 1 time | 5 | 65‑69 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAM_2` | Avsluttede meklinger, samboerbrudd, 2-4 timer | 5 | 70‑74 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAM_3` | Avsluttede meklinger, samboerbrudd, 5-7 timer | 5 | 75‑79 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAM_TOT` | Avsluttede meklinger, samboerbrudd, totalt | 6 | 80‑85 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAM_AVLYST` | Avsluttede meklinger, samboerbrudd, avlyste | 4 | 86‑89 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAK_1` | Avsluttede meklinger, sak, 1 time | 5 | 90‑94 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAK_2` | Avsluttede meklinger, sak, 2-4 timer | 5 | 95‑99 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAK_3` | Avsluttede meklinger, sak, 5-7 timer | 5 | 100‑104 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAK_TOT` | Avsluttede meklinger, sak, totalt | 6 | 105‑110 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAK_AVLYST` | Avsluttede meklinger, sak, avlyste | 4 | 111‑114 | INTEGER_TYPE |  |  |  |
| `MEKLING_TILB_1` | Avsluttede meklinger, tilbakesendelse, 1 time | 4 | 115‑118 | INTEGER_TYPE |  |  |  |
| `MEKLING_TILB_2` | Avsluttede meklinger, tilbakesendelse, 2-4 timer | 4 | 119‑122 | INTEGER_TYPE |  |  |  |
| `MEKLING_TILB_3` | Avsluttede meklinger, tilbakesendelse, 5-7 timer | 4 | 123‑126 | INTEGER_TYPE |  |  |  |
| `MEKLING_TILB_TOT` | Avsluttede meklinger, tilbakesendelse, totalt | 5 | 127‑131 | INTEGER_TYPE |  |  |  |
| `MEKLING_TILB_AVLYST` | Avsluttede meklinger, tilbakesendelse, avlyste | 4 | 132‑135 | INTEGER_TYPE |  |  |  |
| `MEKLING_FLY_1` | Avsluttede meklinger, flytting, 1 time | 5 | 136‑140 | INTEGER_TYPE |  |  |  |
| `MEKLING_FLY_2` | Avsluttede meklinger, flytting, 2-4 timer | 5 | 141‑145 | INTEGER_TYPE |  |  |  |
| `MEKLING_FLY_3` | Avsluttede meklinger, flytting, 5-7 timer | 5 | 146‑150 | INTEGER_TYPE |  |  |  |
| `MEKLING_FLY_TOT` | Avsluttede meklinger, flytting, totalt | 6 | 151‑156 | INTEGER_TYPE |  |  |  |
| `MEKLING_FLY_AVLYST` | Avsluttede meklinger, flytting, avlyste | 5 | 157‑161 | INTEGER_TYPE |  |  |  |
| `MEKLING_TOT_1` | Avsluttede meklinger, totalt, 1 time | 6 | 162‑167 | INTEGER_TYPE |  |  |  |
| `MEKLING_TOT_2` | Avsluttede meklinger, totalt, 2-4 timer | 6 | 168‑173 | INTEGER_TYPE |  |  |  |
| `MEKLING_TOT_3` | Avsluttede meklinger, totalt, 5-7 timer | 6 | 174‑179 | INTEGER_TYPE |  |  |  |
| `MEKLING_TOT_ALLE` | Avsluttede meklinger, totalt, totalt | 6 | 180‑185 | INTEGER_TYPE | ☑️ |  |  |
| `MEKLING_TOT_AVLYST` | Avsluttede meklinger, totalt, avlyste | 5 | 186‑190 | INTEGER_TYPE |  |  |  |
| `SEP_BEGGE` | Oppmøte, separasjon/skilsmisse, begge møtt | 5 | 191‑195 | INTEGER_TYPE |  |  |  |
| `SEP_EN` | Oppmøte, separasjon/skilsmisse, en møtt | 5 | 196‑200 | INTEGER_TYPE |  |  |  |
| `SEP_TOT` | Oppmøte, separasjon/skilsmisse, totalt | 6 | 201‑206 | INTEGER_TYPE |  |  |  |
| `SAM_BEGGE` | Oppmøte, samboere, begge møtt | 5 | 207‑211 | INTEGER_TYPE |  |  |  |
| `SAM_EN` | Oppmøte, samboere, en møtt | 5 | 212‑216 | INTEGER_TYPE |  |  |  |
| `SAM_TOT` | Oppmøte, samboere, totalt | 6 | 217‑222 | INTEGER_TYPE |  |  |  |
| `SAK_BEGGE` | Oppmøte, sak, begge møtt | 5 | 223‑227 | INTEGER_TYPE |  |  |  |
| `SAK_EN` | Oppmøte, sak, en møtt | 5 | 228‑232 | INTEGER_TYPE |  |  |  |
| `SAK_TOT` | Oppmøte, sak, totalt | 6 | 233‑238 | INTEGER_TYPE |  |  |  |
| `TILB_BEGGE` | Oppmøte, tilbakesendt fra retten, begge møtt | 4 | 239‑242 | INTEGER_TYPE |  |  |  |
| `TILB_EN` | Oppmøte, tilbakesendt fra retten, en møtt | 4 | 243‑246 | INTEGER_TYPE |  |  |  |
| `TILB_TOT` | Oppmøte, tilbakesendt fra retten, totalt | 5 | 247‑251 | INTEGER_TYPE |  |  |  |
| `FLY_BEGGE` | Oppmøte, flytting, begge møtt | 5 | 252‑256 | INTEGER_TYPE |  |  |  |
| `FLY_EN` | Oppmøte, flytting, en møtt | 5 | 257‑261 | INTEGER_TYPE |  |  |  |
| `FLY_TOT` | Oppmøte, flytting, totalt | 6 | 262‑267 | INTEGER_TYPE |  |  |  |
| `BEGGE_TOT` | Oppmøte, begge møtt, totalt | 6 | 268‑273 | INTEGER_TYPE |  |  |  |
| `EN_TOT` | Oppmøte, en møtt, totalt | 6 | 274‑279 | INTEGER_TYPE |  |  |  |
| `ENBEGGE_TOT` | Oppmøte, totalt, totalt | 6 | 280‑285 | INTEGER_TYPE | ☑️ |  |  |
| `VENTETID_SEP_1` | Ventetid, separasjon/skilsmisse, 0-1 uke | 5 | 286‑290 | INTEGER_TYPE |  |  |  |
| `VENTETID_SEP_2` | Ventetid, separasjon/skilsmisse, 1-2 uker | 5 | 291‑295 | INTEGER_TYPE |  |  |  |
| `VENTETID_SEP_3` | Ventetid, separasjon/skilsmisse, 2-3 uker | 5 | 296‑300 | INTEGER_TYPE |  |  |  |
| `VENTETID_SEP_4` | Ventetid, separasjon/skilsmisse, lengre enn 3 uker | 5 | 301‑305 | INTEGER_TYPE |  |  |  |
| `VENTETID_SEP_TOT` | Ventetid, separasjon/skilsmisse, totalt | 6 | 306‑311 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAM_1` | Ventetid, samboerbrudd, 0-1 uke | 5 | 312‑316 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAM_2` | Ventetid, samboerbrudd, 1-2 uker | 5 | 317‑321 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAM_3` | Ventetid, samboerbrudd, 2-3 uker | 5 | 322‑326 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAM_4` | Ventetid, samboerbrudd, lengre enn 3 uker | 5 | 327‑331 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAM_TOT` | Ventetid, samboerbrudd, totalt | 6 | 332‑337 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAK_1` | Ventetid, sak, 0-1 uke | 5 | 338‑342 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAK_2` | Ventetid, sak, 1-2 uker | 5 | 343‑347 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAK_3` | Ventetid, sak, 2-3 uker | 5 | 348‑352 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAK_4` | Ventetid, sak, lengre enn 3 uker | 5 | 353‑357 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAK_TOT` | Ventetid, sak, totalt | 6 | 358‑363 | INTEGER_TYPE |  |  |  |
| `VENTETID_TILB_1` | Ventetid, tilbakesendt fra retten, 0-1 uke | 4 | 364‑367 | INTEGER_TYPE |  |  |  |
| `VENTETID_TILB_2` | Ventetid, tilbakesendt fra retten, 1-2 uker | 4 | 368‑371 | INTEGER_TYPE |  |  |  |
| `VENTETID_TILB_3` | Ventetid, tilbakesendt fra retten, 2-3 uker | 4 | 372‑375 | INTEGER_TYPE |  |  |  |
| `VENTETID_TILB_4` | Ventetid, tilbakesendt fra retten, lengre enn 3 uker | 4 | 376‑379 | INTEGER_TYPE |  |  |  |
| `VENTETID_TILB_TOT` | Ventetid, tilbakesendt fra retten, totalt | 5 | 380‑384 | INTEGER_TYPE |  |  |  |
| `VENTETID_FLY_1` | Ventetid, flytting, 0-1 uke | 5 | 385‑389 | INTEGER_TYPE |  |  |  |
| `VENTETID_FLY_2` | Ventetid, flytting, 1-2 uker | 5 | 390‑394 | INTEGER_TYPE |  |  |  |
| `VENTETID_FLY_3` | Ventetid, flytting, 2-3 uker | 5 | 395‑399 | INTEGER_TYPE |  |  |  |
| `VENTETID_FLY_4` | Ventetid, flytting, lengre enn 3 uker | 5 | 400‑404 | INTEGER_TYPE |  |  |  |
| `VENTETID_FLY_TOT` | Ventetid, flytting, totalt | 6 | 405‑410 | INTEGER_TYPE |  |  |  |
| `VENTETID_TOT_1` | Ventetid, totalt, 0-1 uke | 6 | 411‑416 | INTEGER_TYPE |  |  |  |
| `VENTETID_TOT_2` | Ventetid, totalt, 1-2 uker | 6 | 417‑422 | INTEGER_TYPE |  |  |  |
| `VENTETID_TOT_3` | Ventetid, totalt, 2-3 uker | 6 | 423‑428 | INTEGER_TYPE |  |  |  |
| `VENTETID_TOT_4` | Ventetid, totalt, lengre enn 3 uker | 6 | 429‑434 | INTEGER_TYPE |  |  |  |
| `VENTETID_TOT_TOT` | Ventetid, totalt, totalt | 6 | 435‑440 | INTEGER_TYPE | ☑️ |  |  |
| `FORHOLD_MEKLER` | Forhold, lengre enn 3 uker skyldes forhold hos mekler, antall | 5 | 441‑445 | INTEGER_TYPE |  |  |  |
| `FORHOLD_KLIENT` | Forhold, lengre enn 3 uker skyldes forhold hos foreldre, antall | 5 | 446‑450 | INTEGER_TYPE |  |  |  |
| `FORHOLD_TOT` | Forhold, lengre enn 3 uker totalt, antall | 6 | 451‑456 | INTEGER_TYPE | ☑️ |  |  |
| `VARIGHET_SEP_1` | Varighet, separasjon/skilsmisse, 0-2 mnd. | 5 | 457‑461 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SEP_2` | Varighet, separasjon/skilsmisse, 2-6 mnd. | 5 | 462‑466 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SEP_3` | Varighet, separasjon/skilsmisse, lengre enn 6 mnd. | 5 | 467‑471 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SEP_TOT` | Varighet, separasjon/skilsmisse, totalt | 6 | 472‑477 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAM_1` | Varighet, samboerbrudd, 0-2 mnd. | 5 | 478‑482 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAM_2` | Varighet, samboerbrudd, 2-6 mnd. | 5 | 483‑487 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAM_3` | Varighet, samboerbrudd, lengre enn 6 mnd. | 5 | 488‑492 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAM_TOT` | Varighet, samboerbrudd, totalt | 6 | 493‑498 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAK_1` | Varighet, sak, 0-2 mnd. | 5 | 499‑503 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAK_2` | Varighet, sak, 2-6 mnd. | 5 | 504‑508 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAK_3` | Varighet, sak, lengre enn 6 mnd. | 5 | 509‑513 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAK_TOT` | Varighet, sak, totalt | 6 | 514‑519 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TILB_1` | Varighet, tilbakesendt fra retten, 0-2 mnd. | 4 | 520‑523 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TILB_2` | Varighet, tilbakesendt fra retten, 2-6 mnd. | 4 | 524‑527 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TILB_3` | Varighet, tilbakesendt fra retten, lengre enn 6 mnd. | 4 | 528‑531 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TILB_TOT` | Varighet, tilbakesendt fra retten, totalt | 5 | 532‑536 | INTEGER_TYPE |  |  |  |
| `VARIGHET_FLY_1` | Varighet, flytting, 0-2 mnd. | 5 | 537‑541 | INTEGER_TYPE |  |  |  |
| `VARIGHET_FLY_2` | Varighet, flytting, 2-6 mnd. | 5 | 542‑546 | INTEGER_TYPE |  |  |  |
| `VARIGHET_FLY_3` | Varighet, flytting, lengre enn 6 mnd. | 5 | 547‑551 | INTEGER_TYPE |  |  |  |
| `VARIGHET_FLY_TOT` | Varighet, flytting, totalt | 6 | 552‑557 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TOT_1` | Varighet, totalt, 0-2 mnd. | 6 | 558‑563 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TOT_2` | Varighet, totalt, 2-6 mnd. | 6 | 564‑569 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TOT_3` | Varighet, totalt, lengre enn 6 mnd. | 6 | 570‑575 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TOT_TOT` | Varighet, totalt, totalt | 6 | 576‑581 | INTEGER_TYPE | ☑️ |  |  |
| `RESULT_TOT_1` | Resultat, avtale inngått | 6 | 582‑587 | INTEGER_TYPE |  |  |  |
| `RESULT_TOT_2` | Resultat, avtale ikke inngått | 6 | 588‑593 | INTEGER_TYPE |  |  |  |
| `RESULT_TOT_3` | Resultat, uavklart | 6 | 594‑599 | INTEGER_TYPE |  |  |  |
| `RESULT_TOT_TOT` | Resultat, totalt | 6 | 600‑605 | INTEGER_TYPE | ☑️ |  |  |
| `BARN_DELT` | Saksgang, Barn deltatt | 6 | 606‑611 | INTEGER_TYPE | ☑️ |  |  |
| `BARN_IKKE_DELT` | Saksgang, Barn ikke deltatt | 6 | 612‑617 | INTEGER_TYPE |  |  |  |
| `BARN_TOT` | Saksgang, Barn delt/ikke delt i alt | 6 | 618‑623 | INTEGER_TYPE | ☑️ |  |  |
| `BEKYMR_SENDT` | Saksgang, Bekymringsmelding sendt | 6 | 624‑629 | INTEGER_TYPE | ☑️ |  |  |
| `BEKYMR_IKKE_SENDT` | Saksgang, Bekymringsmelding ikke sendt | 6 | 630‑635 | INTEGER_TYPE |  |  |  |
| `BEKYMR_TOT` | Saksgang, Bekymring. sendt/ikke sendt i alt | 6 | 636‑641 | INTEGER_TYPE | ☑️ |  |  |
| `VOLD_TEMA` | Saksgang, Vold tema for mekling | 6 | 642‑647 | INTEGER_TYPE | ☑️ |  |  |
| `VOLD_IKKE_TEMA` | Saksgang, Vold ikke tema for mekling | 6 | 648‑653 | INTEGER_TYPE |  |  |  |
| `VOLD_TOT` | Saksgang, Vold tema/ikke tema i alt | 6 | 654‑659 | INTEGER_TYPE | ☑️ |  |  |
