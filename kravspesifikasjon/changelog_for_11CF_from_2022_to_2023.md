# Endringslogg: Kvalifiseringsstønad, Kostra-skjema 11CF, fra  2022 til 2023


## Fjernet
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `KVP_OSLO` | Kun for Oslos bydeler: Kommer deltakeren fra kvalifiseringsprogram i annen bydel? | 1 | 58‑58 | STRING_TYPE |  |  |  |

## Endret
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `KOMMNR_KVP_KOMM` | Hvis ja, velg kommunenummer fra liste | 4 | 54-57 | STRING_TYPE |  |  | [Vis endringer i Klass](https://www.ssb.no/klass/klassifikasjoner/131/endringer)<br/><br/>Endret:<br/>`5001` : Trondheim<br/>endret til Trondheim - Tråante<br/>`5025` : Røros<br/>endret til Røros - Rosse<br/> |
| `YTELSE_SOSHJELP` | Hadde deltakeren i løpet av de siste to månedene før registrert søknad ved NAV-kontoret en eller flere av følgende ytelser? Kan krysse av for flere svaralternativer) | 1 | 59-59<br/> endret til <br/>58-58 | STRING_TYPE |  |  | Ingen endringer |
| `YTELSE_TYPE_SOSHJ` | Hvis sosialhjelp | 1 | 60-60<br/> endret til <br/>59-59 | STRING_TYPE |  |  | Ingen endringer |
| `YTELSE_INTRO` | Andre ytelser | 1 | 61-61<br/> endret til <br/>60-60 | STRING_TYPE |  |  | Ingen endringer |
| `YTELSE_INDIVIDSTONAD` |  | 1 | 62-62<br/> endret til <br/>61-61 | STRING_TYPE |  |  | Ingen endringer |
| `YTELSE_FOLKETRYGDL` |  | 1 | 63-63<br/> endret til <br/>62-62 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_ASTONAD` | Har deltakeren i 2022 i løpet av perioden med kvalifiseringsstønad også mottatt økonomisk sosialhjelp, kommunal bostøtte eller husbankens bostøtte? Hvis ja, hvilke? (kan krysse av for flere svaralternativer)<br/> endret til <br/>Har deltakeren i 2023 i løpet av perioden med kvalifiseringsstønad også mottatt økonomisk sosialhjelp, kommunal bostøtte eller husbankens bostøtte? Hvis ja, hvilke? (kan krysse av for flere svaralternativer) | 1 | 64-64<br/> endret til <br/>63-63 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_KOMMBOS` |  | 1 | 65-65<br/> endret til <br/>64-64 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_HUSBANKBOS` |  | 1 | 66-66<br/> endret til <br/>65-65 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_SOSHJ_ENGANG` |  | 1 | 67-67<br/> endret til <br/>66-66 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_SOSHJ_PGM` |  | 1 | 68-68<br/> endret til <br/>67-67 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_SOSHJ_SUP` |  | 1 | 69-69<br/> endret til <br/>68-68 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_1` | For hvilke måneder i løpet av 2022 har deltakeren fått kvalifiseringsstønad?<br/> endret til <br/>For hvilke måneder i løpet av 2023 har deltakeren fått kvalifiseringsstønad? | 2 | 70-71<br/> endret til <br/>69-70 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_2` |  | 2 | 72-73<br/> endret til <br/>71-72 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_3` |  | 2 | 74-75<br/> endret til <br/>73-74 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_4` |  | 2 | 76-77<br/> endret til <br/>75-76 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_5` |  | 2 | 78-79<br/> endret til <br/>77-78 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_6` |  | 2 | 80-81<br/> endret til <br/>79-80 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_7` |  | 2 | 82-83<br/> endret til <br/>81-82 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_8` |  | 2 | 84-85<br/> endret til <br/>83-84 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_9` |  | 2 | 86-87<br/> endret til <br/>85-86 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_10` |  | 2 | 88-89<br/> endret til <br/>87-88 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_11` |  | 2 | 90-91<br/> endret til <br/>89-90 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_12` |  | 2 | 92-93<br/> endret til <br/>91-92 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_STONAD` | Samlet utbetalt kvalifiseringsstønad i løpet av 2022<br/> endret til <br/>Samlet utbetalt kvalifiseringsstønad i løpet av 2023 | 7 | 94-100<br/> endret til <br/>93-99 | INTEGER_TYPE |  |  | Ingen endringer |
| `STATUS` | Hva er status for deltakelsen i kvalifiseringsprogrammet? | 1 | 101-101<br/> endret til <br/>100-100 | STRING_TYPE | ✅ |  | Lagt til:<br/>`7`: Deltakerens program er avsluttet etter avbrudd<br/><br/> |
| `AVSL_DATO` | Hvilken dato avsluttet deltakeren programmet? (gjelder ikke for permisjoner) (DDMMÅÅ) | 6 | 102-107<br/> endret til <br/>101-106 | DATE_TYPE |  | ddMMyy | Ingen endringer |
| `AVSL_ORDINAERTARB` | Ved fullført program eller program avsluttet etter avtale (gjelder ikke flytting) - hva var deltakerens situasjon umiddelbart etter avslutningen? (kan krysse av for flere svaralternativer) | 2 | 108-109<br/> endret til <br/>107-108 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_ARBLONNSTILS` |  | 2 | 110-111<br/> endret til <br/>109-110 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_ARBMARK` |  | 2 | 112-113<br/> endret til <br/>111-112 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_SKOLE` |  | 2 | 114-115<br/> endret til <br/>113-114 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_UFORE` |  | 2 | 116-117<br/> endret til <br/>115-116 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_AAP` |  | 2 | 118-119<br/> endret til <br/>117-118 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_OK_AVKLAR` |  | 2 | 120-121<br/> endret til <br/>119-120 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_UTEN_OK_AVKLAR` |  | 2 | 122-123<br/> endret til <br/>121-122 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_ANNET` |  | 2 | 124-125<br/> endret til <br/>123-124 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_UKJENT` |  | 2 | 126-127<br/> endret til <br/>125-126 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_VIKTIGSTE_INNTEKT` | Hva var deltakerens viktigste inntektskilde umiddelbart etter avslutningen? (Kun ett kryss) | 2 | 128-129<br/> endret til <br/>127-128 | STRING_TYPE |  |  | Ingen endringer |
| `SAKSBEHANDLER` | Saksbehandlernummer | 10 | 130-139<br/> endret til <br/>129-138 | STRING_TYPE |  |  | Ingen endringer |
