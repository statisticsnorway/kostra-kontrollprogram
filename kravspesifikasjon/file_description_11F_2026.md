# Filbeskrivelse: Økonomisk sosialhjelp, Kostra-skjema 11F (2026)

Filbeskrivelse for Økonomisk sosialhjelp for rapporteringsår 2026

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `KOMMUNE_NR` | Kommunenummer | 4 | 1‑4 | STRING_TYPE | ☑️ |  |  |
| `VERSION` | Rapporteringsår / versjon / oppgaveår | 2 | 5‑6 | STRING_TYPE | ☑️ |  |  |
| `BYDELSNR` | Bydelsnummer | 2 | 7‑8 | STRING_TYPE | ☑️ |  |  |
| `FODSELSDATO` | Fødselsdato (DDMMÅÅÅÅ) | 8 | 9‑16 | DATE_TYPE | ☑️ | ddMMyyyy |  |
| `PERSON_JOURNALNR` | Journalnummer | 8 | 17‑24 | STRING_TYPE | ☑️ |  |  |
| `PERSON_FODSELSNR` | Hva er mottakerens fødselsnummer? | 11 | 25‑35 | STRING_TYPE |  |  |  |
| `PERSON_DUF` | Hva er mottakerens DUF-nummer? | 12 | 36‑47 | STRING_TYPE |  |  |  |
| `KJONN` | Hva er mottakerens kjønn? | 1 | 48‑48 | STRING_TYPE | ☑️ |  | `1`: Mann<br/>`2`: Kvinne |
| `EKTSTAT` | Hva er mottakerens sivilstand/sivilstatus ved siste kontakt? | 1 | 49‑49 | STRING_TYPE | ☑️ |  | `1`: Ugift<br/>`2`: Gift<br/>`3`: Samboer<br/>`4`: Skilt/separert<br/>`5`: Enke/enkemann |
| `BU18` | Har mottakeren barn under 18 år som mottakeren (eventuelt ektefelle/samboer) har forsørgerplikt for og som bor i husholdningen? | 1 | 50‑50 | STRING_TYPE | ☑️ |  | `1`: Ja<br/>`2`: Nei |
| `ANTBU18` | Hvis ja, hvor mange? | 2 | 51‑52 | INTEGER_TYPE |  |  |  |
| `VKLO` | Hva er mottakerens viktigste kilde til livsopphold ved siste kontakt? | 1 | 53‑53 | STRING_TYPE | ☑️ |  | `1`: Arbeidsinntekt<br/>`2`: Kursstønad/lønn i arbeidsmarkedstiltak<br/>`3`: Trygd/stønad<br/>`4`: Stipend/lån<br/>`5`: Sosialhjelp<br/>`6`: Introduksjonsstøtte<br/>`7`: Ektefelle/samboers inntekt<br/>`8`: Kvalifiseringsstønad<br/>`9`: Annen inntekt |
| `TRYGDESIT` | Oppgi trygd/pensjon som utgjør størst økonomisk verdi ved siste kontakt | 2 | 54‑55 | STRING_TYPE | ☑️ |  | `01`: Sykepenger<br/>`02`: Dagpenger<br/>`04`: Uføretrygd<br/>`05`: Overgangsstønad<br/>`06`: Omstillingsstønad/tidl. Etterlattepensjon<br/>`07`: Alderspensjon<br/>`09`: Supplerende stønad (kort botid)<br/>`10`: Annen trygd<br/>`11`: Arbeidsavklaringspenger<br/>`12`: Har ingen trygd/pensjon<br/>`13`: Barnetrygd |
| `ARBSIT` | Hva er mottakerens viktigste arbeidssituasjon/livssituasjon ved siste kontakt? | 2 | 56‑57 | STRING_TYPE | ☑️ |  | `01`: Arbeid, heltid<br/>`02`: Arbeid, deltid<br/>`03`: Under utdanning<br/>`04`: Ikke arbeidssøker<br/>`05`: Arbeidsmarkedstiltak (statlig)<br/>`06`: Kommunalt tiltak<br/>`07`: Registrert arbeidsledig<br/>`08`: Arbeidsledig, men ikke registrert hos NAV<br/>`09`: Introduksjonsordning<br/>`10`: Kvalifiseringsprogram |
| `STMND_1` | Har mottakeren fått økonomisk stønad i januar? | 2 | 58‑59 | STRING_TYPE |  |  | `01`: Januar |
| `STMND_2` | Har mottakeren fått økonomisk stønad i februar? | 2 | 60‑61 | STRING_TYPE |  |  | `02`: Februar |
| `STMND_3` | Har mottakeren fått økonomisk stønad i mars? | 2 | 62‑63 | STRING_TYPE |  |  | `03`: Mars |
| `STMND_4` | Har mottakeren fått økonomisk stønad i april? | 2 | 64‑65 | STRING_TYPE |  |  | `04`: April |
| `STMND_5` | Har mottakeren fått økonomisk stønad i mai? | 2 | 66‑67 | STRING_TYPE |  |  | `05`: Mai |
| `STMND_6` | Har mottakeren fått økonomisk stønad i juni? | 2 | 68‑69 | STRING_TYPE |  |  | `06`: Juni |
| `STMND_7` | Har mottakeren fått økonomisk stønad i juli? | 2 | 70‑71 | STRING_TYPE |  |  | `07`: Juli |
| `STMND_8` | Har mottakeren fått økonomisk stønad i august? | 2 | 72‑73 | STRING_TYPE |  |  | `08`: August |
| `STMND_9` | Har mottakeren fått økonomisk stønad i september? | 2 | 74‑75 | STRING_TYPE |  |  | `09`: September |
| `STMND_10` | Har mottakeren fått økonomisk stønad i oktober? | 2 | 76‑77 | STRING_TYPE |  |  | `10`: Oktober |
| `STMND_11` | Har mottakeren fått økonomisk stønad i november? | 2 | 78‑79 | STRING_TYPE |  |  | `11`: November |
| `STMND_12` | Har mottakeren fått økonomisk stønad i desember? | 2 | 80‑81 | STRING_TYPE |  |  | `12`: Desember |
| `BIDRAG` | Hva fikk mottakeren utbetalt i bidrag i løpet av året? | 7 | 82‑88 | INTEGER_TYPE | ☑️ |  |  |
| `LAAN` | Hva fikk mottakeren utbetalt i lån i løpet av året? | 7 | 89‑95 | INTEGER_TYPE | ☑️ |  |  |
| `BIDRAG_JAN` | Hva fikk mottakeren utbetalt i den enkelte måned? Januar, bidrag | 7 | 96‑102 | INTEGER_TYPE |  |  |  |
| `LAAN_JAN` | Januar, lån | 7 | 103‑109 | INTEGER_TYPE |  |  |  |
| `BIDRAG_FEB` | Februar, bidrag | 7 | 110‑116 | INTEGER_TYPE |  |  |  |
| `LAAN_FEB` | Februar, lån | 7 | 117‑123 | INTEGER_TYPE |  |  |  |
| `BIDRAG_MARS` | Mars, bidrag | 7 | 124‑130 | INTEGER_TYPE |  |  |  |
| `LAAN_MARS` | Mars, lån | 7 | 131‑137 | INTEGER_TYPE |  |  |  |
| `BIDRAG_APRIL` | April, bidrag | 7 | 138‑144 | INTEGER_TYPE |  |  |  |
| `LAAN_APRIL` | April, lån | 7 | 145‑151 | INTEGER_TYPE |  |  |  |
| `BIDRAG_MAI` | Mai, bidrag | 7 | 152‑158 | INTEGER_TYPE |  |  |  |
| `LAAN_MAI` | Mai, lån | 7 | 159‑165 | INTEGER_TYPE |  |  |  |
| `BIDRAG_JUNI` | Juni, bidrag | 7 | 166‑172 | INTEGER_TYPE |  |  |  |
| `LAAN_JUNI` | Juni, lån | 7 | 173‑179 | INTEGER_TYPE |  |  |  |
| `BIDRAG_JULI` | Juli, bidrag | 7 | 180‑186 | INTEGER_TYPE |  |  |  |
| `LAAN_JULI` | Juli, lån | 7 | 187‑193 | INTEGER_TYPE |  |  |  |
| `BIDRAG_AUG` | August, bidrag | 7 | 194‑200 | INTEGER_TYPE |  |  |  |
| `LAAN_AUG` | August, lån | 7 | 201‑207 | INTEGER_TYPE |  |  |  |
| `BIDRAG_SEPT` | September, bidrag | 7 | 208‑214 | INTEGER_TYPE |  |  |  |
| `LAAN_SEPT` | September, lån | 7 | 215‑221 | INTEGER_TYPE |  |  |  |
| `BIDRAG_OKT` | Oktober, bidrag | 7 | 222‑228 | INTEGER_TYPE |  |  |  |
| `LAAN_OKT` | Oktober, lån | 7 | 229‑235 | INTEGER_TYPE |  |  |  |
| `BIDRAG_NOV` | November, bidrag | 7 | 236‑242 | INTEGER_TYPE |  |  |  |
| `LAAN_NOV` | November, lån | 7 | 243‑249 | INTEGER_TYPE |  |  |  |
| `BIDRAG_DES` | Desember, bidrag | 7 | 250‑256 | INTEGER_TYPE |  |  |  |
| `LAAN_DES` | Desember, lån | 7 | 257‑263 | INTEGER_TYPE |  |  |  |
| `GITT_OKONOMIRAD` | Er det gitt økonomisk rådgivning på nivå II (midlertidig betalingsudyktighet) eller III (varig betalingsudyktighet) i forbindelse med utbetaling? (Se NAVs veileder til bruk ved økonomisk rådgivning) | 1 | 264‑264 | STRING_TYPE | ☑️ |  | `1`: Ja<br/>`2`: Nei |
| `FAAT_INDIVIDUELL_PLAN` | Har mottakeren fått utarbeidet individuell plan (lov om sosiale tjenester i NAV § 28)? | 1 | 265‑265 | STRING_TYPE | ☑️ |  | `1`: Ja<br/>`2`: Nei |
| `SAKSBEHANDLER` | Saksbehandlernummer | 10 | 266‑275 | STRING_TYPE | ☑️ |  |  |
| `BOSIT` | Hva er mottakerens bosituasjon ved siste kontakt? | 1 | 276‑276 | STRING_TYPE | ☑️ |  | `1`: Leid privat bolig<br/>`2`: Leid kommunal bolig<br/>`3`: Eid bolig<br/>`4`: Uten bolig<br/>`5`: Annet<br/>`6`: Institusjon  |
| `VILKARSOSLOV` | I forbindelse med vedtak om utbetaling av stønad, stilles det vilkår til mottakeren etter sosialtjenesteloven? | 1 | 277‑277 | STRING_TYPE | ☑️ |  | `1`: Ja<br/>`2`: Nei |
| `VILKARSAMEKT` | I forbindelse med vedtak om utbetaling av stønad, stilles det vilkår til søkerens samboer/ektefelle etter sosialtjenesteloven? | 1 | 278‑278 | STRING_TYPE | ☑️ |  | `1`: Ja<br/>`2`: Nei |
| `UTBETDATO` | Hvis "ja" på spørsmålet "Stilles det vilkår til mottakeren etter sosialtjenesteloven?" Oppgi utbetalingsvedtakets dato (DDMMÅÅÅÅ) | 8 | 279‑286 | DATE_TYPE |  | ddMMyyyy |  |
| `UTBETTOMDATO` | Hvis "ja" på spørsmålet "Stilles det vilkår til mottakeren etter sosialtjenesteloven?" Oppgi utbetalingsvedtakets til og med dato (DDMMÅÅÅÅ) | 8 | 287‑294 | DATE_TYPE |  | ddMMyyyy |  |
| `VILKARARBEID` | Oppgi hvilke vilkår det stilles til mottakeren. Flere kryss mulig | 2 | 295‑296 | STRING_TYPE |  |  | `16`: Delta på arbeidstrening/arbeidspraksis |
| `VILKARKURS` |  | 2 | 297‑298 | STRING_TYPE |  |  | `17`: Delta på arbeidsrettede kurs, opplæring eller jobbsøkingskurs |
| `VILKARUTD` |  | 2 | 299‑300 | STRING_TYPE |  |  | `04`: Benytte rett til skole |
| `VILKARJOBBLOG` |  | 2 | 301‑302 | STRING_TYPE |  |  | `06`: Registrere seg som arbeidssøker/føre jobblogg |
| `VILKARJOBBTILB` |  | 2 | 303‑304 | STRING_TYPE |  |  | `07`: Ta imot et konkret jobbtilbud |
| `VILKARSAMT` |  | 2 | 305‑306 | STRING_TYPE |  |  | `08`: Møte til veiledningssamtaler |
| `VILKAROKRETT` |  | 2 | 307‑308 | STRING_TYPE |  |  | `10`: Gjøre gjeldende andre økonomiske rettigheter |
| `VILKARLIVSH` |  | 2 | 309‑310 | STRING_TYPE |  |  | `18`: Realisere formuesgoder/ redusere boutgifter |
| `VILKARHELSE` |  | 2 | 311‑312 | STRING_TYPE |  |  | `14`: Oppsøke helsetjenester/ lege |
| `VILKARANNET` |  | 2 | 313‑314 | STRING_TYPE |  |  | `15`: Annet |
| `VILKARDIGPLAN` | Bruke og følge opp digital aktivitetsplan | 2 | 315‑316 | STRING_TYPE |  |  | `19`: Bruke og følge opp digital aktivitetsplan |
| `VILKARNORSKSAMISKOPPL` | Norsk- eller samiskopplæring | 2 | 317‑318 | STRING_TYPE |  |  | `20`: Norsk- eller samiskopplæring |
| `VEDTAKDATO` | Unntak for mottaker under 30 år. Begrunnelse for første unntak fra sosialtjenestelovens § 20a for mottaker under 30 år. Oppgi vedtaksdato (DDMMÅÅÅÅ) | 8 | 319‑326 | DATE_TYPE |  | ddMMyyyy |  |
| `VEDTAKARB` | Begrunnelse for første unntak fra sosialtjenestelovens § 20a | 2 | 327‑328 | STRING_TYPE |  |  | `01`: Mottaker er i arbeid |
| `VEDTAKAKT` |  | 2 | 329‑330 | STRING_TYPE |  |  | `02`: Mottaker er allerede i aktivitet knyttet til mottak av statlig eller annen kommunal ytelse |
| `VEDTAKHELSE` |  | 2 | 331‑332 | STRING_TYPE |  |  | `03`: Mottakers helsemessige eller sosiale situasjon hindrer deltakelse i aktivitet |
| `VEDTAKGRUNN` |  | 2 | 333‑334 | STRING_TYPE |  |  | `04`: Andre tungtveiende grunner taler mot at det stilles vilkår om aktivitet |
| `SANKSJONRED` | Sanksjonering av mottaker. Sanksjon i løpet av året som følge av brudd på vilkår etter sosialtjenestelovens § 20 eller § 20a | 2 | 335‑336 | STRING_TYPE |  |  | `01`: Redusert stønad |
| `SANKSJONANDRE` |  | 2 | 337‑338 | STRING_TYPE |  |  | `02`: Andre konsekvenser |
