package no.ssb.kostra.program

import io.kotest.matchers.string.shouldContain
import io.kotest.core.spec.style.BehaviorSpec
import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@MicronautTest(environments = [Environment.CLI, Environment.TEST])
class KostraKontrollprogramCommandSpec(
    applicationContext: ApplicationContext
) : BehaviorSpec({

    Given("kostra-kontrollprogram-konsoll") {
        When("invocation with -v") {
            val baos = ByteArrayOutputStream()
            val origStream = System.out

            System.setOut(PrintStream(baos))

            val args = arrayOf("-v")
            PicocliRunner.run(KostraKontrollprogramCommand::class.java, applicationContext, *args)

            Then("should display greeting") {
                baos.toString() shouldContain "Hi!"
            }

            System.setOut(origStream)
        }

        When("other") {
            // add more tests...
        }
    }
})