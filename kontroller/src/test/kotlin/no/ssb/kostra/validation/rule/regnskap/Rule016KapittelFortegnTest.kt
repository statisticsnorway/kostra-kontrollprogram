package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoArgsTest

class Rule016KapittelFortegnSpec : BehaviorSpec({
    include(
        validationRuleNoArgsTest(
            sut = Rule016KapittelFortegn(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "wrong skjema",
                kostraRecordsInTest("0A", 841, 0)
            ),
            ForAllRowItem(
                "correct skjema, wrong kapittel",
                kostraRecordsInTest("0B", 51, 0)
            ),
            ForAllRowItem(
                "correct skjema, correct kapittel (5900), correct belop = 0",
                kostraRecordsInTest("0B", 5900, 0)
            ),
            ForAllRowItem(
                "correct skjema, correct kapittel (5900), correct belop = 1",
                kostraRecordsInTest("0B", 5900, 0)
            ),
            ForAllRowItem(
                "correct skjema, correct kapittel (5900), wrong belop = -1",
                kostraRecordsInTest("0B", 5900, -1),
                expectedErrorMessage = "Kapittel 5900 Merforbruk i driftsregnskapet (Beløp = -1) og " +
                        "kapittel 5970 Udekket beløp i investeringsregnskapet  skal bare kunne rapporteres som " +
                        "positive tall, altså tall større eller lik 0. Eventuelt mindreforbruk i driftsregnskapet " +
                        "eller udisponert i investeringsregnskapet må posteres etter reglene i Forskrift om " +
                        "økonomiplan, årsbudsjett, årsregnskap og årsberetning for kommuner og fylkeskommuner " +
                        "mv. , henholdsvis paragrafene 4.3 og 4.6"

            ),
            ForAllRowItem(
                "correct skjema, correct kapittel (5970), correct belop = 0",
                kostraRecordsInTest("0B", 5970, 0)
            ),
            ForAllRowItem(
                "correct skjema, correct kapittel (5970), correct belop = 1",
                kostraRecordsInTest("0B", 5970, 1)
            ),

            ForAllRowItem(
                "correct skjema, correct kapittel (5970), wrong belop = -1",
                kostraRecordsInTest("0B", 5970, -1),
                expectedErrorMessage = "Kapittel 5900 Merforbruk i driftsregnskapet  og " +
                        "kapittel 5970 Udekket beløp i investeringsregnskapet (Beløp = -1) skal bare kunne rapporteres " +
                        "som positive tall, altså tall større eller lik 0. Eventuelt mindreforbruk i driftsregnskapet " +
                        "eller udisponert i investeringsregnskapet må posteres etter reglene i Forskrift om " +
                        "økonomiplan, årsbudsjett, årsregnskap og årsberetning for kommuner og fylkeskommuner " +
                        "mv. , henholdsvis paragrafene 4.3 og 4.6"
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            skjema: String,
            kapittel: Int,
            belop: Int
        ) = mapOf(
            RegnskapConstants.FIELD_SKJEMA to skjema,
            RegnskapConstants.FIELD_FUNKSJON to "$kapittel",
            RegnskapConstants.FIELD_BELOP to "$belop"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions).asList()
    }
}