package no.ssb.kostra.area.famvern.famvern52a

import no.ssb.kostra.area.famvern.FamilievernConstants.kontorFylkeRegionMappingList
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.famvern.famvern52a.*

class Familievern52aMain(arguments: KotlinArguments) : PositionedFileValidator(arguments) {
    override val fieldDefinitions = Familievern52aFieldDefinitions

    override val fatalRules: List<AbstractRule<List<String>>> = listOf(
        Rule001RecordLength(fieldDefinitions.fieldDefinitions.last().to)
    )

    override val validationRules = listOf(
        Rule003Regionsnummer(kontorFylkeRegionMappingList),
        Rule004Kontornummer(kontorFylkeRegionMappingList),
        Rule005RegionsnummerKontornummer(kontorFylkeRegionMappingList),
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
    )
}