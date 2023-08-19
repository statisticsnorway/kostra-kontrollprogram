package no.ssb.kostra.area.famvern.famvern53

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.famvern.famvern53.*

class Familievern53Main(arguments: KotlinArguments) : PositionedFileValidator(arguments) {
    override val fieldDefinitions = Familievern53FieldDefinitions

    override val fatalRules: List<AbstractRule<List<String>>> = listOf(
        Rule001RecordLength(fieldDefinitions.fieldDefinitions.last().to)
    )

    override val validationRules = listOf(
        Rule003Fylkesnummer(FamilievernConstants.kontorFylkeRegionMappingList),
        Rule004Kontornummer(FamilievernConstants.kontorFylkeRegionMappingList),
        Rule005FylkesnummerKontornummer(FamilievernConstants.kontorFylkeRegionMappingList),
        Rule010Tiltak(Familievern53Constants.rule010Mappings),
        Rule010Timer(Familievern53Constants.rule010Mappings),
    )
}