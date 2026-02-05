# Endringslogg: Kvalifiseringsstønd, Kostra-skjema 11CF, fra  2025 til 2026


## Fjernet
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `YTELSE_INDIVIDSTONAD` |  | 1 | 73‑73 | STRING_TYPE |  |  | `5`: Individstønad - stønad til livsopphold etter forskrift om arbeidsmarkedstiltak |

## Lagt til
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `YTELSE_TILTAKSPENGER` |  | 1 | 73‑73 | STRING_TYPE |  |  | `5`: Individstønad - stønad til livsopphold etter forskrift om arbeidsmarkedstiltak |

## Endret
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `FODSELSDATO` | Fødselsdato (DDMMÅÅÅÅ)<br/> endret til <br/>Fødselsdato (DDMMÅÅÅÅÅÅ) | 8 | 9-16 | DATE_TYPE | ✅ | ddMMyyyy | Ingen endringer |
| `REG_DATO` | Hvilken dato ble søknaden registrert ved NAV-kontoret? DDMMÅÅ<br/> endret til <br/>Hvilken dato ble søknaden registrert ved NAV-kontoret? DDMMÅÅÅÅ | 8 | 41-48 | DATE_TYPE | ✅ | ddMMyyyy | Ingen endringer |
| `VEDTAK_DATO` | Hvilken dato ble det fattet vedtak om program (søknad innvilget)? DDMMÅÅ<br/> endret til <br/>Hvilken dato ble det fattet vedtak om program (søknad innvilget)? DDMMÅÅÅÅ | 8 | 49-56 | DATE_TYPE | ✅ | ddMMyyyy | Ingen endringer |
| `BEGYNT_DATO` | Hvilken dato begynte deltakeren i program (iverksettelse)? DDMMÅÅ<br/> endret til <br/>Hvilken dato begynte deltakeren i program (iverksettelse)? DDMMÅÅÅÅ | 8 | 57-64 | DATE_TYPE | ✅ | ddMMyyyy | Ingen endringer |
| `KOMMNR_KVP_KOMM` | Hvis ja, velg kommunenummer fra liste | 4 | 66-69 | STRING_TYPE |  |  | [Vis endringer i Klass](https://www.ssb.no/klass/klassifikasjoner/131/endringer)<br/><br/>Endret:<br/>`0301` : Oslo<br/>endret til Oslo - Oslove<br/>`5006` : Steinkjer<br/>endret til Steinkjer - Stïentje<br/>`5536` : Lyngen<br/>endret til Lyngen - Ivgu - Yykeä<br/> |
| `KVP_MED_ASTONAD` | Har deltakeren i 2025 i løpet av perioden med kvalifiseringsstønad også mottatt økonomisk sosialhjelp, kommunal bostøtte eller husbankens bostøtte? Hvis ja, hvilke? (kan krysse av for flere svaralternativer)<br/> endret til <br/>Har deltakeren i 2026 i løpet av perioden med kvalifiseringsstønad også mottatt økonomisk sosialhjelp, kommunal bostøtte eller husbankens bostøtte? Hvis ja, hvilke? (kan krysse av for flere svaralternativer) | 1 | 75-75 | STRING_TYPE |  |  | Ingen endringer |
| `STMND_1` | For hvilke måneder i løpet av 2025 har deltakeren fått kvalifiseringsstønad?<br/> endret til <br/>For hvilke måneder i løpet av 2026 har deltakeren fått kvalifiseringsstønad? | 2 | 81-82 | STRING_TYPE |  |  | Ingen endringer |
| `KVP_STONAD` | Samlet utbetalt kvalifiseringsstønad i løpet av 2025<br/> endret til <br/>Samlet utbetalt kvalifiseringsstønad i løpet av 2026 | 7 | 105-111 | INTEGER_TYPE |  |  | Ingen endringer |
| `STATUS` | Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12.2025?<br/> endret til <br/>Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12.2026? | 1 | 112-112 | STRING_TYPE | ✅ |  | Ingen endringer |
| `AVSL_DATO` | Hvilken dato avsluttet deltakeren programmet? (gjelder ikke for permisjoner) (DDMMÅÅ)<br/> endret til <br/>Hvilken dato avsluttet deltakeren programmet? (gjelder ikke for permisjoner) (DDMMÅÅÅÅ) | 8 | 113-120 | DATE_TYPE |  | ddMMyyyy | Ingen endringer |
