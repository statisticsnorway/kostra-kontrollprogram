package no.ssb.kostra.area.famvern.famvern52b

import no.ssb.kostra.area.famvern.FamilievernConstants.kontorFylkeRegionMappingList
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.Validator
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.famvern.famvern52b.*

class Familievern52bMain(arguments: KotlinArguments) : Validator(arguments) {
    override val fieldDefinitions = Familievern52bFieldDefinitions

    override val fatalRules: List<AbstractRule<List<String>>> = listOf(
        Rule001RecordLength(fieldDefinitions.fieldDefinitions.last().to)
    )

    override val validationRules = listOf(
        Rule003Regionsnummer(kontorFylkeRegionMappingList),
        Rule004Kontornummer(kontorFylkeRegionMappingList),
        Rule005RegionsnummerKontornummer(kontorFylkeRegionMappingList),
        Rule006Dubletter(),
        Rule007Gruppenavn(),
        Rule008StartDato(),
        Rule009Malgruppe(),
        Rule010GruppensHovedtema(),
        Rule011AntallGruppemoterGjennomfortILA(),
        Rule012AntallGruppemoterGjennomfortSO(),
        Rule013AntallTimerAnvendtIGruppenTotaltILA(),
        Rule014AntallTimerAnvendtIGruppenTotaltSO(),
        Rule015AntallDeltagereIGruppenILA(),
        Rule016AntallDeltagereIGruppenSO(),
        Rule017AntallTerapeuterInvolvertIGruppebehandlingen(),
        Rule018Tolk(),
        Rule019StatusVedAretsSlutt(),
        Rule020GruppebehandlingenAvsluttetManglerAvslutningsdato(),
        Rule021AvslutningsdatoForForsteSamtale(),
    )
}