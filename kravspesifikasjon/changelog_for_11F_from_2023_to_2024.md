# Endringslogg: Økonomisk sosialhjelp, Kostra-skjema 11F, fra  2023 til 2024


## Fjernet
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `DISTRIKTSNR` | Distriktsnummer | 2 | 9‑10 | STRING_TYPE |  |  |  |

## Lagt til
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `VILKARNORSKSAMISKOPPL` | Norsk- eller samiskopplæring | 2 | 305‑306 | STRING_TYPE |  |  | `20`: Norsk- eller samiskopplæring |

## Endret
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `PERSON_JOURNALNR` | Journalnummer | 8 | 11-18<br/> endret til <br/>9-16 | STRING_TYPE |  |  | Ingen endringer |
| `PERSON_FODSELSNR` | Hva er mottakerens fødselsnummer? | 11 | 19-29<br/> endret til <br/>17-27 | STRING_TYPE |  |  | Ingen endringer |
| `PERSON_DUF` | Hva er mottakerens DUF-nummer? | 12 | 30-41<br/> endret til <br/>28-39 | STRING_TYPE |  |  | Ingen endringer |
| `KJONN` | Hva er mottakerens kjønn? | 1 | 42-42<br/> endret til <br/>40-40 | STRING_TYPE |  |  | Ingen endringer |
| `EKTSTAT` | Hva er mottakerens sivilstand/sivilstatus ved siste kontakt? | 1 | 43-43<br/> endret til <br/>41-41 | STRING_TYPE |  |  | Ingen endringer |
| `BU18` | Har mottakeren barn under 18 år som mottakeren (eventuelt ektefelle/samboer) har forsørgerplikt for og som bor i husholdningen? | 1 | 44-44<br/> endret til <br/>42-42 | STRING_TYPE |  |  | Ingen endringer |
| `ANTBU18` | Hvis ja, hvor mange? | 2 | 45-46<br/> endret til <br/>43-44 | INTEGER_TYPE |  |  | Ingen endringer |
| `VKLO` | Hva er mottakerens viktigste kilde til livsopphold ved siste kontakt? | 1 | 47-47<br/> endret til <br/>45-45 | STRING_TYPE |  |  | Endret:<br/>`3` : Trygd/pensjon<br/>endret til Trygd/stønad<br/>`7` : Ektefelle/samboers arbeidsinntekt<br/>endret til Ektefelle/samboers inntekt<br/> |
| `TRYGDESIT` | Oppgi trygd/pensjon som utgjør størst økonomisk verdi ved siste kontakt | 2 | 48-49<br/> endret til <br/>46-47 | STRING_TYPE |  |  | Lagt til:<br/>`13`: Barnetrygd<br/><br/>Endret:<br/>`06` : Etterlattepensjon<br/>endret til Omstillingsstønad/tidl. Etterlattepensjon<br/> |
| `ARBSIT` | Hva er mottakerens viktigste arbeidssituasjon/livssituasjon ved siste kontakt? | 2 | 50-51<br/> endret til <br/>48-49 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_1` | Har mottakeren fått økonomisk stønad i januar? | 2 | 52-53<br/> endret til <br/>50-51 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_2` | Har mottakeren fått økonomisk stønad i februar? | 2 | 54-55<br/> endret til <br/>52-53 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_3` | Har mottakeren fått økonomisk stønad i mars? | 2 | 56-57<br/> endret til <br/>54-55 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_4` | Har mottakeren fått økonomisk stønad i april? | 2 | 58-59<br/> endret til <br/>56-57 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_5` | Har mottakeren fått økonomisk stønad i mai? | 2 | 60-61<br/> endret til <br/>58-59 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_6` | Har mottakeren fått økonomisk stønad i juni? | 2 | 62-63<br/> endret til <br/>60-61 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_7` | Har mottakeren fått økonomisk stønad i juli? | 2 | 64-65<br/> endret til <br/>62-63 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_8` | Har mottakeren fått økonomisk stønad i august? | 2 | 66-67<br/> endret til <br/>64-65 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_9` | Har mottakeren fått økonomisk stønad i september? | 2 | 68-69<br/> endret til <br/>66-67 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_10` | Har mottakeren fått økonomisk stønad i oktober? | 2 | 70-71<br/> endret til <br/>68-69 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_11` | Har mottakeren fått økonomisk stønad i november? | 2 | 72-73<br/> endret til <br/>70-71 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_12` | Har mottakeren fått økonomisk stønad i desember? | 2 | 74-75<br/> endret til <br/>72-73 | STRING_TYPE |  |  | Ingen endringer |
| `BIDRAG` | Bidrag | 7 | 76-82<br/> endret til <br/>74-80 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN` | Lån | 7 | 83-89<br/> endret til <br/>81-87 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_JAN` | Hva fikk mottakeren utbetalt i den enkelte måned? Januar, bidrag | 7 | 90-96<br/> endret til <br/>88-94 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_JAN` | Januar, lån | 7 | 97-103<br/> endret til <br/>95-101 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_FEB` | Februar, bidrag | 7 | 104-110<br/> endret til <br/>102-108 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_FEB` | Februar, lån | 7 | 111-117<br/> endret til <br/>109-115 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_MARS` | Mars, bidrag | 7 | 118-124<br/> endret til <br/>116-122 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_MARS` | Mars, lån | 7 | 125-131<br/> endret til <br/>123-129 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_APRIL` | April, bidrag | 7 | 132-138<br/> endret til <br/>130-136 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_APRIL` | April, lån | 7 | 139-145<br/> endret til <br/>137-143 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_MAI` | Mai, bidrag | 7 | 146-152<br/> endret til <br/>144-150 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_MAI` | Mai, lån | 7 | 153-159<br/> endret til <br/>151-157 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_JUNI` | Juni, bidrag | 7 | 160-166<br/> endret til <br/>158-164 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_JUNI` | Juni, lån | 7 | 167-173<br/> endret til <br/>165-171 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_JULI` | Juli, bidrag | 7 | 174-180<br/> endret til <br/>172-178 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_JULI` | Juli, lån | 7 | 181-187<br/> endret til <br/>179-185 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_AUG` | August, bidrag | 7 | 188-194<br/> endret til <br/>186-192 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_AUG` | August, lån | 7 | 195-201<br/> endret til <br/>193-199 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_SEPT` | September, bidrag | 7 | 202-208<br/> endret til <br/>200-206 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_SEPT` | September, lån | 7 | 209-215<br/> endret til <br/>207-213 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_OKT` | Oktober, bidrag | 7 | 216-222<br/> endret til <br/>214-220 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_OKT` | Oktober, lån | 7 | 223-229<br/> endret til <br/>221-227 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_NOV` | November, bidrag | 7 | 230-236<br/> endret til <br/>228-234 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_NOV` | November, lån | 7 | 237-243<br/> endret til <br/>235-241 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_DES` | Desember, bidrag | 7 | 244-250<br/> endret til <br/>242-248 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_DES` | Desember, lån | 7 | 251-257<br/> endret til <br/>249-255 | INTEGER_TYPE |  |  | Ingen endringer |
| `GITT_OKONOMIRAD` | Er det gitt økonomisk rådgivning på nivå II (midlertidig betalingsudyktighet) eller III (varig betalingsudyktighet) i forbindelse med utbetaling? (Se NAVs veileder til bruk ved økonomisk rådgivning) | 1 | 258-258<br/> endret til <br/>256-256 | STRING_TYPE |  |  | Ingen endringer |
| `FAAT_INDIVIDUELL_PLAN` | Har mottakeren fått utarbeidet individuell plan (lov om sosiale tjenester i NAV § 28)? | 1 | 259-259<br/> endret til <br/>257-257 | STRING_TYPE |  |  | Ingen endringer |
| `SAKSBEHANDLER` | Saksbehandlernummer | 10 | 260-269<br/> endret til <br/>258-267 | STRING_TYPE |  |  | Ingen endringer |
| `BOSIT` | Hva er mottakerens bosituasjon ved siste kontakt? | 1 | 270-270<br/> endret til <br/>268-268 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARSOSLOV` | Stilles det vilkår til mottakeren etter sosialtjenesteloven? | 1 | 271-271<br/> endret til <br/>269-269 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARSAMEKT` | Stilles det vilkår til søkerens samboer/ektefelle etter sosialtjenesteloven? | 1 | 272-272<br/> endret til <br/>270-270 | STRING_TYPE |  |  | Ingen endringer |
| `UTBETDATO` | Oppgi utbetalingsvedtakets dato (DDMMÅÅ) | 6 | 273-278<br/> endret til <br/>271-276 | DATE_TYPE |  | ddMMyy | Ingen endringer |
| `UTBETTOMDATO` | Oppgi utbetalingsvedtakets til og med dato (DDMMÅÅ) | 6 | 279-284<br/> endret til <br/>277-282 | DATE_TYPE |  | ddMMyy | Ingen endringer |
| `VILKARARBEID` | Oppgi hvilke vilkår det stilles til mottakeren. Flere kryss mulig | 2 | 285-286<br/> endret til <br/>283-284 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARKURS` |  | 2 | 287-288<br/> endret til <br/>285-286 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARUTD` |  | 2 | 289-290<br/> endret til <br/>287-288 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARJOBBLOG` |  | 2 | 291-292<br/> endret til <br/>289-290 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARJOBBTILB` |  | 2 | 293-294<br/> endret til <br/>291-292 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARSAMT` |  | 2 | 295-296<br/> endret til <br/>293-294 | STRING_TYPE |  |  | Ingen endringer |
| `VILKAROKRETT` |  | 2 | 297-298<br/> endret til <br/>295-296 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARLIVSH` |  | 2 | 299-300<br/> endret til <br/>297-298 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARHELSE` |  | 2 | 301-302<br/> endret til <br/>299-300 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARANNET` |  | 2 | 303-304<br/> endret til <br/>301-302 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARDIGPLAN` | Bruke og følge opp digital aktivitetsplan | 2 | 305-306<br/> endret til <br/>303-304 | STRING_TYPE |  |  | Ingen endringer |
