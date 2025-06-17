# Filbeskrivelse: Regnskap (2025)

Filbeskrivelse for alle regnskap for 2025 som blir rapportert via skjema.ssb.no

## Feltdefinisjoner

| Navn | Beskrivelse | Lengde | Fra‑Til | Datatype | Obligatorisk | Dato-maske | Kodeliste |
|------|-------------|--------|---------|----------|--------------|------------|-----------|
| `skjema` | Type regnskap | 2 | 1‑2 | STRING_TYPE | ☑️ |  | `0A`: 0A. Bevilgningsregnskap kommunekasse<br/>`0B`: 0B. Balanse kommunekasse<br/>`0C`: 0C. Bevilgningsregnskap fylkeskasse<br/>`0D`: 0D. Balanse fylkeskasse<br/>`0F`: 0F. Bevilgningsregnskap for kirkelig fellesråd<br/>`0G`: 0G. Balanseregnskap for kirkelig fellesråd<br/>`0I`: 0I. Bevilgningsregnskap kommunale særbedrifter og lånefond<br/>`0J`: 0J. Balanse kommunale særbedrifter og lånefond<br/>`0K`: 0K. Bevilgningsregnskap fylkeskommunale særbedrifter og lånefond<br/>`0L`: 0L. Balanse fylkeskommunale særbedrifter og lånefond<br/>`0M`: 0M. Konsolidert bevilgningsregnskap kommuner<br/>`0N`: 0N. Konsolidert balanse kommuner<br/>`0P`: 0P. Konsolidert bevilgningsregnskap fylkeskommuner<br/>`0Q`: 0Q. Konsolidert balanse fylkeskommuner<br/>`0X`: 0X. Resultatrekneskap helseforetak<br/>`0Y`: 0Y. Balanse helseforetak |
| `aargang` | Rapporteringsår / oppgaveår / årgang | 4 | 3‑6 | STRING_TYPE | ☑️ |  |  |
| `kvartal` | Årsregnskap / kvartalsvis regnskap | 1 | 7‑7 | STRING_TYPE | ☑️ |  | ` `: Årsregnskap<br/>`1`: 1. Kvartal<br/>`2`: 2. Kvartal<br/>`3`: 3. Kvartal<br/>`4`: 4. Kvartal |
| `region` | Alle rapporterer med 6-sifret nummer for region.<br/>Helseregionsnummer.<br/>Fylkeskommunenummer.<br/>Kommunenummer, dersom det ikke rapporteres på bydelsnivå, markeres de to siste posisjoner med 00. <br/>Bydelsnummer, bydelsrapportering godtas kun fra Oslo.<br/> | 6 | 8‑13 | STRING_TYPE | ☑️ |  |  |
| `orgnr` | Benyttes der det rapporteres for virksomheter, f.eks. regnskap 0F, 0G, 0I, 0J, 0K 0L, 0X, 0Y | 9 | 14‑22 | STRING_TYPE |  |  |  |
| `foretaksnr` | Benyttes der det rapporteres for foretak, f.eks regnskap 0X | 9 | 23‑31 | STRING_TYPE |  |  |  |
| `kontoklasse` | Se regnskapets veiledning for aktuelle koder for kontoklasse | 1 | 32‑32 | STRING_TYPE | ☑️ |  | ` `: Helseforetakregnskap<br/>`0`: Investeringsregnskap<br/>`1`: Driftsregnskap<br/>`2`: Balanseregnskap<br/>`3`: Driftsregnskap<br/>`4`: Investeringsregnskap<br/>`5`: Balanseregnskap |
| `funksjon_kapittel` | Se regnskapets veiledning for aktuelle koder for funksjon / kapittel | 4 | 33‑36 | STRING_TYPE |  |  |  |
| `art_sektor` | Se regnskapets veiledning for aktuelle koder for art / sektor | 3 | 37‑39 | STRING_TYPE |  |  |  |
| `belop` | Beløpet skal gis i 1000 kr og høyrejusteres.<br/>For bevilgnings-/resultatregnskap:<br/>* Utgifter (debet) rapporteres i positive verdier. <br/>* Inntekter (kredit) rapporteres i negative verdier (med minustegnet i posisjonen foran beløpet).<br/>For balanseregnskap:<br/>* Eiendeler rapporteres i positive verdier<br/>* Gjel | 9 | 40‑48 | INTEGER_TYPE | ☑️ |  |  |
