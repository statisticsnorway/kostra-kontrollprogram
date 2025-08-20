# Endringslogg: Kvalifiseringsstønad, Kostra-skjema 11CF, fra  2024 til 2025


## Lagt til
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `FODSELSDATO` | Fødselsdato (DDMMÅÅÅÅ) | 8 | 9‑16 | DATE_TYPE | ✅ | ddMMyyyy |  |

## Endret
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `PERSON_JOURNALNR` | Journalnummer | 8 | 9-16 <br/> endret til <br/> 17-24 | STRING_TYPE | ✅ |  | Ingen endringer |
| `PERSON_FODSELSNR` | Fødsels- og personnummer | 11 | 17-27 <br/> endret til <br/> 25-35 | STRING_TYPE | ✅ |  | Ingen endringer |
| `KJONN` | Kjønn | 1 | 28-28 <br/> endret til <br/> 36-36 | STRING_TYPE | ✅ |  | Ingen endringer |
| `EKTSTAT` | Sivilstand | 1 | 29-29 <br/> endret til <br/> 37-37 | STRING_TYPE | ✅ |  | Ingen endringer |
| `BU18` | Har deltakeren barn under 18 år som deltakeren (eventuelt ektefelle/samboer) har forsørgerplikt for og som bor i husholdningen? | 1 | 30-30 <br/> endret til <br/> 38-38 | STRING_TYPE | ✅ |  | Ingen endringer |
| `ANTBU18` | Hvis ja, hvor mange? | 2 | 31-32 <br/> endret til <br/> 39-40 | INTEGER_TYPE |  |  | Ingen endringer |
| `REG_DATO` | Hvilken dato ble søknaden registrert ved NAV-kontoret? DDMMÅÅ | 6 <br/> endret til <br/> 8 | 33-38 <br/> endret til <br/> 41-48 | DATE_TYPE | ✅ | ddMMyy <br/> endret til <br/> ddMMyyyy | Ingen endringer |
| `VEDTAK_DATO` | Hvilken dato ble det fattet vedtak om program (søknad innvilget)? DDMMÅÅ | 6 <br/> endret til <br/> 8 | 39-44 <br/> endret til <br/> 49-56 | DATE_TYPE | ✅ | ddMMyy <br/> endret til <br/> ddMMyyyy | Ingen endringer |
| `BEGYNT_DATO` | Hvilken dato begynte deltakeren i program (iverksettelse)? DDMMÅÅ | 6 <br/> endret til <br/> 8 | 45-50 <br/> endret til <br/> 57-64 | DATE_TYPE | ✅ | ddMMyy <br/> endret til <br/> ddMMyyyy | Ingen endringer |
| `KVP_KOMM` | Kommer deltakeren fra kvalifiseringsprogram i annen kommune? | 1 | 51-51 <br/> endret til <br/> 65-65 | STRING_TYPE |  |  | Ingen endringer |
| `KOMMNR_KVP_KOMM` | Hvis ja, velg kommunenummer fra liste | 4 | 52-55 <br/> endret til <br/> 66-69 | STRING_TYPE |  |  | Ingen endringer |
| `YTELSE_SOSHJELP` | Hadde deltakeren i løpet av de siste to månedene før registrert søknad ved NAV-kontoret en eller flere av følgende ytelser? Kan krysse av for flere svaralternativer) | 1 | 56-56 <br/> endret til <br/> 70-70 | STRING_TYPE |  |  | Ingen endringer |
| `YTELSE_TYPE_SOSHJ` | Hvis sosialhjelp | 1 | 57-57 <br/> endret til <br/> 71-71 | STRING_TYPE |  |  | Ingen endringer |
| `YTELSE_INTRO` | Andre ytelser | 1 | 58-58 <br/> endret til <br/> 72-72 | STRING_TYPE |  |  | Ingen endringer |
| `YTELSE_INDIVIDSTONAD` |  | 1 | 59-59 <br/> endret til <br/> 73-73 | STRING_TYPE |  |  | Ingen endringer |
| `YTELSE_FOLKETRYGDL` |  | 1 | 60-60 <br/> endret til <br/> 74-74 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_ASTONAD` | Har deltakeren i 2024 i løpet av perioden med kvalifiseringsstønad også mottatt økonomisk sosialhjelp, kommunal bostøtte eller husbankens bostøtte? Hvis ja, hvilke? (kan krysse av for flere svaralternativer) <br/> endret til <br/> Har deltakeren i 2025 i løpet av perioden med kvalifiseringsstønad også mottatt økonomisk sosialhjelp, kommunal bostøtte eller husbankens bostøtte? Hvis ja, hvilke? (kan krysse av for flere svaralternativer) | 1 | 61-61 <br/> endret til <br/> 75-75 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_KOMMBOS` |  | 1 | 62-62 <br/> endret til <br/> 76-76 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_HUSBANKBOS` |  | 1 | 63-63 <br/> endret til <br/> 77-77 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_SOSHJ_ENGANG` |  | 1 | 64-64 <br/> endret til <br/> 78-78 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_SOSHJ_PGM` |  | 1 | 65-65 <br/> endret til <br/> 79-79 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_MED_SOSHJ_SUP` |  | 1 | 66-66 <br/> endret til <br/> 80-80 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_1` | For hvilke måneder i løpet av 2024 har deltakeren fått kvalifiseringsstønad? | 2 | 67-68 <br/> endret til <br/> 81-82 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_2` |  | 2 | 69-70 <br/> endret til <br/> 83-84 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_3` |  | 2 | 71-72 <br/> endret til <br/> 85-86 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_4` |  | 2 | 73-74 <br/> endret til <br/> 87-88 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_5` |  | 2 | 75-76 <br/> endret til <br/> 89-90 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_6` |  | 2 | 77-78 <br/> endret til <br/> 91-92 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_7` |  | 2 | 79-80 <br/> endret til <br/> 93-94 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_8` |  | 2 | 81-82 <br/> endret til <br/> 95-96 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_9` |  | 2 | 83-84 <br/> endret til <br/> 97-98 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_10` |  | 2 | 85-86 <br/> endret til <br/> 99-100 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_11` |  | 2 | 87-88 <br/> endret til <br/> 101-102 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_12` |  | 2 | 89-90 <br/> endret til <br/> 103-104 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_STONAD` | Samlet utbetalt kvalifiseringsstønad i løpet av 2024 | 7 | 91-97 <br/> endret til <br/> 105-111 | INTEGER_TYPE |  |  | Ingen endringer |
| `STATUS` | Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12.2024? <br/> endret til <br/> Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12.2025? | 1 | 98-98 <br/> endret til <br/> 112-112 | STRING_TYPE | ✅ |  | Ingen endringer |
| `AVSL_DATO` | Hvilken dato avsluttet deltakeren programmet? (gjelder ikke for permisjoner) (DDMMÅÅ) | 6 <br/> endret til <br/> 8 | 99-104 <br/> endret til <br/> 113-120 | DATE_TYPE |  | ddMMyy <br/> endret til <br/> ddMMyyyy | Ingen endringer |
| `AVSL_ORDINAERTARB` | Ved fullført program eller program avsluttet etter avtale (gjelder ikke flytting) - hva var deltakerens situasjon umiddelbart etter avslutningen? (kan krysse av for flere svaralternativer) | 2 | 105-106 <br/> endret til <br/> 121-122 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_ARBLONNSTILS` |  | 2 | 107-108 <br/> endret til <br/> 123-124 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_ARBMARK` |  | 2 | 109-110 <br/> endret til <br/> 125-126 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_SKOLE` |  | 2 | 111-112 <br/> endret til <br/> 127-128 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_UFORE` |  | 2 | 113-114 <br/> endret til <br/> 129-130 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_AAP` |  | 2 | 115-116 <br/> endret til <br/> 131-132 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_OK_AVKLAR` |  | 2 | 117-118 <br/> endret til <br/> 133-134 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_UTEN_OK_AVKLAR` |  | 2 | 119-120 <br/> endret til <br/> 135-136 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_ANNET` |  | 2 | 121-122 <br/> endret til <br/> 137-138 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_UKJENT` |  | 2 | 123-124 <br/> endret til <br/> 139-140 | STRING_TYPE |  |  | Ingen endringer |
| `AVSL_VIKTIGSTE_INNTEKT` | Hva var deltakerens viktigste inntektskilde umiddelbart etter avslutningen? (Kun ett kryss) | 2 | 125-126 <br/> endret til <br/> 141-142 | STRING_TYPE |  |  | Ingen endringer |
| `SAKSBEHANDLER` | Saksbehandlernummer | 10 | 127-136 <br/> endret til <br/> 143-152 | STRING_TYPE |  |  | Ingen endringer |
