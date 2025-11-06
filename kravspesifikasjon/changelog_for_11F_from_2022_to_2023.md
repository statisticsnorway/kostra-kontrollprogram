# Endringslogg: Økonomisk sosialhjelp, Kostra-skjema 11F, fra  2022 til 2023


## Endret
| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `VKLO` | Hva er mottakerens viktigste kilde til livsopphold ved siste kontakt? | 1 | 47-47 | STRING_TYPE |  |  | Lagt til:<br/>`1`: Arbeidsinntekt<br/>`2`: Kursstønad/lønn i arbeidsmarkedstiltak<br/>`3`: Trygd/pensjon<br/>`4`: Stipend/lån<br/>`5`: Sosialhjelp<br/>`6`: Introduksjonsstøtte<br/>`7`: Ektefelle/samboers arbeidsinntekt<br/>`8`: Kvalifiseringsstønad<br/>`9`: Annen inntekt<br/><br/> |
| `TRYGDESIT` | Oppgi trygd/pensjon som utgjør størst økonomisk verdi ved siste kontakt | 2 | 48-49 | STRING_TYPE |  |  | Lagt til:<br/>`01`: Sykepenger<br/>`02`: Dagpenger<br/>`04`: Uføretrygd<br/>`05`: Overgangsstønad<br/>`06`: Etterlattepensjon<br/>`07`: Alderspensjon<br/>`09`: Supplerende stønad (kort botid)<br/>`10`: Annen trygd<br/>`11`: Arbeidsavklaringspenger<br/>`12`: Har ingen trygd/pensjon<br/><br/> |
| `ARBSIT` | Hva er mottakerens viktigste arbeidssituasjon/livssituasjon ved siste kontakt? | 2 | 50-51 | STRING_TYPE |  |  | Lagt til:<br/>`01`: Arbeid, heltid<br/>`02`: Arbeid, deltid<br/>`03`: Under utdanning<br/>`04`: Ikke arbeidssøker<br/>`05`: Arbeidsmarkedstiltak (statlig)<br/>`06`: Kommunalt tiltak<br/>`07`: Registrert arbeidsledig<br/>`08`: Arbeidsledig, men ikke registrert hos NAV<br/>`09`: Introduksjonsordning<br/>`10`: Kvalifiseringsprogram<br/><br/> |
