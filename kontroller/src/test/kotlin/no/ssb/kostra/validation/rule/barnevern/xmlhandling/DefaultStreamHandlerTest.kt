package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.ssb.kostra.BarnevernTestData.barnevernTypeInTest
import no.ssb.kostra.BarnevernTestData.kostraIndividInTest
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.marshallInstance
import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.DefaultStreamHandler.Companion.avgiverFileError
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.DefaultStreamHandler.Companion.individFileError

class DefaultStreamHandlerTest : BehaviorSpec({

    Given("DefaultStreamHandler") {

        lateinit var avgiverXmlElementHandler: XmlElementHandler<KostraAvgiverType>
        lateinit var individXmlElementHandler: XmlElementHandler<KostraIndividType>
        lateinit var sut: DefaultStreamHandler

        beforeContainer {
            avgiverXmlElementHandler = mockk()
            individXmlElementHandler = mockk()
            sut = DefaultStreamHandler(
                avgiverXmlElementHandler = avgiverXmlElementHandler,
                individXmlElementHandler = individXmlElementHandler
            )

            every {
                avgiverXmlElementHandler.handleXmlElement(any(), any())
            } answers {
                emptyList<ValidationReportEntry>() to null
            }

            every {
                individXmlElementHandler.handleXmlElement(any(), any())
            } answers {
                emptyList<ValidationReportEntry>() to null
            }
        }

        When("no errors") {
            val result = sut.handleStream(
                marshallInstance(kostraIndividInTest).byteInputStream(),
                argumentsInTest, { _ -> run {} }, { _ -> run {} }
            )

            Then("result should be as expected") {
                result.shouldNotBeNull()
                result.shouldBeEmpty()
            }
        }

        When("avgiverXmlElementHandler throws exception") {
            every {
                avgiverXmlElementHandler.handleXmlElement(any(), any())
            } answers {
                throw NullPointerException()
            }

            val result = sut.handleStream(
                marshallInstance(barnevernTypeInTest).byteInputStream(),
                argumentsInTest, { _ -> run {} }, { _ -> run {} }
            )

            verify { individXmlElementHandler.handleXmlElement(any(), any()) }

            Then("result should be as expected") {
                result.shouldNotBeNull()
                result.shouldContain(avgiverFileError)
            }
        }

        When("individXmlElementHandler throws exception") {
            every {
                individXmlElementHandler.handleXmlElement(any(), any())
            } answers {
                throw NullPointerException()
            }

            val result = sut.handleStream(
                marshallInstance(barnevernTypeInTest).byteInputStream(),
                argumentsInTest, { _ -> run {} }, { _ -> run {} }
            )

            verify { avgiverXmlElementHandler.handleXmlElement(any(), any()) }

            Then("result should be as expected") {
                result.shouldNotBeNull()
                result.shouldContain(individFileError)
            }
        }
    }
})
