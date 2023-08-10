package no.ssb.kostra.area.sosial.sosialhjelp

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.SosialConstants
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.*
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.rule.RuleTestData
import java.time.LocalDate
import java.time.Year

class SosialhjelpMainTest : BehaviorSpec({
    Given("KvalifiseringMain") {
        forAll(
            row(
                "validating an invalid record string",
                " ".repeat(SosialhjelpFieldDefinitions.fieldLength + 10),
                1,
                1
            ),
            row(
                "validating an empty record string",
                " ".repeat(SosialhjelpFieldDefinitions.fieldLength),
                43,
                17
            ),
            row(
                "validating a valid record string",
                recordStringInTest("22"),
                43,
                0
            ),
            row(
                "validating a valid record string with invalid data",
                recordStringInTest("XX"),
                43,
                1
            ),
        ) { description, inputFileContent, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = SosialhjelpMain(
                    argumentsInTest(
                        inputFileContent = inputFileContent
                    )
                ).validate()

                Then("validationResult should be as expected") {
                    assertSoftly(validationResult) {
                        numberOfControls shouldBe expectedNumberOfControls
                        reportEntries.size shouldBe expectedReportEntriesSize
                    }
                }
            }
        }
    }
}) {
    companion object {
        private val fodselsnummer = RandomUtils.generateRandomSSN(
            LocalDate.now().minusYears(40),
            LocalDate.now().minusYears(21)
        )

        private fun recordStringInTest(version: String) =
            " ".repeat(fieldDefinitions.last().to)
                .toKostraRecord(1, fieldDefinitions)
                .plus(
                    mapOf(
                        SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                        SosialhjelpColumnNames.KOMMUNE_NR_COL_NAME to RuleTestData
                            .argumentsInTest.region.municipalityIdFromRegion(),
                        SosialhjelpColumnNames.BYDELSNR_COL_NAME to "  ",
                        SosialhjelpColumnNames.VERSION_COL_NAME to version,
                        SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME to "~journalNummer~",
                        SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME to fodselsnummer,
                        SosialhjelpColumnNames.KJONN_COL_NAME to "1",
                        SosialhjelpColumnNames.STATUS_COL_NAME to "1",
                        SosialhjelpColumnNames.EKTSTAT_COL_NAME to "1",
                        SosialhjelpColumnNames.HAR_BARN_UNDER_18_COL_NAME to "1",
                        SosialhjelpColumnNames.ANT_BARN_UNDER_18_COL_NAME to "1",
                        SosialhjelpColumnNames.AVSL_DATO_COL_NAME to " ".repeat(6),

                        SosialhjelpColumnNames.VKLO_COL_NAME to fieldDefinitions
                            .byColumnName(SosialhjelpColumnNames.VKLO_COL_NAME).codeList.first().code,
                        SosialhjelpColumnNames.ARBSIT_COL_NAME to fieldDefinitions
                            .byColumnName(SosialhjelpColumnNames.ARBSIT_COL_NAME).codeList.first().code,
                        SosialhjelpColumnNames.BIDRAG_COL_NAME to "1000",
                        SosialhjelpColumnNames.GITT_OKONOMIRAD_COL_NAME to fieldDefinitions
                            .byColumnName(
                                SosialhjelpColumnNames.GITT_OKONOMIRAD_COL_NAME
                            ).codeList.first().code,
                        SosialhjelpColumnNames.FAAT_INDIVIDUELL_PLAN_COL_NAME to fieldDefinitions
                            .byColumnName(
                                SosialhjelpColumnNames.FAAT_INDIVIDUELL_PLAN_COL_NAME
                            ).codeList.first().code,
                        SosialhjelpColumnNames.BOSIT_COL_NAME to fieldDefinitions
                            .byColumnName(SosialhjelpColumnNames.BOSIT_COL_NAME).codeList.first().code,
                        SosialhjelpColumnNames.BIDRAG_1_COL_NAME to "1000",
                        SosialhjelpColumnNames.VILKARSOSLOV_COL_NAME to fieldDefinitions
                            .byColumnName(
                                SosialhjelpColumnNames.VILKARSOSLOV_COL_NAME
                            ).codeList.last().code,
                        SosialhjelpColumnNames.VILKARSAMEKT_COL_NAME to fieldDefinitions
                            .byColumnName(
                                SosialhjelpColumnNames.VILKARSAMEKT_COL_NAME
                            ).codeList.last().code,

                        *((1..12).map {
                            "${SosialConstants.MONTH_PREFIX}$it" to it.toString().padStart(2, '0')
                        }).toTypedArray(),
                    )
                )
                .toRecordString()

        private fun argumentsInTest(
            inputFileContent: String
        ): KotlinArguments = KotlinArguments(
            aargang = (Year.now().value - 1).toString(),
            region = RuleTestData.argumentsInTest.region.municipalityIdFromRegion(),
            skjema = "11CF",

            inputFileContent = inputFileContent
        )
    }
}
/*
The following assertion for ValidationResult(reportEntries=[
ValidationReportEntry(severity=ERROR, caseworker=, journalId=, individId=, contextId=,
ruleName=Kontroll 14: Viktigste kilde til livsopphold. Gyldige verdier, messageText=Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret skal oppgis. Fant '( )', forventet én av '([1=Arbeidsinntekt, 2=Kursstønad/lønn i arbeidsmarkedstiltak, 3=Trygd/pensjon, 4=Stipend/lån, 5=Sosialhjelp, 6=Introduksjonsstøtte, 7=Ektefelle/samboers arbeidsinntekt, 8=Kvalifiseringsstønad, 9=Annen inntekt])'., lineNumbers=[]),

ValidationReportEntry(severity=ERROR, caseworker=, journalId=, individId=, contextId=,
ruleName=Kontroll 25: Arbeidssituasjon. Gyldige koder., messageText=Mottakerens arbeidssituasjon ved siste kontakt med sosial-/NAV-kontoret er ikke fylt ut, eller feil kode er benyttet. Utfylt verdi er '(  =Ukjent)'. Feltet er obligatorisk å fylle ut., lineNumbers=[]),

ValidationReportEntry(severity=ERROR, caseworker=, journalId=, individId=, contextId=,
ruleName=Kontroll 31: Stønadssum på kr 200,- eller lavere., messageText=Det samlede stønadsbeløpet (summen (42) av bidrag (     42) og lån (       )) som mottakeren har fått i løpet av rapporteringsåret er lik/lavere enn Statistisk sentralbyrås kontrollgrense på kr. (200),-., lineNumbers=[]),

ValidationReportEntry(severity=ERROR, caseworker=, journalId=, individId=, contextId=,
ruleName=Kontroll 32: Økonomiskrådgivning. Gyldige koder., messageText=Det er ikke krysset av for om mottakeren er gitt økonomisk rådgiving i forbindelse med utbetaling av økonomisk sosialhjelp. Utfylt verdi er '( =Ukjent)'. Feltet er obligatorisk å fylle ut., lineNumbers=[]),

ValidationReportEntry(severity=ERROR, caseworker=, journalId=, individId=, contextId=,
ruleName=Kontroll 33: Utarbeidelse av individuell plan. Gyldige koder., messageText=Det er ikke krysset av for om mottakeren har fått utarbeidet individuell plan. Utfylt verdi er '( =Ukjent)'. Feltet er obligatorisk å fylle ut., lineNumbers=[]),

ValidationReportEntry(severity=ERROR, caseworker=Sara Saksb, journalId=~journal, individId=06059645658, contextId=,
ruleName=Kontroll 35: Boligsituasjon. Gyldige koder., messageText=Det er ikke krysset av for mottakerens boligsituasjon. Utfylt verdi er '( =Ukjent)'. Feltet er obligatorisk å fylle ut., lineNumbers=[1]),

ValidationReportEntry(severity=WARNING, caseworker=Sara Saksb, journalId=~journal, individId=06059645658, contextId=,
ruleName=Kontroll 36: Bidrag fordelt på måneder, messageText=Det er ikke fylt ut bidrag (0) fordelt på måneder eller sum stemmer ikke med sum bidrag (     42) utbetalt i løpet av året., lineNumbers=[1]),

ValidationReportEntry(severity=ERROR, caseworker=Sara Saksb, journalId=~journal, individId=06059645658, contextId=,
ruleName=Kontroll 39: Første vilkår i året, vilkår, messageText=Det er ikke krysset av for om det stilles vilkår til mottakeren etter sosialtjenesteloven. Registreres for første vilkår i kalenderåret. Feltet er obligatorisk., lineNumbers=[1]),

ValidationReportEntry(severity=ERROR, caseworker=Sara Saksb, journalId=~journal, individId=06059645658, contextId=,
ruleName=Kontroll 40: Første vilkår i året, vilkår til søkerens samboer/ektefelle, messageText=Det er ikke krysset av for om det stilles vilkår til mottakeren etter sosialtjenesteloven. Registreres for første vilkår i kalenderåret. Feltet er obligatorisk., lineNumbers=[1])], numberOfControls=43, statsEntries=[]) failed:




 */