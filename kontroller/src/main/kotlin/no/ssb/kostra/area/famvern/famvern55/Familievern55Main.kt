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
        Rule001RecordLength(fieldDefinitions.fieldDefinitions.last().to),
        Rule002FileDescription(fieldDefinitions.fieldDefinitions)
    )

    override val validationRules = listOf(
        Rule003Fylkesnummer(FamilievernConstants.kontorFylkeRegionMappingList),
        Rule005AvsluttedeMeklingerEtterTidsbruk(),
        Rule006AvsluttedeMeklingerEtterDeltakere(),
        Rule007AvsluttedeMeklingerEtterTidsbruk(),
        Rule008AvsluttedeMeklingerIkkeOverholdtTidsfrist(),
        Rule009AvsluttedeMeklingerEtterVarighet(),
        Rule010AvsluttedeMeklingerHvorBarnHarDeltatt(),
        Rule011ResultatAvAvsluttedeMeklinger(),
        Rule012AntallAvsluttedeMeklingerSkriftligAvtaleEtterResultat(),
        Rule013AvsluttedeMeklingerOgBekymringsmeldinger(),
        Rule014KontrollAvTotalsummerForMeklinger(),
        Rule015KontrollAvTotalsummerForSkriftligeAvtaler(),
        Rule016AvsluttedeMeklingerUtenOppmote()
    )
}