# Kravspesifikasjon til kontrollene for Økonomisk sosialhjelp

## <a name="omfang">Omfang</a>
Kontaktepost: **ssb-sosial@ssb.no**

Generell beskrivelse: Sosialstatistikken er en individstatistikk som hentes inn fra alle kommunale sosialtjenester som et filuttrekk fra kommunens fagsystem.

### <a name="kontrollrapport">Advarsler og feil</a>
Kontrollprogrammet lager en kontrollrapport som er en liste med advarsler og feil. Advarsler skal kommunens sosialtjeneste sjekke og om mulig rette opp. Feil som hindrer innsending må rettes for å få sendt inn fil. I kontrollrapporten vil identifikasjon av hvilket individ kontrollen slår ut for være journalnummer. Journalnummer kan derfor ikke være noe som kan identifisere individet, for eksempel fødsels- og personnummer eller DUF-nummer.

## Filbeskrivelse
Filbeskrivelsen består av en yaml-fil som beskriver hvert felt med tilhørende informasjon. [Lenke til filbeskrivelsen](/kontroller/src/main/resources/sosialhjelp_11_filedescription.yaml) 

## Kontroller
I forbindelse med kontrollering så blir noen variabler sendt med fra portalen og blir benyttet i noen av kontrollene. Disse er:
* <a name="rapporteringsar">**rapporteringsår**</a>, for eksempel **2023**
* <a name="kommunenummer">**kommunenummer**</a>, for eksempel **0301**
* <a name="bydelsnummer">**bydelsnummer**</a>, for eksempel **15**

#### <a name="kontroll_03_Kommunenummer">Avgiver Kontroll 3: Kommunenummer</a>

Gitt at en har en datafil for Økonomisk sosialhjelp med **KOMMUNE_NR** og [**kommunenummer**](#kommunenummer) <br/>
når **KOMMUNE_NR** og **kommunenummer** er forskjellige<br/>
så gi en :no_entry:**FEIL** med meldingen "Korrigér kommunenummer. Fant '{KOMMUNE_NR}', forventet '{**kommunenummer**}'
[Kode](../kontroller/src/main/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule003Kommunenummer.kt)
[Test](../kontroller/src/test/kotlin/no/ssb/kostra/validation/rule/sosial/rule/Rule003KommunenummerTest.kt)