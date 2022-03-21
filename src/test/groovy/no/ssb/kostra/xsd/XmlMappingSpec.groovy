//file:noinspection SpellCheckingInspection
package no.ssb.kostra.xsd

import no.ssb.kostra.barn.convert.BarnevernConverter
import no.ssb.kostra.barn.xsd.KostraMeldingType
import spock.lang.Specification

import static no.ssb.kostra.TestUtils.getResourceAsString
import static no.ssb.kostra.barn.validation.ValidationUtils.validate

class XmlMappingSpec extends Specification {

    def XML_FILE = "testfiler/15F/testfile_all_fields_set.xml"

    def "when given invalid XML, expect false"() {
        expect:
        !validate("~xml~")
    }

    def "when given valid XML, expect true"() {
        expect:
        validate(getResourceAsString(XML_FILE))
    }

    def "when deserializing valid XML, all props are set"() {
        when:
        def root = BarnevernConverter.unmarshallXml(
                getResourceAsString(XML_FILE))

        then:
        noExceptionThrown()
        and:
        null != root
        and:
        verifyAll(root) {
            null != it.avgiver
            verifyAll(root.avgiver) {
                null != it.organisasjonsnummer
                null != it.versjon
                null != it.kommunenummer
                null != it.kommunenavn
            }

            null != it.individ
            1 == it.individ.size()
            verifyAll(it.individ[0]) {

                null != it.id
                null != it.startDato
                null != it.sluttDato
                null != it.journalnummer
                null != it.fodselsnummer
                null != it.duFnummer
                null != it.bydelsnummer
                null != it.bydelsnavn
                null != it.distriktsnummer
                null != it.saksbehandler
                null != it.avslutta3112

                null != it.flytting
                1 == it.flytting.size()
                verifyAll(it.flytting[0]) {
                    null != it.id
                    null != it.sluttDato

                    null != it.arsakFra
                    verifyAll(it.arsakFra) {
                        null != it.kode
                        null != it.presisering
                    }

                    null != it.flyttingTil
                    verifyAll(it.flyttingTil) {
                        null != it.kode
                        null != it.presisering
                    }
                }

                null != it.tiltak
                1 == it.tiltak.size()
                verifyAll(it.tiltak[0]) {
                    null != it.id
                    null != it.startDato
                    null != it.sluttDato

                    null != it.lovhjemmel
                    verifyAll(it.lovhjemmel) {
                        null != it.lov
                        null != it.kapittel
                        null != it.paragraf
                        null != it.ledd
                        null != it.punktum
                    }

                    null != it.jmfrLovhjemmel
                    2 == it.jmfrLovhjemmel.size()

                    null != it.kategori
                    verifyAll(it.kategori) {
                        null != it.kode
                        null != it.presisering
                    }

                    null != it.opphevelse
                    verifyAll(it.opphevelse) {
                        null != it.kode
                        null != it.presisering
                    }
                }

                null != it.plan
                1 == it.plan.size()
                verifyAll(it.plan[0]) {
                    null != it.id
                    null != it.startDato
                    null != it.sluttDato
                    null != it.evaluertDato
                    null != it.plantype
                }

                null != it.melding
                1 == melding.size()
                verifyAll(melding[0] as KostraMeldingType) {
                    null != it.id
                    null != it.startDato
                    null != it.sluttDato
                    null != it.konklusjon

                    null != it.melder
                    1 == it.melder.size()
                    verifyAll(melder[0]) {
                        null != it.presisering
                        null != it.kode
                    }

                    null != it.saksinnhold
                    1 == it.saksinnhold.size()
                    verifyAll(it.saksinnhold[0]) {
                        null != it.presisering
                        null != it.kode
                    }

                    null != it.undersokelse
                    verifyAll(it.undersokelse) {
                        null != it.presisering
                        null != it.id
                        null != it.startDato
                        null != it.sluttDato
                        null != it.konklusjon

                        null != it.vedtaksgrunnlag
                        1 == it.vedtaksgrunnlag.size()
                        verifyAll(it.vedtaksgrunnlag[0]) {
                            null != it.presisering
                            null != it.kode
                        }
                    }
                }
            }
        }
    }
}
