package no.ssb.kostra.area.regnskap

import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.regnskap.*

abstract class RegnskapValidator(
    final override val arguments: KotlinArguments,
) : PositionedFileValidator(arguments = arguments) {
    override val fieldDefinitions: FieldDefinitions = RegnskapFieldDefinitions

    override val preValidationRules: List<AbstractRule<List<String>>> = listOf(
        Rule001RecordLength(RegnskapFieldDefinitions.fieldLength)
    )

    abstract val funksjonList: List<String>
    abstract val kapittelList: List<String>
    abstract val artList: List<String>
    abstract val sektorList: List<String>
    protected fun commonValidationRules(): List<AbstractRule<List<KostraRecord>>> = listOf(
        Rule003Skjema(),
        Rule004Aargang(),
        Rule005Kvartal(),
        Rule006Region(),
        Rule007Organisasjonsnummer(),
        Rule008Foretaksnummer(),
        Rule009Kontoklasse(kontoklasseList = RegnskapConstants.getKontoklasseBySkjema(arguments.skjema)),
        Rule010Funksjon(funksjonList = funksjonList),
        Rule011Kapittel(kapittelList = kapittelList),
        Rule012Art(artList = artList),
        Rule013Sektor(sektorList = sektorList),
        Rule014Belop(),
        Rule015Duplicates(RegnskapConstants.mappingDuplicates(arguments = arguments))
    )


}