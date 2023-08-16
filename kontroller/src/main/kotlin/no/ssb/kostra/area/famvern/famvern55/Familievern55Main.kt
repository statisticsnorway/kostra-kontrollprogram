package no.ssb.kostra.area.famvern.famvern55

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.Validator
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.famvern.famvern55.*


class Familievern55Main(arguments: KotlinArguments) : Validator(arguments) {
    override val fieldDefinitions = Familievern55FieldDefinitions

    override val fatalRules: List<AbstractRule<List<String>>> = listOf(
        Rule001RecordLength(fieldDefinitions.fieldDefinitions.last().to)
    )

    override val validationRules = listOf(
        Rule003Fylkesnummer(FamilievernConstants.kontorFylkeRegionMappingList),
        Rule005AvsluttedeMeklingerEtterTidsbruk(),
        Rule006AvsluttedeMeklingerEtterDeltakere(),
        Rule007AvsluttedeMeklingerEtterTidsbruk(),
        Rule008AvsluttedeMeklingerIkkeOverholdtTidsfrist(),
        Rule009AvsluttedeMeklingerEtterVarighet(),
    )
}