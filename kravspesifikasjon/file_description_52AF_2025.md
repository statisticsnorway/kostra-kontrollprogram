# Filbeskrivelse: 52AF Familievernsaker, klientrapportering (2025)

Filbeskrivelse for 52AF Familievernsaker, klientrapportering for 2025

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `REGION_NR_A` | Familievernregionnummer | 6 | 1‑6 | STRING_TYPE | ☑️ |  | `667200`: Region øst<br/>`667300`: Region sør<br/>`667400`: Region vest<br/>`667500`: Region Midt-Norge<br/>`667600`: Region nord |
| `KONTOR_NR_A` | Familievernkontornummer | 3 | 7‑9 | STRING_TYPE | ☑️ |  | `017`: Familievernkontoret Østfold<br/>`023`: Familievernkontoret Asker Bærum<br/>`024`: Familievernkontoret Follo<br/>`025`: Familievernkontoret Nedre Romerike<br/>`027`: Familievernkontoret Øvre Romerike Glåmdal<br/>`030`: Familiekontoret Oslo Nord<br/>`037`: Familievernkontoret Homansbyen<br/>`038`: Familievernkontoret Enerhaugen<br/>`039`: Familievernkontoret Chrisiania<br/>`046`: Familievernkontoret Innlandet vest<br/>`047`: Familievernkontoret Innlandet øst<br/>`052`: Familievernkontoret Otta SKF<br/>`061`: Familievernkontoret i Buskerud<br/>`065`: Familievernkontoret Ringerike - Hallingdal<br/>`071`: Familievernkontoret i Vestfold<br/>`073`: Familievernkontoret i Søndre Vestfold<br/>`081`: Grenland familievernkontor<br/>`082`: Familiekontoret Øvre Telemark<br/>`091`: Familievernkontoret i Arendal<br/>`101`: Familiekontoret i Vest-Agder<br/>`111`: Familievernkontoret i Sør-Rogaland<br/>`112`: Familievernkontoret Haugalandet<br/>`125`: Bergen og omland familiekontor<br/>`127`: Bjørgvin familiekontor<br/>`141`: Familiekontora for Sunnfjord og Sogn<br/>`142`: Nordfjord familiekontor<br/>`151`: Familievernkontoret i Romsdal<br/>`152`: Familievernkontoret Sunnmøre<br/>`153`: Familievernkontoret Nordmøre<br/>`162`: Familiervernkontoret i Trondheim<br/>`171`: Familievernkontoret Innherred<br/>`172`: Familievernkontoret Namdalen<br/>`181`: Bodø familievernkontor<br/>`183`: Familievernkontoret i Mo i Rana<br/>`184`: Familievernkontoret i Mosjøen<br/>`185`: Lofoten og Vesterålen Familievernkontor<br/>`192`: Tromsø familievernkontor<br/>`193`: Finnsnes familievernkontor<br/>`194`: Harstad og Narvik familievernkontor<br/>`202`: Familievernkontoret i Øst-Finnmark<br/>`203`: Indre Finnmark familievernkontor - Sis Finnmarkku Bearassuojalanguovddas<br/>`205`: Alta og Hammerfest familievernkontor |
| `JOURNAL_NR_A` | Journalnummer | 9 | 10‑18 | STRING_TYPE | ☑️ |  |  |
| `HENV_DATO_A` | Henvendelsesdato (ddmmåååå) | 8 | 19‑26 | DATE_TYPE | ☑️ | ddMMyyyy |  |
| `KONTAKT_TIDL_A` | Primærklientens tidligere klientforhold ved kontoret | 1 | 27‑27 | STRING_TYPE | ☑️ |  | `1`: under 6 md siden<br/>`2`: mellom 6 md og 3 år siden<br/>`3`: 3 år eller mer siden<br/>`4`: har ikke vært i kontakt med familievernet tidligere |
| `HENV_GRUNN_A` | Primærklientens viktigste begrunnelse for å ta kontakt | 1 | 28‑28 | STRING_TYPE | ☑️ |  | `1`: Parforholdet<br/>`2`: Foreldresamarbeid/- veiledning<br/>`3`: Andre eller sammensatte problemer i familien<br/>`4`: Hjelp til barn og ungdom |
| `PRIMK_KJONN_A` | Primærklienten kjønn | 1 | 29‑29 | STRING_TYPE | ☑️ |  | `1`: Mann/gutt<br/>`2`: Kvinne/jente |
| `PRIMK_FODT_A` | Primærklientens fødselsår (fire siffer) | 4 | 30‑33 | INTEGER_TYPE |  |  |  |
| `PRIMK_SIVILS_A` | Primærklientens "samlivsstatus" ved sakens opprettelse | 1 | 34‑34 | STRING_TYPE | ☑️ |  | `1`: Gift<br/>`2`: Registrert partner<br/>`3`: Samboer<br/>`4`: Lever ikke i samliv |
| `FORMELL_SIVILS_A` | Om samboer eller Lever ikke i samliv skal det krysses av for sivilstand | 1 | 35‑35 | STRING_TYPE |  |  | `1`: Ugift<br/>`2`: Gift<br/>`3`: Registrert partner<br/>`4`: Separert / separert partner<br/>`5`: Skilt / skilt partner<br/>`6`: Enke / enkemann / gjenlevende partner |
| `PRIMK_SAMBO_A` | Ved henvendelsestidspunktet bor primærklienten sammen med | 1 | 36‑36 | STRING_TYPE | ☑️ |  | `1`: Partner (og eventuelt barn)<br/>`2`: Barn<br/>`3`: Foreldre / Andre omsorgspersoner<br/>`4`: Andre<br/>`5`: Ikke sammen med andre |
| `PRIMK_ARBSIT_A` | Primærklientens viktigste tilknyting til utdanning og arbeidsliv | 1 | 37‑37 | STRING_TYPE | ☑️ |  | `1`: Arbeid heltid<br/>`2`: Arbeid deltid<br/>`3`: Arbeidssøker<br/>`4`: Under utdanning<br/>`5`: Fødselspermisjon / Fedrekvote<br/>`6`: Annen inntekt fre NAV<br/>`7`: Uten inntekt |
| `PRIMK_VSRELASJ_A` | Primærklientens relasjon til viktigste deltaker i saken | 1 | 38‑38 | STRING_TYPE |  |  | `1`: Partner<br/>`2`: Ekspartner<br/>`3`: Forelder<br/>`4`: Sønn/datter under 18 år<br/>`5`: Sønn/datter 18 år eller eldre<br/>`6`: Øvrig familie<br/>`7`: Venn<br/>`8`: Annet |
| `PART_LENGDE_A` | Om Partnere, varighet parforhold | 1 | 39‑39 | STRING_TYPE |  |  | `1`: Har ikke bodd sammen<br/>`2`: Under 2 år<br/>`3`: 2 - 4 år<br/>`4`: 5 - 9 år<br/>`5`: 10 - 19 år<br/>`6`: 20 år eller mer |
| `EKSPART_LENGDE_A` | Om Ekspartnere, tid siden brudd | 1 | 40‑40 | STRING_TYPE |  |  | `1`: Har ikke bodd sammen<br/>`2`: Under 2 år<br/>`3`: 2 - 4 år<br/>`4`: 5 - 9 år<br/>`5`: 10 - 19 år<br/>`6`: 20 år eller mer |
| `EKSPART_VARIGH_A` | Om Ekspartnere, varighet tidligere parforhold | 1 | 41‑41 | STRING_TYPE |  |  | `1`: Har ikke bodd sammen<br/>`2`: Under 2 år<br/>`3`: 2 - 4 år<br/>`4`: 5 - 9 år<br/>`5`: 10 - 19 år<br/>`6`: 20 år eller mer |
| `PRIMKREL_PART_A` | Primærklientens viktigste relasjon til de andre deltakerne i saken hvis flere enn to deltakere (flere kryss mulig) | 1 | 42‑42 | STRING_TYPE |  |  | `1`: Partner |
| `PRIMKREL_EKSPART_A` |  | 1 | 43‑43 | STRING_TYPE |  |  | `1`: Ekspartner |
| `PRIMKREL_FORELD_A` |  | 1 | 44‑44 | STRING_TYPE |  |  | `1`: Forelder |
| `PRIMKREL_BU18_A` |  | 1 | 45‑45 | STRING_TYPE |  |  | `1`: Sønn/datter (under 18 år) |
| `PRIMKREL_B18_A` |  | 1 | 46‑46 | STRING_TYPE |  |  | `1`: Sønn/datter (18 år eller eldre) |
| `PRIMKREL_OVRIG_A` |  | 1 | 47‑47 | STRING_TYPE |  |  | `1`: Øvrig familie |
| `PRIMKREL_VENN_A` |  | 1 | 48‑48 | STRING_TYPE |  |  | `1`: Venner |
| `PRIMKREL_ANDRE_A` |  | 1 | 49‑49 | STRING_TYPE |  |  | `1`: Annet |
| `FORSTE_SAMT_A` | Første behandlingssamtale fant sted (ddmmåååå) | 8 | 50‑57 | DATE_TYPE | ☑️ | ddMMyyyy |  |
| `SAMT_FORHOLD_A` | Dersom tiden fra hendvendelse til første behandlingssamtale overstiger 4 uker, hva skyldes dette? | 1 | 58‑58 | STRING_TYPE |  |  | `1`: Forhold hos kontoret<br/>`2`: Forhold hos klient/klientene |
| `TEMA_PARREL_A` | Hvilke områder har vært arbeidet med siden saken ble opprettet? (flere kryss mulig) | 1 | 59‑59 | STRING_TYPE |  |  | `1`: Styrke parforholdet |
| `TEMA_AVKLAR_A` |  | 1 | 60‑60 | STRING_TYPE |  |  | `1`: Avklare/avslutte parforholdet |
| `TEMA_SAMLBRUDD_A` |  | 1 | 61‑61 | STRING_TYPE |  |  | `1`: Samlivsbrudd i familien |
| `TEMA_SAMSPILL_A` |  | 1 | 62‑62 | STRING_TYPE |  |  | `1`: Samspillsvansker |
| `TEMA_BARNSIT_A` |  | 1 | 63‑63 | STRING_TYPE |  |  | `1`: Barnets opplevelse av sin livssituasjon |
| `TEMA_BARNFOR_A` |  | 1 | 64‑64 | STRING_TYPE |  |  | `1`: Barnets situasjon i foreldrenes konflikt |
| `TEMA_BOSTED_A` |  | 1 | 65‑65 | STRING_TYPE |  |  | `1`: bostedsavklaring/samvær |
| `TEMA_FORELDRE_A` |  | 1 | 66‑66 | STRING_TYPE |  |  | `1`: foreldrerollen |
| `TEMA_FORBARN_A` |  | 1 | 67‑67 | STRING_TYPE |  |  | `1`: foreldre-barn-relasjonen |
| `TEMA_FLERGEN_A` |  | 1 | 68‑68 | STRING_TYPE |  |  | `1`: flergenerasjonsproblematikk |
| `TEMA_SAMBARN_A` |  | 1 | 69‑69 | STRING_TYPE |  |  | `1`: samarb. om felles barn (foreldre bor ikke sammen) |
| `TEMA_SÆRBARN_A` |  | 1 | 70‑70 | STRING_TYPE |  |  | `1`: særkullsbarn og/eller ny familie |
| `TEMA_KULTUR_A` |  | 1 | 71‑71 | STRING_TYPE |  |  | `1`: kultur-/minoritetsspørsmål |
| `TEMA_TVANG_A` |  | 1 | 72‑72 | STRING_TYPE |  |  | `1`: tvangsekteskap |
| `TEMA_RUS_A` |  | 1 | 73‑73 | STRING_TYPE |  |  | `1`: bruk av rusmidler |
| `TEMA_SYKD_A` |  | 1 | 74‑74 | STRING_TYPE |  |  | `1`: sykdom / nedsatt funksjonsevne |
| `TEMA_VOLD_A` |  | 1 | 75‑75 | STRING_TYPE |  |  | `1`: fysisk / psykisk vold / seksuelt misbruk |
| `TEMA_ALVH_A` |  | 1 | 76‑76 | STRING_TYPE |  |  | `1`: annen alvorlig hendelse |
| `HOVEDF_BEHAND_A` | Hva har vært hovedformen på behandlingstilbudet siden saken ble opprettet? | 1 | 77‑77 | STRING_TYPE | ☑️ |  | `1`: Parsamtale<br/>`2`: Foreldresamtale<br/>`3`: Familiesamtale<br/>`4`: Individualsamtale |
| `BEKYMR_MELD_A` | Bekymringsmelding sendt barnevernet | 1 | 78‑78 | STRING_TYPE | ☑️ |  | `1`: Ja<br/>`2`: Nei |
| `DELT_PARTNER_A` | Partner | 1 | 79‑79 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `DELT_EKSPART_A` | Ekspartner | 1 | 80‑80 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `DELT_BARNU18_A` | Sønn/datter under 18 år | 1 | 81‑81 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `DELT_BARNO18_A` | Sønn/datter 18 år eller eldre | 1 | 82‑82 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `DELT_FORELDRE_A` | Foreldre | 1 | 83‑83 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `DELT_OVRFAM_A` | Øvrig familie | 1 | 84‑84 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `DELT_VENN_A` | Venner | 1 | 85‑85 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `DELT_ANDR_A` | Annet | 1 | 86‑86 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `SAMT_PRIMK_A` | Primærkrient | 2 | 87‑88 | INTEGER_TYPE |  |  |  |
| `SAMT_PARTNER_A` | Partner | 2 | 89‑90 | INTEGER_TYPE |  |  |  |
| `SAMT_EKSPART_A` | Ekspartner | 2 | 91‑92 | INTEGER_TYPE |  |  |  |
| `SAMT_BARNU18_A` | Sønn/datter under 18 år | 2 | 93‑94 | INTEGER_TYPE |  |  |  |
| `SAMT_BARNO18_A` | Sønn/datter 18 år eller eldre | 2 | 95‑96 | INTEGER_TYPE |  |  |  |
| `SAMT_FORELDRE_A` | Foreldre | 2 | 97‑98 | INTEGER_TYPE |  |  |  |
| `SAMT_OVRFAM_A` | Øvrig familie | 2 | 99‑100 | INTEGER_TYPE |  |  |  |
| `SAMT_VENN_A` | Venn | 2 | 101‑102 | INTEGER_TYPE |  |  |  |
| `SAMT_ANDRE_A` | Annet | 2 | 103‑104 | INTEGER_TYPE |  |  |  |
| `ANTSAMT_HOVEDT_A` | Hovedterapaut | 3 | 105‑107 | INTEGER_TYPE |  |  |  |
| `ANTSAMT_ANDREANS_A` | Andre utenom hovedterapeut (Co-terapaut) | 3 | 108‑110 | INTEGER_TYPE |  |  |  |
| `ANTSAMT_IARET_A` | I løpet av året | 3 | 111‑113 | INTEGER_TYPE | ☑️ |  |  |
| `ANTSAMT_OPPR_A` | Siden opprettelsen | 3 | 114‑116 | INTEGER_TYPE | ☑️ |  |  |
| `TLFSAMT_IARET_A` | I løpet av året | 3 | 117‑119 | INTEGER_TYPE |  |  |  |
| `TLFSAMT_OPPR_A` | Siden opprettelsen | 3 | 120‑122 | INTEGER_TYPE |  |  |  |
| `TIMER_IARET_A` | I løpet av året | 3 | 123‑125 | INTEGER_TYPE | ☑️ |  |  |
| `TIMER_OPPR_A` | Siden opprettelsen | 3 | 126‑128 | INTEGER_TYPE | ☑️ |  |  |
| `TOLK_A` | Tolk benyttet i forbindelse med én eller flere behandlingssamtaler siden saken ble opprettet | 1 | 129‑129 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `SAMARB_INGEN_A` |  | 1 | 130‑130 | STRING_TYPE |  |  | `1`: Ikke samarbeid med med annen instans |
| `SAMARB_LEGE_A` |  | 1 | 131‑131 | STRING_TYPE |  |  | `1`: Fastlege |
| `SAMARB_HELSE_A` |  | 1 | 132‑132 | STRING_TYPE |  |  | `1`: Helsestasjon / Familiesenter |
| `SAMARB_PSYKH_A` |  | 1 | 133‑133 | STRING_TYPE |  |  | `1`: Psykisk helsevern |
| `SAMARB_JURIST_A` |  | 1 | 134‑134 | STRING_TYPE |  |  | `1`: Jurist |
| `SAMARB_KRISES_A` |  | 1 | 135‑135 | STRING_TYPE |  |  | `1`: Krisesenter |
| `SAMARB_SKOLE_A` |  | 1 | 136‑136 | STRING_TYPE |  |  | `1`: Skole/PP-tjeneste |
| `SAMARB_SOS_A` |  | 1 | 137‑137 | STRING_TYPE |  |  | `1`: NAV |
| `SAMARB_KOMMB_A` |  | 1 | 138‑138 | STRING_TYPE |  |  | `1`: Kommunalt barnevern |
| `SAMARB_STATB_A` |  | 1 | 139‑139 | STRING_TYPE |  |  | `1`: Statlig barnevern |
| `SAMARB_ANDRE_A` |  | 1 | 140‑140 | STRING_TYPE |  |  | `1`: Annet |
| `STATUS_ARETSSL_A` | Status for saken ved årets slutt | 1 | 141‑141 | STRING_TYPE |  |  | `1`: Avsluttet etter avtale med klient<br/>`2`: Klient uteblitt<br/>`3`: Saken ikke avsluttet i inneværende år |
| `HOVEDTEMA_A` | Sakens hovedtema (fylles ut når saken avsluttes) | 2 | 142‑143 | STRING_TYPE |  |  | `01`: styrke parforholdet<br/>`02`: avklare/avslutte parforholdet<br/>`03`: samlivsbrudd i familien<br/>`04`: samspillvansker<br/>`05`: barnets opplevelse av sin livssituasjon<br/>`06`: barnets situasjon i foreldrenes konflikt<br/>`07`: bostedsavklaring/ samvær<br/>`08`: foreldrerollen<br/>`09`: foreldre-barn-relasjonen<br/>`10`: flergenerasjons- problematikk<br/>`11`: samarb. om felles barn (foreldre bor ikke sammen)<br/>`12`: særkullsbarn og/eller ny familie<br/>`13`: kultur-/minoritetsspørsmål<br/>`14`: tvangsekteskap<br/>`15`: bruk av rusmidler<br/>`16`: sykdom / nedsatt funksjonsevne<br/>`17`: fysisk / psykisk vold / seksuelt misbruk<br/>`18`: annen alvorlig hendelse |
| `DATO_AVSL_A` | Dato for avslutning av saken (ddmmåååå) | 8 | 144‑151 | DATE_TYPE |  | ddMMyyyy |  |
