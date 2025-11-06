# Filbeskrivelse: Økonomisk sosialhjelp, Kostra-skjema 11F (2022)

Filbeskrivelse for Økonomisk sosialhjelp for rapporteringsår 2022

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `KOMMUNE_NR` | Kommunenummer | 4 | 1‑4 | STRING_TYPE |  |  |  |
| `VERSION` | Rapporteringsår / versjon / oppgaveår | 2 | 5‑6 | STRING_TYPE |  |  |  |
| `BYDELSNR` | Bydelsnummer | 2 | 7‑8 | STRING_TYPE |  |  |  |
| `DISTRIKTSNR` | Distriktsnummer | 2 | 9‑10 | STRING_TYPE |  |  |  |
| `PERSON_JOURNALNR` | Journalnummer | 8 | 11‑18 | STRING_TYPE |  |  |  |
| `PERSON_FODSELSNR` | Hva er mottakerens fødselsnummer? | 11 | 19‑29 | STRING_TYPE |  |  |  |
| `PERSON_DUF` | Hva er mottakerens DUF-nummer? | 12 | 30‑41 | STRING_TYPE |  |  |  |
| `KJONN` | Hva er mottakerens kjønn? | 1 | 42‑42 | STRING_TYPE |  |  | `1`: Mann<br/>`2`: Kvinne |
| `EKTSTAT` | Hva er mottakerens sivilstand/sivilstatus ved siste kontakt? | 1 | 43‑43 | STRING_TYPE |  |  | `1`: Ugift<br/>`2`: Gift<br/>`3`: Samboer<br/>`4`: Skilt/separert<br/>`5`: Enke/enkemann |
| `BU18` | Har mottakeren barn under 18 år som mottakeren (eventuelt ektefelle/samboer) har forsørgerplikt for og som bor i husholdningen? | 1 | 44‑44 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `ANTBU18` | Hvis ja, hvor mange? | 2 | 45‑46 | INTEGER_TYPE |  |  |  |
| `VKLO` | Hva er mottakerens viktigste kilde til livsopphold ved siste kontakt? | 1 | 47‑47 | STRING_TYPE |  |  |  |
| `TRYGDESIT` | Oppgi trygd/pensjon som utgjør størst økonomisk verdi ved siste kontakt | 2 | 48‑49 | STRING_TYPE |  |  |  |
| `ARBSIT` | Hva er mottakerens viktigste arbeidssituasjon/livssituasjon ved siste kontakt? | 2 | 50‑51 | STRING_TYPE |  |  |  |
| `STMND_1` | Har mottakeren fått økonomisk stønad i januar? | 2 | 52‑53 | STRING_TYPE |  |  | `01`: Januar |
| `STMND_2` | Har mottakeren fått økonomisk stønad i februar? | 2 | 54‑55 | STRING_TYPE |  |  | `02`: Februar |
| `STMND_3` | Har mottakeren fått økonomisk stønad i mars? | 2 | 56‑57 | STRING_TYPE |  |  | `03`: Mars |
| `STMND_4` | Har mottakeren fått økonomisk stønad i april? | 2 | 58‑59 | STRING_TYPE |  |  | `04`: April |
| `STMND_5` | Har mottakeren fått økonomisk stønad i mai? | 2 | 60‑61 | STRING_TYPE |  |  | `05`: Mai |
| `STMND_6` | Har mottakeren fått økonomisk stønad i juni? | 2 | 62‑63 | STRING_TYPE |  |  | `06`: Juni |
| `STMND_7` | Har mottakeren fått økonomisk stønad i juli? | 2 | 64‑65 | STRING_TYPE |  |  | `07`: Juli |
| `STMND_8` | Har mottakeren fått økonomisk stønad i august? | 2 | 66‑67 | STRING_TYPE |  |  | `08`: August |
| `STMND_9` | Har mottakeren fått økonomisk stønad i september? | 2 | 68‑69 | STRING_TYPE |  |  | `09`: September |
| `STMND_10` | Har mottakeren fått økonomisk stønad i oktober? | 2 | 70‑71 | STRING_TYPE |  |  | `10`: Oktober |
| `STMND_11` | Har mottakeren fått økonomisk stønad i november? | 2 | 72‑73 | STRING_TYPE |  |  | `11`: November |
| `STMND_12` | Har mottakeren fått økonomisk stønad i desember? | 2 | 74‑75 | STRING_TYPE |  |  | `12`: Desember |
| `BIDRAG` | Bidrag | 7 | 76‑82 | INTEGER_TYPE |  |  |  |
| `LAAN` | Lån | 7 | 83‑89 | INTEGER_TYPE |  |  |  |
| `BIDRAG_JAN` | Hva fikk mottakeren utbetalt i den enkelte måned? Januar, bidrag | 7 | 90‑96 | INTEGER_TYPE |  |  |  |
| `LAAN_JAN` | Januar, lån | 7 | 97‑103 | INTEGER_TYPE |  |  |  |
| `BIDRAG_FEB` | Februar, bidrag | 7 | 104‑110 | INTEGER_TYPE |  |  |  |
| `LAAN_FEB` | Februar, lån | 7 | 111‑117 | INTEGER_TYPE |  |  |  |
| `BIDRAG_MARS` | Mars, bidrag | 7 | 118‑124 | INTEGER_TYPE |  |  |  |
| `LAAN_MARS` | Mars, lån | 7 | 125‑131 | INTEGER_TYPE |  |  |  |
| `BIDRAG_APRIL` | April, bidrag | 7 | 132‑138 | INTEGER_TYPE |  |  |  |
| `LAAN_APRIL` | April, lån | 7 | 139‑145 | INTEGER_TYPE |  |  |  |
| `BIDRAG_MAI` | Mai, bidrag | 7 | 146‑152 | INTEGER_TYPE |  |  |  |
| `LAAN_MAI` | Mai, lån | 7 | 153‑159 | INTEGER_TYPE |  |  |  |
| `BIDRAG_JUNI` | Juni, bidrag | 7 | 160‑166 | INTEGER_TYPE |  |  |  |
| `LAAN_JUNI` | Juni, lån | 7 | 167‑173 | INTEGER_TYPE |  |  |  |
| `BIDRAG_JULI` | Juli, bidrag | 7 | 174‑180 | INTEGER_TYPE |  |  |  |
| `LAAN_JULI` | Juli, lån | 7 | 181‑187 | INTEGER_TYPE |  |  |  |
| `BIDRAG_AUG` | August, bidrag | 7 | 188‑194 | INTEGER_TYPE |  |  |  |
| `LAAN_AUG` | August, lån | 7 | 195‑201 | INTEGER_TYPE |  |  |  |
| `BIDRAG_SEPT` | September, bidrag | 7 | 202‑208 | INTEGER_TYPE |  |  |  |
| `LAAN_SEPT` | September, lån | 7 | 209‑215 | INTEGER_TYPE |  |  |  |
| `BIDRAG_OKT` | Oktober, bidrag | 7 | 216‑222 | INTEGER_TYPE |  |  |  |
| `LAAN_OKT` | Oktober, lån | 7 | 223‑229 | INTEGER_TYPE |  |  |  |
| `BIDRAG_NOV` | November, bidrag | 7 | 230‑236 | INTEGER_TYPE |  |  |  |
| `LAAN_NOV` | November, lån | 7 | 237‑243 | INTEGER_TYPE |  |  |  |
| `BIDRAG_DES` | Desember, bidrag | 7 | 244‑250 | INTEGER_TYPE |  |  |  |
| `LAAN_DES` | Desember, lån | 7 | 251‑257 | INTEGER_TYPE |  |  |  |
| `GITT_OKONOMIRAD` | Er det gitt økonomisk rådgivning på nivå II (midlertidig betalingsudyktighet) eller III (varig betalingsudyktighet) i forbindelse med utbetaling? (Se NAVs veileder til bruk ved økonomisk rådgivning) | 1 | 258‑258 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `FAAT_INDIVIDUELL_PLAN` | Har mottakeren fått utarbeidet individuell plan (lov om sosiale tjenester i NAV § 28)? | 1 | 259‑259 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `SAKSBEHANDLER` | Saksbehandlernummer | 10 | 260‑269 | STRING_TYPE |  |  |  |
| `BOSIT` | Hva er mottakerens bosituasjon ved siste kontakt? | 1 | 270‑270 | STRING_TYPE |  |  | `1`: Leid privat bolig<br/>`2`: Leid kommunal bolig<br/>`3`: Eid bolig<br/>`4`: Uten bolig<br/>`5`: Annet<br/>`6`: Institusjon  |
| `VILKARSOSLOV` | Stilles det vilkår til mottakeren etter sosialtjenesteloven? | 1 | 271‑271 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `VILKARSAMEKT` | Stilles det vilkår til søkerens samboer/ektefelle etter sosialtjenesteloven? | 1 | 272‑272 | STRING_TYPE |  |  | `1`: Ja<br/>`2`: Nei |
| `UTBETDATO` | Oppgi utbetalingsvedtakets dato (DDMMÅÅ) | 6 | 273‑278 | DATE_TYPE |  | ddMMyy |  |
| `UTBETTOMDATO` | Oppgi utbetalingsvedtakets til og med dato (DDMMÅÅ) | 6 | 279‑284 | DATE_TYPE |  | ddMMyy |  |
| `VILKARARBEID` | Oppgi hvilke vilkår det stilles til mottakeren. Flere kryss mulig | 2 | 285‑286 | STRING_TYPE |  |  | `16`: Delta på arbeidstrening/arbeidspraksis |
| `VILKARKURS` |  | 2 | 287‑288 | STRING_TYPE |  |  | `17`: Delta på arbeidsrettede kurs, opplæring eller jobbsøkingskurs |
| `VILKARUTD` |  | 2 | 289‑290 | STRING_TYPE |  |  | `04`: Benytte rett til skole |
| `VILKARJOBBLOG` |  | 2 | 291‑292 | STRING_TYPE |  |  | `06`: Registrere seg som arbeidssøker/føre jobblogg |
| `VILKARJOBBTILB` |  | 2 | 293‑294 | STRING_TYPE |  |  | `07`: Ta imot et konkret jobbtilbud |
| `VILKARSAMT` |  | 2 | 295‑296 | STRING_TYPE |  |  | `08`: Møte til veiledningssamtaler |
| `VILKAROKRETT` |  | 2 | 297‑298 | STRING_TYPE |  |  | `10`: Gjøre gjeldende andre økonomiske rettigheter |
| `VILKARLIVSH` |  | 2 | 299‑300 | STRING_TYPE |  |  | `18`: Realisere formuesgoder/ redusere boutgifter |
| `VILKARHELSE` |  | 2 | 301‑302 | STRING_TYPE |  |  | `14`: Oppsøke helsetjenester/ lege |
| `VILKARANNET` |  | 2 | 303‑304 | STRING_TYPE |  |  | `15`: Annet |
| `VILKARDIGPLAN` | Bruke og følge opp digital aktivitetsplan | 2 | 305‑306 | STRING_TYPE |  |  | `19`: Bruke og følge opp digital aktivitetsplan |
| `VEDTAKDATO` | Unntak for mottaker under 30 år. Begrunnelse for første unntak fra sosialtjenestelovens § 20a for mottaker under 30 år. Oppgi vedtaksdato (DDMMÅÅ) | 6 | 307‑312 | DATE_TYPE |  | ddMMyy |  |
| `VEDTAKARB` | Begrunnelse for første unntak fra sosialtjenestelovens § 20a | 2 | 313‑314 | STRING_TYPE |  |  | `01`: Mottaker er i arbeid |
| `VEDTAKAKT` |  | 2 | 315‑316 | STRING_TYPE |  |  | `02`: Mottaker er allerede i aktivitet knyttet til mottak av statlig eller annen kommunal ytelse |
| `VEDTAKHELSE` |  | 2 | 317‑318 | STRING_TYPE |  |  | `03`: Mottakers helsemessige eller sosiale situasjon hindrer deltakelse i aktivitet |
| `VEDTAKGRUNN` |  | 2 | 319‑320 | STRING_TYPE |  |  | `04`: Andre tungtveiende grunner taler mot at det stilles vilkår om aktivitet |
| `SANKSJONRED` | Sanksjonering av mottaker. Sanksjon i løpet av året som følge av brudd på vilkår etter sosialtjenestelovens § 20a | 2 | 321‑322 | STRING_TYPE |  |  | `01`: Redusert stønad |
| `SANKSJONANDRE` |  | 2 | 323‑324 | STRING_TYPE |  |  | `02`: Andre konsekvenser |
