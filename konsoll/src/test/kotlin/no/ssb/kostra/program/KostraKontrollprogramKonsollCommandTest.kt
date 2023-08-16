package no.ssb.kostra.program

import io.kotest.matchers.string.shouldContain
import io.kotest.core.spec.style.BehaviorSpec
import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class KostraKontrollprogramCommandSpec: BehaviorSpec({

    Given("kostra-kontrollprogram-konsoll") {
        val ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        When("invocation with -v") {
            val baos = ByteArrayOutputStream()
            System.setOut(PrintStream(baos))

            val args = arrayOf("-v")
            PicocliRunner.run(KostraKontrollprogramCommand::class.java, ctx, *args)

            Then("should display greeting") {
                baos.toString() shouldContain "Hi!"
            }
        }

        When("other") {
            // add more tests...
        }

        ctx.close()
    }
})