package no.ssb.kostra.area.famvern.famvern52a

import no.ssb.kostra.area.famvern.FamilievernConstants.famvernHierarchyKontorFylkeRegionMappingList
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.Rule002FileDescription
import no.ssb.kostra.validation.rule.famvern.famvern52a.*

class Familievern52aMain(arguments: KotlinArguments) : PositionedFileValidator(arguments) {
    override val fieldDefinitions = Familievern52aFieldDefinitions

    override val preValidationRules: List<AbstractRule<List<String>>> = listOf(
        Rule001RecordLength(fieldDefinitions.fieldDefinitions.last().to)
    )

    override val validationRules = listOf(
        Rule002FileDescription(fieldDefinitions.fieldDefinitions),
        Rule003Regionsnummer(famvernHierarchyKontorFylkeRegionMappingList),
        Rule004Kontornummer(famvernHierarchyKontorFylkeRegionMappingList),
        Rule005RegionsnummerKontornummer(famvernHierarchyKontorFylkeRegionMappingList),
        Rule006Dubletter(),
        Rule007Henvendelsesdato(),
        Rule009KontaktTidligere(),
        Rule011HenvendelsesBegrunnelse(),
        Rule013Kjonn(),
        Rule014Fodselsaar(),
        Rule015Samlivsstatus(),
        Rule016FormellSivilstand(),
        Rule017Bosituasjon(),
        Rule018Arbeidssituasjon(),
        Rule019AVarighetSamtalepartner(),
        Rule019B1VarighetSidenBrudd(),
        Rule019B2VarighetEkspartner(),
        Rule020DatoForsteBehandlingssamtale(),
        Rule021DatoForsteBehandlingssamtaleEtterHenvendelse(),
        Rule022OmraaderArbeidet(),
        Rule023HovedformPaaBehandlingstilbudet(),
        Rule024DeltagelseBehandlingssamtaler(),
        Rule025Behandlingssamtaler(),
        Rule026RelasjonBehandlingssamtalerDeltatt(),
        Rule027AntallBehandlingssamtalerForAnsatteVedKontoret(),
        Rule028AntallBehandlingssamtaler(),
        Rule029AntallBehandlingssamtalerSidenOpprettelsen(),
        Rule030TotaltAntallTimer(),
        Rule031TotaltAntallTimerSidenSakenBleOpprettet(),
        Rule032SamarbeidMedAndreInstanserSidenOpprettelsen(),
        Rule033StatusVedAretsSlutt(),
        Rule034SakensHovedtema(),
        Rule035SakenAvsluttetManglerAvslutningsdato(),
        Rule036AvslutningsdatoForForsteSamtale(),
        Rule037BekymringsmeldingSendtBarnevernet(),
        Rule038Ventetid(),
    )
}