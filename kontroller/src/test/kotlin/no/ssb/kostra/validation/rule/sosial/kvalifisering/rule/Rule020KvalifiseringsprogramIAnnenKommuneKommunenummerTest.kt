package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Rule020KvalifiseringsprogramIAnnenKommuneKommunenummerTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule020KvalifiseringsprogramIAnnenKommuneKommunenummer(),
            forAllRows = listOf(
                ForAllRowItem(
                    "valid kvpKomm and kommnrKvpKomm",
                    kostraRecordInTest(1, "1106"),
                ),
                ForAllRowItem(
                    "valid kvpKomm and kommnrKvpKomm #2",
                    kostraRecordInTest(2, "1106"),
                ),
                ForAllRowItem(
                    "invalid kommnrKvpKomm",
                    kostraRecordInTest(1, "4242"),
                    "Det er svart '1=Ja' på om deltakeren kommer fra " +
                            "kvalifiseringsprogram i annen kommune, men kommunenummer ('4242') mangler eller " +
                            "er ugyldig. Feltet er obligatorisk å fylle ut.",
                ),
                ForAllRowItem(
                    "empty kommnrKvpKomm",
                    kostraRecordInTest(1, " ".repeat(4)),
                    "Det er svart '1=Ja' på om deltakeren kommer fra " +
                            "kvalifiseringsprogram i annen kommune, men kommunenummer ('${" ".repeat(4)}') " +
                            "mangler eller er ugyldig. Feltet er obligatorisk å fylle ut.",
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            kvpKomm: Int,
            kommnrKvpKomm: String
        ) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(
                    KVP_KOMM_COL_NAME to kvpKomm.toString(),
                    KOMMNR_KVP_KOMM_COL_NAME to kommnrKvpKomm
                )
            )
        )
    }
}
