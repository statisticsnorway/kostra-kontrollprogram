package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VedtaksgrunnlagType", propOrder = ["presisering"])
data class KostraVedtaksgrunnlagType(

    @field:XmlElement(name = "Presisering")
    var presisering: String? = null,

    @field:XmlAttribute(name = "Kode", required = true)
    var kode: String? = null
) {
    companion object {

        fun createRandomVedtaksgrunnlagType(): KostraVedtaksgrunnlagType =
            KostraVedtaksgrunnlagType(null, (1..27).random().toString())
    }

    /*
    1 = Foreldres somatiske sykdom
    2 = Foreldres psykiske problem/ lidelse
    3 = Foreldres rusmisbruk
    4 = Foreldres manglende foreldreferdigheter
    5 = Foreldres kriminalitet
    6 = Høy grad av konflikt hjemme
    7 = Vold i hjemmet/ barnet vitne til vold i nære relasjoner
    8 = Barnet utsatt for vanskjøtsel (Barnet overlatt til seg selv, dårlig kosthold, dårlig hygiene)
    9 = Barnet utsatt for fysisk vold
    10 = Barnet utsatt for psykisk vold
    11 = Barnet utsatt for seksuelle overgrep
    12 = Barnet mangler omsorgsperson
    13 = Barnet har nedsatt funksjonsevne
    14 = Barnets psykiske problem/lidelse
    15 = Barnets rusmisbruk
    16 = Barnets atferd/ kriminalitet
    17 = Barnets relasjonsvansker (mistanke om eller diagnostiserte tilknytningsvansker, problematikk knyttet til samspillet mellom barn og omsorgspersoner)
    18 = Andre forhold ved foreldre/ familien (krever presisering) (Denne kategorien skal kunne benyttes dersom ingen av kategoriene 1-27 passer.)
    19 = Andre forhold ved barnets situasjon (krever presisering) (Denne kategorien skal kunne benyttes dersom ingen av kategoriene 1-27 passer.)</xs:documentation>
    20 = Foreldres manglende beskyttelse av barnet
    21 = Foreldres manglende stimulering og regulering av barnet
    22 = Foreldres manglende sensitivitet og følelsesmessige tilgjengelighet for barnet
    23 = Foreldres manglende oppfølging av barnets behov for barnehage, skole og pedagogiske tjenester
    24 = Konflikt mellom foreldre som ikke bor sammen
    25 = Barnets atferd
    26 = Barnets kriminelle handlinger
    27 = Barnet utsatt for menneskehandel
    */
}
