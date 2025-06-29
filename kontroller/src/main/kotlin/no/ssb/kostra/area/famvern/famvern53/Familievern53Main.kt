package no.ssb.kostra.area.famvern.famvern53

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.Rule002FileDescription
import no.ssb.kostra.validation.rule.famvern.famvern53.*

class Familievern53Main(arguments: KotlinArguments) : PositionedFileValidator(arguments) {
    override val fieldDefinitions = Familievern53FieldDefinitions

    override val preValidationRules: List<AbstractRule<List<String>>> = listOf(
        Rule001RecordLength(fieldDefinitions.fieldDefinitions.last().to)
    )

    override val validationRules = listOf(
        Rule002FileDescription(fieldDefinitions.fieldDefinitions),
        Rule003Fylkesnummer(FamilievernConstants.famvernHierarchyKontorFylkeRegionMappingList),
        Rule004Kontornummer(FamilievernConstants.famvernHierarchyKontorFylkeRegionMappingList),
        Rule005FylkesnummerKontornummer(FamilievernConstants.famvernHierarchyKontorFylkeRegionMappingList),
        Rule010Tiltak(Familievern53Constants.rule010Mappings),
        Rule010Timer(Familievern53Constants.rule010Mappings),
    )
}