//file:noinspection SpellCheckingInspection
package no.ssb.kostra.xsd

import no.ssb.kostra.barn.convert.KostraBarnevernConverter
import no.ssb.kostra.barn.xsd.KostraMeldingType
import spock.lang.Specification

import java.time.LocalDate

import static no.ssb.kostra.TestUtils.getResourceAsString
import static no.ssb.kostra.barn.ValidationUtils.validate
import static no.ssb.kostra.barn.convert.KostraBarnevernConverter.marshallInstance
import static no.ssb.kostra.barn.convert.KostraBarnevernConverter.unmarshallXml

class XmlMappingSpec extends Specification {

    def XML_FILE = "testfiler/15F/testfile_all_fields_set.xml"

    def "when given invalid XML, expect validate() == false"() {
        expect:
        !validate("~xml~")
    }

    def "when given valid XML, expect validate() == true"() {
        expect:
        validate(getResourceAsString(XML_FILE))
    }

    def "when marshalling to XML, validate() == true"() {
        expect:
        def barnevernType = unmarshallXml(getResourceAsString(XML_FILE))
        and:
        validate(marshallInstance(barnevernType))
    }

    def "when deserializing valid XML, all props are set"() {
        when:
        def root = unmarshallXml(getResourceAsString(XML_FILE))

        then:
        noExceptionThrown()
        and:
        null != root
        and:
        verifyAll(root) {
            verifyAll(root.avgiver) {
                "958935420" == it.organisasjonsnummer
                2020 == it.versjon
                "0301" == it.kommunenummer
                "Oslo" == it.kommunenavn
            }

            1 == it.individ.size()
            verifyAll(it.individ[0]) {

                "i1" == it.id
                LocalDate.parse("2020-02-10") == it.startDato
                LocalDate.parse("2020-06-17") == it.sluttDato
                "J1" == it.journalnummer
                "01011088194" == it.fodselsnummer
                "123456123456" == it.duFnummer
                "02" == it.bydelsnummer
                "Gamle Oslo" == it.bydelsnavn
                "42" == it.distriktsnummer
                "Sara Saksbehandler" == it.saksbehandler
                "1" == it.avslutta3112

                1 == it.flytting.size()
                verifyAll(it.flytting[0]) {
                    "42" == it.id
                    LocalDate.parse("2020-02-11") == it.sluttDato

                    null != it.arsakFra
                    verifyAll(it.arsakFra) {
                        "1.1.1" == it.kode
                        "Presisering" == it.presisering
                    }

                    null != it.flyttingTil
                    verifyAll(it.flyttingTil) {
                        "1" == it.kode
                        "Presisering" == it.presisering
                    }
                }

                1 == it.tiltak.size()
                verifyAll(it.tiltak[0]) {
                    "T2" == it.id
                    LocalDate.parse("2020-02-10") == it.startDato
                    LocalDate.parse("2020-04-10") == it.sluttDato

                    verifyAll(it.lovhjemmel) {
                        "BVL" == it.lov
                        "4" == it.kapittel
                        "12" == it.paragraf
                        "1" == it.ledd
                        "Punkt" == it.punktum
                    }

                    2 == it.jmfrLovhjemmel.size()

                    verifyAll(it.jmfrLovhjemmel[0]) {
                        "BVL" == it.lov
                        "4" == it.kapittel
                        "8" == it.paragraf
                        "2" == it.ledd
                    }

                    verifyAll(it.kategori) {
                        "1.99" == it.kode
                        "Presisering" == it.presisering
                    }

                    null != it.opphevelse
                    verifyAll(it.opphevelse) {
                        "1" == it.kode
                        "Presisering" == it.presisering
                    }
                }

                1 == it.plan.size()
                verifyAll(it.plan[0]) {
                    "P1" == it.id
                    LocalDate.parse("2020-02-11") == it.startDato
                    LocalDate.parse("2020-06-17") == it.sluttDato
                    LocalDate.parse("2020-02-12") == it.evaluertDato
                    "1" == it.plantype
                }

                1 == melding.size()
                verifyAll(melding[0] as KostraMeldingType) {

                    "M1" == it.id
                    LocalDate.parse("2020-02-10") == it.startDato
                    LocalDate.parse("2020-02-17") == it.sluttDato
                    "1" == it.konklusjon

                    1 == it.melder.size()
                    verifyAll(melder[0]) {
                        "22" == it.kode
                        "Presiseringstekst" == it.presisering
                    }

                    1 == it.saksinnhold.size()
                    verifyAll(it.saksinnhold[0]) {
                        "18" == it.kode
                        "Presiseringstekst" == it.presisering
                    }

                    null != it.undersokelse
                    verifyAll(it.undersokelse) {
                        "U1" == it.id
                        LocalDate.parse("2020-02-17") == it.startDato
                        LocalDate.parse("2020-06-17") == it.sluttDato
                        "1" == it.konklusjon
                        "Presiseringstekst" == it.presisering

                        1 == it.vedtaksgrunnlag.size()
                        verifyAll(it.vedtaksgrunnlag[0]) {
                            "1" == it.kode
                            "Presiseringstekst" == it.presisering
                        }
                    }
                }
            }
        }
    }
}
