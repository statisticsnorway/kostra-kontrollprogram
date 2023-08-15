package no.ssb.kostra.area.famvern.famvern52b

import no.ssb.kostra.area.famvern.FamilievernConstants.kontorFylkeRegionMappingList
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.Validator
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.famvern.famvern52b.Rule003Regionsnummer
import no.ssb.kostra.validation.rule.famvern.famvern52b.Rule004Kontornummer
import no.ssb.kostra.validation.rule.famvern.famvern52b.Rule005RegionsnummerKontornummer

class Familievern52bMain(arguments: KotlinArguments) : Validator(arguments) {
    override val fieldDefinitions = Familievern52bFieldDefinitions

    override val fatalRules: List<AbstractRule<List<String>>> = listOf(
        Rule001RecordLength(fieldDefinitions.fieldDefinitions.last().to)
    )

    override val validationRules = listOf(
        Rule003Regionsnummer(kontorFylkeRegionMappingList),
        Rule004Kontornummer(kontorFylkeRegionMappingList),
        Rule005RegionsnummerKontornummer(kontorFylkeRegionMappingList),
    )
}