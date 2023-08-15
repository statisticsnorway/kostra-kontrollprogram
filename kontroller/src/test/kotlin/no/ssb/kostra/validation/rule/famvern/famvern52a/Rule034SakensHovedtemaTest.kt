package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule034SakensHovedtemaTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule034SakensHovedtema(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "no match",
                kostraRecordInTest("X", " "),
            ),
            ForAllRowItem(
                "all good",
                kostraRecordInTest("1", "01"),
            ),
            ForAllRowItem(
                "invalid value",
                kostraRecordInTest("1", "  "),
                expectedErrorMessage = "Det er krysset av for at saken er avsluttet i rapporteringsåret, " +
                        "men ikke fylt ut hovedtema for saken, eller feltet har ugyldig format. Fant '  ', " +
                        "forventet én av: [01=styrke parforholdet, 02=avklare/avslutte parforholdet, " +
                        "03=samlivsbrudd i familien, 04=samspillvansker, 05=barnets opplevelse av sin livssituasjon, " +
                        "06=barnets situasjon i foreldrenes konflikt, 07=bostedsavklaring/ samvær, " +
                        "08=foreldrerollen, 09=foreldre-barn-relasjonen, 10=flergenerasjons- problematikk, " +
                        "11=samarb. om felles barn (foreldre bor ikke sammen), 12=særkullsbarn og/eller ny familie, " +
                        "13=kultur-/minoritetsspørsmål, 14=tvangsekteskap, 15=bruk av rusmidler, " +
                        "16=sykdom / nedsatt funksjonsevne, 17=fysisk / psykisk vold / seksuelt misbruk, " +
                        "18=annen alvorlig hendelse].",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(status: String, hovedtema: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52aColumnNames.STATUS_ARETSSL_A_COL_NAME to status,
                    Familievern52aColumnNames.HOVEDTEMA_A_COL_NAME to hovedtema
                )
            )
        )
    }
}
