package no.ssb.kostra.area.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.regnskap.*
import no.ssb.kostra.validation.rule.regnskap.kostra.*

abstract class RegnskapValidator(
    final override val arguments: KotlinArguments,
) : PositionedFileValidator(arguments = arguments) {
    override val fieldDefinitions: FieldDefinitions = RegnskapFieldDefinitions

    override val preValidationRules: List<AbstractRule<List<String>>> = listOf(
        Rule001RecordLength(RegnskapFieldDefinitions.fieldLength)
    )

    protected val fieldFunksjonKapittelLength = 4
    protected val fieldArtSektorLength = 3

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

    protected fun commonKostraValidationRules(
        invalidDriftFunksjonList: List<String>,
        invalidDriftArtList: List<String>,
        invalidInvesteringFunksjonList: List<String>,
        illogicalInvesteringFunksjonList: List<String>,
        invalidInvesteringArtList: List<String>
    ): List<AbstractRule<List<KostraRecord>>> = listOf(
        Rule016KapittelFortegn(),
        Rule020KombinasjonDriftKontoklasseFunksjon(invalidDriftFunksjonList = invalidDriftFunksjonList),
        Rule025KombinasjonDriftKontoklasseArt(invalidDriftArtList = invalidDriftArtList),
        Rule030KombinasjonDriftKontoklasseArt(illogicalDriftArtList = listOf("285", "660")),
        Rule035KombinasjonDriftKontoklasseArt(illogicalDriftArtList = listOf("520", "920")),
        Rule040KombinasjonInvesteringKontoklasseFunksjon(invalidInvesteringFunksjonList = invalidInvesteringFunksjonList),
        Rule045KombinasjonInvesteringKontoklasseFunksjon(illogicalInvesteringFunksjonArtList = illogicalInvesteringFunksjonList),
        Rule050KombinasjonInvesteringKontoklasseArt(invalidInvesteringArtList = invalidInvesteringArtList),
        Rule055KombinasjonInvesteringKontoklasseArt(illogicalInvesteringArtList = listOf("620", "650", "900")),
        Rule060KombinasjonInvesteringKontoklasseFunksjonArt(),
        Rule065KombinasjonBevilgningFunksjonArt(),
        Rule070KombinasjonBevilgningFunksjonArt(),
        Rule075KombinasjonBevilgningFunksjonArt(),
        Rule080KombinasjonBevilgningFunksjonArt(),
        Rule100SummeringDriftUtgiftsposteringer(),
        Rule105SummeringDriftInntektsposteringer(),
        Rule130SkatteInntekter(),
        Rule135Rammetilskudd(),
    )

    protected fun specificKostraValidationRules(): List<AbstractRule<List<KostraRecord>>> = listOf(
        Rule085SummeringInvesteringUtgiftsposteringer(),
        Rule090SummeringInvesteringInntektsposteringer(),
        Rule095SummeringInvesteringDifferanse(),
        Rule110SummeringDriftDifferanse(),
        Rule115SummeringBalanseAktiva(),
        Rule120SummeringBalansePassiva(),
        Rule125SummeringBalanseDifferanse(),
        Rule126SummeringDriftOsloInternDifferanse(),
        Rule127SummeringInvesteringOsloInternDifferanse(),
        Rule140OverforingerDriftInvestering(),
        Rule145AvskrivningerMotpostAvskrivninger(),
        Rule150Avskrivninger(),
        Rule155AvskrivningerDifferanse(),
        Rule160AvskrivningerAndreFunksjoner(),
        Rule165AvskrivningerMotpostAvskrivningerAndreFunksjoner(),
        Rule170Funksjon290Investering(),
        Rule175Funksjon290Drift(),
        Rule180Funksjon465Investering(),
        Rule185Funksjon465Drift(),
        Rule190Memoriakonti()
    )

    override fun getRecordsList(arguments: KotlinArguments): List<KostraRecord> = arguments
        .getInputContentAsStringList()
        .withIndex()
        .map { (index, recordString) ->
            recordString.toKostraRecord(
                index = index + 1,
                fieldDefinitions = fieldDefinitions.fieldDefinitions
            )
        }
        .filterNot { kostraRecord -> kostraRecord.fieldAsTrimmedString(FIELD_BELOP) == "0" }
}