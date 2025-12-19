# Endringslogg: Økonomisk sosialhjelp, Kostra-skjema 11F, fra  2024 til 2025


## Lagt til
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `FODSELSDATO` | Fødselsdato (DDMMÅÅÅÅ) | 8 | 9‑16 | DATE_TYPE | ✅ | ddMMyyyy |  |

## Endret
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `KOMMUNE_NR` | Kommunenummer | 4 | 1-4 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `VERSION` | Rapporteringsår / versjon / oppgaveår | 2 | 5-6 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `BYDELSNR` | Bydelsnummer | 2 | 7-8 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `PERSON_JOURNALNR` | Journalnummer | 8 | 9-16<br/> endret til <br/>17-24 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `PERSON_FODSELSNR` | Hva er mottakerens fødselsnummer? | 11 | 17-27<br/> endret til <br/>25-35 | STRING_TYPE |  |  | Ingen endringer |
| `PERSON_DUF` | Hva er mottakerens DUF-nummer? | 12 | 28-39<br/> endret til <br/>36-47 | STRING_TYPE |  |  | Ingen endringer |
| `KJONN` | Hva er mottakerens kjønn? | 1 | 40-40<br/> endret til <br/>48-48 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `EKTSTAT` | Hva er mottakerens sivilstand/sivilstatus ved siste kontakt? | 1 | 41-41<br/> endret til <br/>49-49 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `BU18` | Har mottakeren barn under 18 år som mottakeren (eventuelt ektefelle/samboer) har forsørgerplikt for og som bor i husholdningen? | 1 | 42-42<br/> endret til <br/>50-50 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `ANTBU18` | Hvis ja, hvor mange? | 2 | 43-44<br/> endret til <br/>51-52 | INTEGER_TYPE |  |  | Ingen endringer |
| `VKLO` | Hva er mottakerens viktigste kilde til livsopphold ved siste kontakt? | 1 | 45-45<br/> endret til <br/>53-53 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `TRYGDESIT` | Oppgi trygd/pensjon som utgjør størst økonomisk verdi ved siste kontakt | 2 | 46-47<br/> endret til <br/>54-55 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `ARBSIT` | Hva er mottakerens viktigste arbeidssituasjon/livssituasjon ved siste kontakt? | 2 | 48-49<br/> endret til <br/>56-57 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `STMND_1` | Har mottakeren fått økonomisk stønad i januar? | 2 | 50-51<br/> endret til <br/>58-59 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_2` | Har mottakeren fått økonomisk stønad i februar? | 2 | 52-53<br/> endret til <br/>60-61 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_3` | Har mottakeren fått økonomisk stønad i mars? | 2 | 54-55<br/> endret til <br/>62-63 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_4` | Har mottakeren fått økonomisk stønad i april? | 2 | 56-57<br/> endret til <br/>64-65 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_5` | Har mottakeren fått økonomisk stønad i mai? | 2 | 58-59<br/> endret til <br/>66-67 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_6` | Har mottakeren fått økonomisk stønad i juni? | 2 | 60-61<br/> endret til <br/>68-69 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_7` | Har mottakeren fått økonomisk stønad i juli? | 2 | 62-63<br/> endret til <br/>70-71 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_8` | Har mottakeren fått økonomisk stønad i august? | 2 | 64-65<br/> endret til <br/>72-73 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_9` | Har mottakeren fått økonomisk stønad i september? | 2 | 66-67<br/> endret til <br/>74-75 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_10` | Har mottakeren fått økonomisk stønad i oktober? | 2 | 68-69<br/> endret til <br/>76-77 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_11` | Har mottakeren fått økonomisk stønad i november? | 2 | 70-71<br/> endret til <br/>78-79 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_12` | Har mottakeren fått økonomisk stønad i desember? | 2 | 72-73<br/> endret til <br/>80-81 | STRING_TYPE |  |  | Ingen endringer |
| `BIDRAG` | Bidrag<br/> endret til <br/>Hva fikk mottakeren utbetalt i bidrag i løpet av året? | 7 | 74-80<br/> endret til <br/>82-88 | INTEGER_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `LAAN` | Lån<br/> endret til <br/>Hva fikk mottakeren utbetalt i lån i løpet av året? | 7 | 81-87<br/> endret til <br/>89-95 | INTEGER_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `BIDRAG_JAN` | Hva fikk mottakeren utbetalt i den enkelte måned? Januar, bidrag | 7 | 88-94<br/> endret til <br/>96-102 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_JAN` | Januar, lån | 7 | 95-101<br/> endret til <br/>103-109 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_FEB` | Februar, bidrag | 7 | 102-108<br/> endret til <br/>110-116 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_FEB` | Februar, lån | 7 | 109-115<br/> endret til <br/>117-123 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_MARS` | Mars, bidrag | 7 | 116-122<br/> endret til <br/>124-130 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_MARS` | Mars, lån | 7 | 123-129<br/> endret til <br/>131-137 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_APRIL` | April, bidrag | 7 | 130-136<br/> endret til <br/>138-144 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_APRIL` | April, lån | 7 | 137-143<br/> endret til <br/>145-151 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_MAI` | Mai, bidrag | 7 | 144-150<br/> endret til <br/>152-158 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_MAI` | Mai, lån | 7 | 151-157<br/> endret til <br/>159-165 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_JUNI` | Juni, bidrag | 7 | 158-164<br/> endret til <br/>166-172 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_JUNI` | Juni, lån | 7 | 165-171<br/> endret til <br/>173-179 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_JULI` | Juli, bidrag | 7 | 172-178<br/> endret til <br/>180-186 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_JULI` | Juli, lån | 7 | 179-185<br/> endret til <br/>187-193 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_AUG` | August, bidrag | 7 | 186-192<br/> endret til <br/>194-200 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_AUG` | August, lån | 7 | 193-199<br/> endret til <br/>201-207 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_SEPT` | September, bidrag | 7 | 200-206<br/> endret til <br/>208-214 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_SEPT` | September, lån | 7 | 207-213<br/> endret til <br/>215-221 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_OKT` | Oktober, bidrag | 7 | 214-220<br/> endret til <br/>222-228 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_OKT` | Oktober, lån | 7 | 221-227<br/> endret til <br/>229-235 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_NOV` | November, bidrag | 7 | 228-234<br/> endret til <br/>236-242 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_NOV` | November, lån | 7 | 235-241<br/> endret til <br/>243-249 | INTEGER_TYPE |  |  | Ingen endringer |
| `BIDRAG_DES` | Desember, bidrag | 7 | 242-248<br/> endret til <br/>250-256 | INTEGER_TYPE |  |  | Ingen endringer |
| `LAAN_DES` | Desember, lån | 7 | 249-255<br/> endret til <br/>257-263 | INTEGER_TYPE |  |  | Ingen endringer |
| `GITT_OKONOMIRAD` | Er det gitt økonomisk rådgivning på nivå II (midlertidig betalingsudyktighet) eller III (varig betalingsudyktighet) i forbindelse med utbetaling? (Se NAVs veileder til bruk ved økonomisk rådgivning) | 1 | 256-256<br/> endret til <br/>264-264 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `FAAT_INDIVIDUELL_PLAN` | Har mottakeren fått utarbeidet individuell plan (lov om sosiale tjenester i NAV § 28)? | 1 | 257-257<br/> endret til <br/>265-265 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `SAKSBEHANDLER` | Saksbehandlernummer | 10 | 258-267<br/> endret til <br/>266-275 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `BOSIT` | Hva er mottakerens bosituasjon ved siste kontakt? | 1 | 268-268<br/> endret til <br/>276-276 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `VILKARSOSLOV` | Stilles det vilkår til mottakeren etter sosialtjenesteloven?<br/> endret til <br/>I forbindelse med vedtak om utbetaling av stønad, stilles det vilkår til mottakeren etter sosialtjenesteloven? | 1 | 269-269<br/> endret til <br/>277-277 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `VILKARSAMEKT` | Stilles det vilkår til søkerens samboer/ektefelle etter sosialtjenesteloven?<br/> endret til <br/>I forbindelse med vedtak om utbetaling av stønad, stilles det vilkår til søkerens samboer/ektefelle etter sosialtjenesteloven? | 1 | 270-270<br/> endret til <br/>278-278 | STRING_TYPE | ❌<br/> endret til <br/>✅ |  | Ingen endringer |
| `UTBETDATO` | Oppgi utbetalingsvedtakets dato (DDMMÅÅ)<br/> endret til <br/>Hvis "ja" på spørsmålet "Stilles det vilkår til mottakeren etter sosialtjenesteloven?" Oppgi utbetalingsvedtakets dato (DDMMÅÅÅÅ) | 6<br/> endret til <br/>8 | 271-276<br/> endret til <br/>279-286 | DATE_TYPE |  | ddMMyy<br/> endret til <br/>ddMMyyyy | Ingen endringer |
| `UTBETTOMDATO` | Oppgi utbetalingsvedtakets til og med dato (DDMMÅÅ)<br/> endret til <br/>Hvis "ja" på spørsmålet "Stilles det vilkår til mottakeren etter sosialtjenesteloven?" Oppgi utbetalingsvedtakets til og med dato (DDMMÅÅÅÅ) | 6<br/> endret til <br/>8 | 277-282<br/> endret til <br/>287-294 | DATE_TYPE |  | ddMMyy<br/> endret til <br/>ddMMyyyy | Ingen endringer |
| `VILKARARBEID` | Oppgi hvilke vilkår det stilles til mottakeren. Flere kryss mulig | 2 | 283-284<br/> endret til <br/>295-296 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARKURS` |  | 2 | 285-286<br/> endret til <br/>297-298 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARUTD` |  | 2 | 287-288<br/> endret til <br/>299-300 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARJOBBLOG` |  | 2 | 289-290<br/> endret til <br/>301-302 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARJOBBTILB` |  | 2 | 291-292<br/> endret til <br/>303-304 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARSAMT` |  | 2 | 293-294<br/> endret til <br/>305-306 | STRING_TYPE |  |  | Ingen endringer |
| `VILKAROKRETT` |  | 2 | 295-296<br/> endret til <br/>307-308 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARLIVSH` |  | 2 | 297-298<br/> endret til <br/>309-310 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARHELSE` |  | 2 | 299-300<br/> endret til <br/>311-312 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARANNET` |  | 2 | 301-302<br/> endret til <br/>313-314 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARDIGPLAN` | Bruke og følge opp digital aktivitetsplan | 2 | 303-304<br/> endret til <br/>315-316 | STRING_TYPE |  |  | Ingen endringer |
| `VILKARNORSKSAMISKOPPL` | Norsk- eller samiskopplæring | 2 | 305-306<br/> endret til <br/>317-318 | STRING_TYPE |  |  | Ingen endringer |
| `VEDTAKDATO` | Unntak for mottaker under 30 år. Begrunnelse for første unntak fra sosialtjenestelovens § 20a for mottaker under 30 år. Oppgi vedtaksdato (DDMMÅÅ)<br/> endret til <br/>Unntak for mottaker under 30 år. Begrunnelse for første unntak fra sosialtjenestelovens § 20a for mottaker under 30 år. Oppgi vedtaksdato (DDMMÅÅÅÅ) | 6<br/> endret til <br/>8 | 307-312<br/> endret til <br/>319-326 | DATE_TYPE |  | ddMMyy<br/> endret til <br/>ddMMyyyy | Ingen endringer |
| `VEDTAKARB` | Begrunnelse for første unntak fra sosialtjenestelovens § 20a | 2 | 313-314<br/> endret til <br/>327-328 | STRING_TYPE |  |  | Ingen endringer |
| `VEDTAKAKT` |  | 2 | 315-316<br/> endret til <br/>329-330 | STRING_TYPE |  |  | Ingen endringer |
| `VEDTAKHELSE` |  | 2 | 317-318<br/> endret til <br/>331-332 | STRING_TYPE |  |  | Ingen endringer |
| `VEDTAKGRUNN` |  | 2 | 319-320<br/> endret til <br/>333-334 | STRING_TYPE |  |  | Ingen endringer |
| `SANKSJONRED` | Sanksjonering av mottaker. Sanksjon i løpet av året som følge av brudd på vilkår etter sosialtjenestelovens § 20a<br/> endret til <br/>Sanksjonering av mottaker. Sanksjon i løpet av året som følge av brudd på vilkår etter sosialtjenestelovens § 20 eller § 20a | 2 | 321-322<br/> endret til <br/>335-336 | STRING_TYPE |  |  | Ingen endringer |
| `SANKSJONANDRE` |  | 2 | 323-324<br/> endret til <br/>337-338 | STRING_TYPE |  |  | Ingen endringer |
