package no.ssb.kostra.area.famvern.famvern55

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.Rule002FileDescription
import no.ssb.kostra.validation.rule.famvern.famvern55.*


class Familievern55Main(arguments: KotlinArguments) : PositionedFileValidator(arguments) {
    override val fieldDefinitions = Familievern55FieldDefinitions

    override val preValidationRules: List<AbstractRule<List<String>>> = listOf(
        Rule001RecordLength(fieldDefinitions.fieldDefinitions.last().to)
    )

    override val validationRules = listOf(
        Rule002FileDescription(fieldDefinitions.fieldDefinitions),
        Rule003Fylkesnummer(FamilievernConstants.famvernHierarchyKontorFylkeRegionMappingList),
        Rule005AvsluttedeMeklingerEtterTidsbruk(),
        Rule006AvsluttedeMeklingerEtterDeltakere(),
        Rule007AvsluttedeMeklingerEtterTidsbruk(),
        Rule008AvsluttedeMeklingerIkkeOverholdtTidsfrist(),
        Rule009AvsluttedeMeklingerEtterVarighet(),
        Rule011ResultatAvAvsluttedeMeklinger(),
        Rule014KontrollAvTotalsummerForMeklinger(),
        Rule017BarnDeltatt(),
        Rule018BekymringsmeldingSendt(),
        Rule019VoldSomTema(),
        Rule020KontrollAvTotalsummerForMeklinger(),
    )
}