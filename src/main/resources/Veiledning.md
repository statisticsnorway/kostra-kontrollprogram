# **Veiledning for kontrollprogrammet - KOSTRA**

**Publisert: 13. juli 2020**

**Bruksanvisning for kontrollprogrammet:**

1. Sørg for at datamaskinen har _Java utviklingsmiljø (JDK)_ installert. Minimum versjon: _**13.0.2 (**__**[OpenJDK](https://jdk.java.net/)**_  eller eventuelt _**[OracleJDK](https://www.oracle.com/java/technologies/javase-downloads.html)**__**)**_.
2. Lagre filen _kontrollprogram.jar_ i en katalog på datamaskinens filsystem.
3. Åpne et _kommandolinjevindu_, og naviger til katalogen nevnt i punkt 2.
4. Avhengig av hvilken type filuttrekk du vil kontrollere, skriv inn et av følgende:


## **1. Kontroll av års baserte filuttrekk. Gjelder for skjemanummer 0A, 0B, 0C, 0D, 0M, 0N, 0P, 0Q, 11F, 11CF, 15F, 52AF, 52BF, 53F og 55F:**

* **&lt;rapporteringsår&gt; =**  rapporteringsår (obligatorisk), det året dataene i filuttrekket gjelder for, f.eks. 2020.
* **&lt;skjemanummer&gt;**  = 0A, 0B, 0C etc..
* **&lt;region&gt;**  = _kommunenummer + bydelsnummer_ eller regionsnummer (obligatorisk), skal være 6 siffer, f.eks. 030100, 030112, 500100 eller 667600.
* **&lt;navn&gt;**  = navnet til den organisasjonen dataene gjelder for (valgfri).
* **&lt;kildefil&gt;**  = fullstendig sti (inkludert filnavn) (obligatorisk) til kildefilen/filuttrekket som skal kontrolleres, sendes inn vha.  **&lt;**. Hvis stien eller filnavnet inneholder mellomrom/blanke tegn må &lt;kildefil&gt; omsluttes av _hermetegn (&quot;)_ .
* **&lt;rapportfil&gt;**  = fullstendig sti (inkludert filnavn) (valgfri) til en fil som kontrollprogrammet kan skrive _kontrollrapporten_ til, vha.  **&gt;**. Hvis stien eller filnavnet inneholder mellomrom/blanke tegn må &lt;rapportfil&gt; omsluttes av _hermetegn (&quot;)_ .

java -jar kontrollprogram.jar -y &lt;rapporteringsår&gt; -s &lt;skjemanummer&gt; -r &lt;region&gt; -n &lt;navn&gt;  **&lt;**  &lt;kildefil&gt;  **&gt;**  &lt;rapportfil&gt;

F.eks.:
 java -jar kontrollprogram.jar -y 2020 -s 0A -r 030100 -n &quot;OSLO KOMMUNE&quot;  **&lt;**  &quot;C:\sti\til\kildefil\BEV\_030100.dat&quot;  **&gt;**  &quot;C:\sti\til\rapport fil\kontrollrapport\_0a.html&quot;

java -jar kontrollprogram.jar -y 2020 -s 11F -r 030100 -n &quot;OSLO KOMMUNE&quot;  **&lt;**  &quot;C:\sti\til\kildefil\Sosial\_030100.dat&quot;  **&gt;**  &quot;C:\sti\til\rapport fil\kontrollrapport\_11.html&quot;

java -jar kontrollprogram.jar -y 2020 -s 52AF -r 667600 -n &quot;BUFDIR&quot;  **&lt;**  &quot;C:\sti\til\kilde fil\FILUTTREKK.dat&quot;  **&gt;**  &quot;C:\sti\til\rapport fil\kontrollrapport\_52af.html&quot;

Programmet benytter standard in/out for henholdsvis kilde- og rapportfil.

## **2. Kontroll av årsbaserte filuttrekk for et gitt organisasjonsnummer. Gjelder for 0F, 0G, 0I, 0J, 0K og 0L:**

* **&lt;rapporteringsår&gt;** = rapporteringsår (obligatorisk), det året dataene i filuttrekket gjelder for, f.eks. 2020.
* **&lt;skjemanummer&gt;**  = 0I, 0J, 0K etc..
* **&lt;region&gt;**  = _kommunenummer + bydelsnummer_ eller regionsnummer (obligatorisk), skal være 6 siffer, f.eks. 030100, 030112, 500100 eller 667600.
* **&lt;organisasjonsnummer&gt;**  = organisasjonsnummer for den enheten som filuttrekket gjelder for, f.eks. 987654321
* **&lt;navn&gt;**  = navnet til den organisasjonen dataene gjelder for (valgfri).
* **&lt;kildefil&gt;**  = fullstendig sti (inkludert filnavn) (obligatorisk) til kildefilen/filuttrekket som skal kontrolleres, sendes inn vha.  **&lt;**. Hvis stien eller filnavnet inneholder mellomrom/blanke tegn må &lt;kildefil&gt; omsluttes av _hermetegn (&quot;)_ .
* **&lt;rapportfil&gt;**  = fullstendig sti (inkludert filnavn) (valgfri) til en fil som kontrollprogrammet kan skrive _kontrollrapporten_ til, vha.  **&gt;**. Hvis stien eller filnavnet inneholder mellomrom/blanke tegn må &lt;rapportfil&gt; omsluttes av _hermetegn (&quot;)_ .

java -jar kontrollprogram.jar -y &lt;rapporteringsår&gt; -s &lt;skjemanummer&gt; -r &lt;region&gt; -u &lt;organisasjonsnummer&gt; -n &lt;navn&gt;  **&lt;**  &lt;kildefil&gt;  **&gt;**  &lt;rapportfil&gt;

F.eks.:
 java -jar kontrollprogram.jar -y 2020 -s 0F -r 110300 -u 976993403 -n &quot;STAVANGER KIRKELIGE FELLESRÅD&quot;  **&lt;**  &quot;C:\sti\til\kildefil\BEV\_976993403.dat&quot;  **&gt;**  &quot;C:\sti\til\rapport fil\kontrollrapport\_0f\_976993403.html&quot;

java -jar kontrollprogram.jar -y 2020 -s 0J -r 030100 -u 987592567 -n &quot;OSLO HAVN KF&quot;  **&lt;**  &quot;C:\sti\til\kildefil\BAL\_987592567.dat&quot;  **&gt;**  &quot;C:\sti\til\rapport fil\kontrollrapport\_0j\_987592567.html&quot;

Programmet benytter standard in/out for henholdsvis kilde- og rapportfil.

## **3. Kontroll av årsbaserte filuttrekk for et helseforetak. Gjelder for 0X og 0Y:**

* **&lt;rapporteringsår&gt; = ** rapporteringsår (obligatorisk), det året dataene i filuttrekket gjelder for, f.eks. 2020.
* **&lt;skjemanummer&gt;**  = 0I, 0J, 0K etc..
* **&lt;region&gt;**  = regionsnummer (obligatorisk), skal være 6 siffer, f.eks. 030000, 040000, 050000, 120000 eller 990000.
* **&lt;foretaksnummer&gt;**  = organisasjonsnummer for det foretaket som filuttrekket gjelder for, f.eks. 987654321
* **&lt;organisasjonsnummer&gt;**  = kommaseparert liste (hvis flere enheter) med organisasjonsnumre for de(n) enheten(e) som filuttrekket gjelder for, f.eks. 976543218,965432187,965432187
* **&lt;navn&gt;**  = navnet til den organisasjonen dataene gjelder for (valgfri).
* **&lt;kildefil&gt;**  = fullstendig sti (inkludert filnavn) (obligatorisk) til kildefilen/filuttrekket som skal kontrolleres, sendes inn vha.  **&lt;**. Hvis stien eller filnavnet inneholder mellomrom/blanke tegn må &lt;kildefil&gt; omsluttes av _hermetegn (&quot;)_ .
* **&lt;rapportfil&gt;**  = fullstendig sti (inkludert filnavn) (valgfri) til en fil som kontrollprogrammet kan skrive _kontrollrapporten_ til, vha.  **&gt;**. Hvis stien eller filnavnet inneholder mellomrom/blanke tegn må &lt;rapportfil&gt; omsluttes av _hermetegn (&quot;)_ .

java -jar kontrollprogram.jar -y &lt;rapporteringsår&gt; -s &lt;skjemanummer&gt; -r &lt;region&gt; -c &lt;foretaksnummer&gt; -u &lt;organisasjonsnummer&gt; -n &lt;navn&gt;  **&lt;**  &lt;kildefil&gt;  **&gt;**  &lt;rapportfil&gt;

F.eks.:
 java -jar kontrollprogram.jar -y 2020 -s &quot;0X&quot; -y &quot;2020&quot; -r &quot;040000&quot; -n &quot;HELSE MIDT-NORGE RHF&quot; -c &quot;983658776&quot; -u &quot;985831580,996246507,983658776,918098275&quot;  **&lt;**  &quot;C:\sti\til\kildefil\RES\_983658776.dat&quot;  **&gt;**  &quot;C:\sti\til\rapportfil\kontrollrapport\_0x\_983658776.html&quot;

java -jar kontrollprogram.jar -y 2020 -s &quot;0X&quot; -r &quot;120000&quot; -n &quot;HELSE SØR-ØST RHF&quot; -c &quot;991324968&quot; -u &quot;991324968&quot;  **&lt;**  &quot;C:\sti\til\kildefil\RES\_991324968.dat&quot;  **&gt;**  &quot;C:\sti\til\rapportfil\kontrollrapport\_0x\_991324968.html&quot;

Programmet benytter standard in/out for henholdsvis kilde- og rapportfil.

## **4. Kontroll av kvartalsbaserte filuttrekk. Gjelder for skjemanummer 0AK\* og 0BK\*:**

* **&lt;rapporteringsår&gt; = ** rapporteringsår (obligatorisk), det året dataene i filuttrekket gjelder for, f.eks. 2020.
* **&lt;skjemanummer&gt;**  = 0AK1, 0BK1, 0BK4 etc..
* **&lt;kvartal&gt;**  = det kvartalet innsendingen gjelder for, f.eks. 1, 2, 3 eller 4.
* **&lt;region&gt;**  = _kommunenummer + bydelsnummer _(obligatorisk), skal være 6 siffer, f.eks. 030100, 340000 eller 500100.
* **&lt;navn&gt;**  = navnet til den organisasjonen dataene gjelder for (valgfri).
* **&lt;kildefil&gt;**  = fullstendig sti (inkludert filnavn) (obligatorisk) til kildefilen/filuttrekket som skal kontrolleres, sendes inn vha.  **&lt;**. Hvis stien eller filnavnet inneholder mellomrom/blanke tegn må &lt;kildefil&gt; omsluttes av _hermetegn (&quot;)_ .
* **&lt;rapportfil&gt;**  = fullstendig sti (inkludert filnavn) (valgfri) til en fil som kontrollprogrammet kan skrive _kontrollrapporten_ til, vha.  **&gt;**. Hvis stien eller filnavnet inneholder mellomrom/blanke tegn må &lt;rapportfil&gt; omsluttes av _hermetegn (&quot;)_ .

java -jar kontrollprogram.jar -y &lt;rapporteringsår&gt; -s &lt;skjemanummer&gt; -q &lt;kvartal&gt; -r &lt;region&gt; -n &lt;navn&gt;  **&lt;**  &lt;kildefil&gt;  **&gt;**  &lt;rapportfil&gt;

F.eks.:
 java -jar kontrollprogram.jar -y 2020 -s 0AK1 -q 1 -r 030100 -n &quot;OSLO KOMMUNE&quot;  **&lt;**  &quot;C:\sti\til\kildefil\BEV\_030100\_K1.dat&quot;  **&gt;**  &quot;C:\sti\til\rapport fil\kontrollrapport\_0ak1.html&quot;

Programmet benytter standard in/out for henholdsvis kilde- og rapportfil.

4