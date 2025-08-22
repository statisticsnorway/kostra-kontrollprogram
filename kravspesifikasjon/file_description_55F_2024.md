# Filbeskrivelse: 55F Meklingssaker i familieverntjenesten (2024)

Filbeskrivelse for 55F Meklingssaker i familieverntjenesten for 2024

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `FYLKE_NR` |  | 2 | 1‑2 | STRING_TYPE | ☑️ |  |  |
| `FYLKE_NAVN` |  | 34 | 3‑36 | STRING_TYPE | ☑️ |  |  |
| `KONTOR_NR` |  | 3 | 37‑39 | STRING_TYPE |  |  |  |
| `MEKLING_SEP_1` |  | 5 | 40‑44 | INTEGER_TYPE |  |  |  |
| `MEKLING_SEP_2` |  | 5 | 45‑49 | INTEGER_TYPE |  |  |  |
| `MEKLING_SEP_3` |  | 5 | 50‑54 | INTEGER_TYPE |  |  |  |
| `MEKLING_SEP_TOT` |  | 6 | 55‑60 | INTEGER_TYPE |  |  |  |
| `MEKLING_SEP_AVLYST` |  | 4 | 61‑64 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAM_1` |  | 5 | 65‑69 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAM_2` |  | 5 | 70‑74 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAM_3` |  | 5 | 75‑79 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAM_TOT` |  | 6 | 80‑85 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAM_AVLYST` |  | 4 | 86‑89 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAK_1` |  | 5 | 90‑94 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAK_2` |  | 5 | 95‑99 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAK_3` |  | 5 | 100‑104 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAK_TOT` |  | 6 | 105‑110 | INTEGER_TYPE |  |  |  |
| `MEKLING_SAK_AVLYST` |  | 4 | 111‑114 | INTEGER_TYPE |  |  |  |
| `MEKLING_TILB_1` |  | 4 | 115‑118 | INTEGER_TYPE |  |  |  |
| `MEKLING_TILB_2` |  | 4 | 119‑122 | INTEGER_TYPE |  |  |  |
| `MEKLING_TILB_3` |  | 4 | 123‑126 | INTEGER_TYPE |  |  |  |
| `MEKLING_TILB_TOT` |  | 5 | 127‑131 | INTEGER_TYPE |  |  |  |
| `MEKLING_TILB_AVLYST` |  | 4 | 132‑135 | INTEGER_TYPE |  |  |  |
| `MEKLING_FLY_1` |  | 5 | 136‑140 | INTEGER_TYPE |  |  |  |
| `MEKLING_FLY_2` |  | 5 | 141‑145 | INTEGER_TYPE |  |  |  |
| `MEKLING_FLY_3` |  | 5 | 146‑150 | INTEGER_TYPE |  |  |  |
| `MEKLING_FLY_TOT` |  | 6 | 151‑156 | INTEGER_TYPE |  |  |  |
| `MEKLING_FLY_AVLYST` |  | 5 | 157‑161 | INTEGER_TYPE |  |  |  |
| `MEKLING_TOT_1` |  | 6 | 162‑167 | INTEGER_TYPE |  |  |  |
| `MEKLING_TOT_2` |  | 6 | 168‑173 | INTEGER_TYPE |  |  |  |
| `MEKLING_TOT_3` |  | 6 | 174‑179 | INTEGER_TYPE |  |  |  |
| `MEKLING_TOT_ALLE` |  | 6 | 180‑185 | INTEGER_TYPE | ☑️ |  |  |
| `MEKLING_TOT_AVLYST` |  | 5 | 186‑190 | INTEGER_TYPE |  |  |  |
| `SEP_BEGGE` |  | 5 | 191‑195 | INTEGER_TYPE |  |  |  |
| `SEP_EN` |  | 5 | 196‑200 | INTEGER_TYPE |  |  |  |
| `SEP_TOT` |  | 6 | 201‑206 | INTEGER_TYPE |  |  |  |
| `SAM_BEGGE` |  | 5 | 207‑211 | INTEGER_TYPE |  |  |  |
| `SAM_EN` |  | 5 | 212‑216 | INTEGER_TYPE |  |  |  |
| `SAM_TOT` |  | 6 | 217‑222 | INTEGER_TYPE |  |  |  |
| `SAK_BEGGE` |  | 5 | 223‑227 | INTEGER_TYPE |  |  |  |
| `SAK_EN` |  | 5 | 228‑232 | INTEGER_TYPE |  |  |  |
| `SAK_TOT` |  | 6 | 233‑238 | INTEGER_TYPE |  |  |  |
| `TILB_BEGGE` |  | 4 | 239‑242 | INTEGER_TYPE |  |  |  |
| `TILB_EN` |  | 4 | 243‑246 | INTEGER_TYPE |  |  |  |
| `TILB_TOT` |  | 5 | 247‑251 | INTEGER_TYPE |  |  |  |
| `FLY_BEGGE` |  | 5 | 252‑256 | INTEGER_TYPE |  |  |  |
| `FLY_EN` |  | 5 | 257‑261 | INTEGER_TYPE |  |  |  |
| `FLY_TOT` |  | 6 | 262‑267 | INTEGER_TYPE |  |  |  |
| `BEGGE_TOT` |  | 6 | 268‑273 | INTEGER_TYPE |  |  |  |
| `EN_TOT` |  | 6 | 274‑279 | INTEGER_TYPE |  |  |  |
| `ENBEGGE_TOT` |  | 6 | 280‑285 | INTEGER_TYPE | ☑️ |  |  |
| `VENTETID_SEP_1` |  | 5 | 286‑290 | INTEGER_TYPE |  |  |  |
| `VENTETID_SEP_2` |  | 5 | 291‑295 | INTEGER_TYPE |  |  |  |
| `VENTETID_SEP_3` |  | 5 | 296‑300 | INTEGER_TYPE |  |  |  |
| `VENTETID_SEP_4` |  | 5 | 301‑305 | INTEGER_TYPE |  |  |  |
| `VENTETID_SEP_TOT` |  | 6 | 306‑311 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAM_1` |  | 5 | 312‑316 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAM_2` |  | 5 | 317‑321 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAM_3` |  | 5 | 322‑326 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAM_4` |  | 5 | 327‑331 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAM_TOT` |  | 6 | 332‑337 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAK_1` |  | 5 | 338‑342 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAK_2` |  | 5 | 343‑347 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAK_3` |  | 5 | 348‑352 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAK_4` |  | 5 | 353‑357 | INTEGER_TYPE |  |  |  |
| `VENTETID_SAK_TOT` |  | 6 | 358‑363 | INTEGER_TYPE |  |  |  |
| `VENTETID_TILB_1` |  | 4 | 364‑367 | INTEGER_TYPE |  |  |  |
| `VENTETID_TILB_2` |  | 4 | 368‑371 | INTEGER_TYPE |  |  |  |
| `VENTETID_TILB_3` |  | 4 | 372‑375 | INTEGER_TYPE |  |  |  |
| `VENTETID_TILB_4` |  | 4 | 376‑379 | INTEGER_TYPE |  |  |  |
| `VENTETID_TILB_TOT` |  | 5 | 380‑384 | INTEGER_TYPE |  |  |  |
| `VENTETID_FLY_1` |  | 5 | 385‑389 | INTEGER_TYPE |  |  |  |
| `VENTETID_FLY_2` |  | 5 | 390‑394 | INTEGER_TYPE |  |  |  |
| `VENTETID_FLY_3` |  | 5 | 395‑399 | INTEGER_TYPE |  |  |  |
| `VENTETID_FLY_4` |  | 5 | 400‑404 | INTEGER_TYPE |  |  |  |
| `VENTETID_FLY_TOT` |  | 6 | 405‑410 | INTEGER_TYPE |  |  |  |
| `VENTETID_TOT_1` |  | 6 | 411‑416 | INTEGER_TYPE |  |  |  |
| `VENTETID_TOT_2` |  | 6 | 417‑422 | INTEGER_TYPE |  |  |  |
| `VENTETID_TOT_3` |  | 6 | 423‑428 | INTEGER_TYPE |  |  |  |
| `VENTETID_TOT_4` |  | 6 | 429‑434 | INTEGER_TYPE |  |  |  |
| `VENTETID_TOT_TOT` |  | 6 | 435‑440 | INTEGER_TYPE | ☑️ |  |  |
| `FORHOLD_MEKLER` |  | 5 | 441‑445 | INTEGER_TYPE |  |  |  |
| `FORHOLD_KLIENT` |  | 5 | 446‑450 | INTEGER_TYPE |  |  |  |
| `FORHOLD_TOT` |  | 6 | 451‑456 | INTEGER_TYPE | ☑️ |  |  |
| `VARIGHET_SEP_1` |  | 5 | 457‑461 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SEP_2` |  | 5 | 462‑466 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SEP_3` |  | 5 | 467‑471 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SEP_TOT` |  | 6 | 472‑477 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAM_1` |  | 5 | 478‑482 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAM_2` |  | 5 | 483‑487 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAM_3` |  | 5 | 488‑492 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAM_TOT` |  | 6 | 493‑498 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAK_1` |  | 5 | 499‑503 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAK_2` |  | 5 | 504‑508 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAK_3` |  | 5 | 509‑513 | INTEGER_TYPE |  |  |  |
| `VARIGHET_SAK_TOT` |  | 6 | 514‑519 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TILB_1` |  | 4 | 520‑523 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TILB_2` |  | 4 | 524‑527 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TILB_3` |  | 4 | 528‑531 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TILB_TOT` |  | 5 | 532‑536 | INTEGER_TYPE |  |  |  |
| `VARIGHET_FLY_1` |  | 5 | 537‑541 | INTEGER_TYPE |  |  |  |
| `VARIGHET_FLY_2` |  | 5 | 542‑546 | INTEGER_TYPE |  |  |  |
| `VARIGHET_FLY_3` |  | 5 | 547‑551 | INTEGER_TYPE |  |  |  |
| `VARIGHET_FLY_TOT` |  | 6 | 552‑557 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TOT_1` |  | 6 | 558‑563 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TOT_2` |  | 6 | 564‑569 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TOT_3` |  | 6 | 570‑575 | INTEGER_TYPE |  |  |  |
| `VARIGHET_TOT_TOT` |  | 6 | 576‑581 | INTEGER_TYPE | ☑️ |  |  |
| `BARNDELT_SEP_TOT` |  | 5 | 582‑586 | INTEGER_TYPE |  |  |  |
| `BARNDELT_SAM_TOT` |  | 5 | 587‑591 | INTEGER_TYPE |  |  |  |
| `BARNDELT_SAK_TOT` |  | 5 | 592‑596 | INTEGER_TYPE |  |  |  |
| `BARNDELT_TILB_TOT` |  | 4 | 597‑600 | INTEGER_TYPE |  |  |  |
| `BARNDELT_FLY_TOT` |  | 5 | 601‑605 | INTEGER_TYPE |  |  |  |
| `BARNDELT_TOT_TOT` |  | 6 | 606‑611 | INTEGER_TYPE | ☑️ |  |  |
| `RESULT_SEP_1` |  | 5 | 612‑616 | INTEGER_TYPE |  |  |  |
| `RESULT_SEP_2` |  | 5 | 617‑621 | INTEGER_TYPE |  |  |  |
| `RESULT_SEP_3` |  | 5 | 622‑626 | INTEGER_TYPE |  |  |  |
| `RESULT_SEP_TOT` |  | 6 | 627‑632 | INTEGER_TYPE |  |  |  |
| `RESULT_SAM_1` |  | 5 | 633‑637 | INTEGER_TYPE |  |  |  |
| `RESULT_SAM_2` |  | 5 | 638‑642 | INTEGER_TYPE |  |  |  |
| `RESULT_SAM_3` |  | 5 | 643‑647 | INTEGER_TYPE |  |  |  |
| `RESULT_SAM_TOT` |  | 6 | 648‑653 | INTEGER_TYPE |  |  |  |
| `RESULT_SAK_1` |  | 5 | 654‑658 | INTEGER_TYPE |  |  |  |
| `RESULT_SAK_2` |  | 5 | 659‑663 | INTEGER_TYPE |  |  |  |
| `RESULT_SAK_3` |  | 5 | 664‑668 | INTEGER_TYPE |  |  |  |
| `RESULT_SAK_TOT` |  | 6 | 669‑674 | INTEGER_TYPE |  |  |  |
| `RESULT_TILB_1` |  | 4 | 675‑678 | INTEGER_TYPE |  |  |  |
| `RESULT_TILB_2` |  | 4 | 679‑682 | INTEGER_TYPE |  |  |  |
| `RESULT_TILB_3` |  | 4 | 683‑686 | INTEGER_TYPE |  |  |  |
| `RESULT_TILB_TOT` |  | 5 | 687‑691 | INTEGER_TYPE |  |  |  |
| `RESULT_FLY_1` |  | 5 | 692‑696 | INTEGER_TYPE |  |  |  |
| `RESULT_FLY_2` |  | 5 | 697‑701 | INTEGER_TYPE |  |  |  |
| `RESULT_FLY_3` |  | 5 | 702‑706 | INTEGER_TYPE |  |  |  |
| `RESULT_FLY_TOT` |  | 6 | 707‑712 | INTEGER_TYPE |  |  |  |
| `RESULT_TOT_1` |  | 6 | 713‑718 | INTEGER_TYPE |  |  |  |
| `RESULT_TOT_2` |  | 6 | 719‑724 | INTEGER_TYPE |  |  |  |
| `RESULT_TOT_3` |  | 6 | 725‑730 | INTEGER_TYPE |  |  |  |
| `RESULT_TOT_TOT` |  | 6 | 731‑736 | INTEGER_TYPE | ☑️ |  |  |
| `AVTALE_SEP_1` |  | 5 | 737‑741 | INTEGER_TYPE |  |  |  |
| `AVTALE_SEP_2` |  | 5 | 742‑746 | INTEGER_TYPE |  |  |  |
| `AVTALE_SEP_3` |  | 5 | 747‑751 | INTEGER_TYPE |  |  |  |
| `AVTALE_SEP_TOT` |  | 6 | 752‑757 | INTEGER_TYPE |  |  |  |
| `AVTALE_SAM_1` |  | 5 | 758‑762 | INTEGER_TYPE |  |  |  |
| `AVTALE_SAM_2` |  | 5 | 763‑767 | INTEGER_TYPE |  |  |  |
| `AVTALE_SAM_3` |  | 5 | 768‑772 | INTEGER_TYPE |  |  |  |
| `AVTALE_SAM_TOT` |  | 6 | 773‑778 | INTEGER_TYPE |  |  |  |
| `AVTALE_SAK_1` |  | 5 | 779‑783 | INTEGER_TYPE |  |  |  |
| `AVTALE_SAK_2` |  | 5 | 784‑788 | INTEGER_TYPE |  |  |  |
| `AVTALE_SAK_3` |  | 5 | 789‑793 | INTEGER_TYPE |  |  |  |
| `AVTALE_SAK_TOT` |  | 6 | 794‑799 | INTEGER_TYPE |  |  |  |
| `AVTALE_TILB_1` |  | 4 | 800‑803 | INTEGER_TYPE |  |  |  |
| `AVTALE_TILB_2` |  | 4 | 804‑807 | INTEGER_TYPE |  |  |  |
| `AVTALE_TILB_3` |  | 4 | 808‑811 | INTEGER_TYPE |  |  |  |
| `AVTALE_TILB_TOT` |  | 5 | 812‑816 | INTEGER_TYPE |  |  |  |
| `AVTALE_FLY_1` |  | 5 | 817‑821 | INTEGER_TYPE |  |  |  |
| `AVTALE_FLY_2` |  | 5 | 822‑826 | INTEGER_TYPE |  |  |  |
| `AVTALE_FLY_3` |  | 5 | 827‑831 | INTEGER_TYPE |  |  |  |
| `AVTALE_FLY_TOT` |  | 6 | 832‑837 | INTEGER_TYPE |  |  |  |
| `AVTALE_TOT_1` |  | 6 | 838‑843 | INTEGER_TYPE |  |  |  |
| `AVTALE_TOT_2` |  | 6 | 844‑849 | INTEGER_TYPE |  |  |  |
| `AVTALE_TOT_3` |  | 6 | 850‑855 | INTEGER_TYPE |  |  |  |
| `AVTALE_TOT_TOT` |  | 6 | 856‑861 | INTEGER_TYPE | ☑️ |  |  |
| `BEKYMR_SEP_1` |  | 5 | 862‑866 | INTEGER_TYPE |  |  |  |
| `BEKYMR_SEP_2` |  | 5 | 867‑871 | INTEGER_TYPE |  |  |  |
| `BEKYMR_SEP_TOT` |  | 6 | 872‑877 | INTEGER_TYPE |  |  |  |
| `BEKYMR_SAM_1` |  | 5 | 878‑882 | INTEGER_TYPE |  |  |  |
| `BEKYMR_SAM_2` |  | 5 | 883‑887 | INTEGER_TYPE |  |  |  |
| `BEKYMR_SAM_TOT` |  | 6 | 888‑893 | INTEGER_TYPE |  |  |  |
| `BEKYMR_SAK_1` |  | 5 | 894‑898 | INTEGER_TYPE |  |  |  |
| `BEKYMR_SAK_2` |  | 5 | 899‑903 | INTEGER_TYPE |  |  |  |
| `BEKYMR_SAK_TOT` |  | 6 | 904‑909 | INTEGER_TYPE |  |  |  |
| `BEKYMR_TILB_1` |  | 4 | 910‑913 | INTEGER_TYPE |  |  |  |
| `BEKYMR_TILB_2` |  | 4 | 914‑917 | INTEGER_TYPE |  |  |  |
| `BEKYMR_TILB_TOT` |  | 5 | 918‑922 | INTEGER_TYPE |  |  |  |
| `BEKYMR_FLY_1` |  | 5 | 923‑927 | INTEGER_TYPE |  |  |  |
| `BEKYMR_FLY_2` |  | 5 | 928‑932 | INTEGER_TYPE |  |  |  |
| `BEKYMR_FLY_TOT` |  | 6 | 933‑938 | INTEGER_TYPE |  |  |  |
| `BEKYMR_TOT_1` |  | 6 | 939‑944 | INTEGER_TYPE |  |  |  |
| `BEKYMR_TOT_2` |  | 6 | 945‑950 | INTEGER_TYPE |  |  |  |
| `BEKYMR_TOT_TOT` |  | 6 | 951‑956 | INTEGER_TYPE | ☑️ |  |  |
| `UTEN_OPPM_1` |  | 5 | 957‑961 | INTEGER_TYPE |  |  |  |
| `UTEN_OPPM_2` |  | 5 | 962‑966 | INTEGER_TYPE |  |  |  |
| `UTEN_OPPM_3` |  | 5 | 967‑971 | INTEGER_TYPE |  |  |  |
| `UTEN_OPPM_4` |  | 4 | 972‑975 | INTEGER_TYPE |  |  |  |
| `UTEN_OPPM_5` |  | 5 | 976‑980 | INTEGER_TYPE |  |  |  |
| `UTEN_OPPM_TOT` |  | 6 | 981‑986 | INTEGER_TYPE | ☑️ |  |  |
