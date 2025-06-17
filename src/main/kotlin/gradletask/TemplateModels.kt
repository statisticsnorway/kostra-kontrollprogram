package gradletask

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.micronaut.serde.annotation.Serdeable
import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.DataType
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FileDescription
import no.ssb.kostra.program.extension.buildFieldDefinitions

@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
data class FileDescriptionTemplate(
    val id: String = "",
    val title: String = "File description",
    val reportingYear: Int = 0,
    val description: String = "Default file description",
    val fields: List<FieldDefinitionTemplate> = emptyList(),
)

@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
data class FieldDefinitionTemplate(
    val name: String,
    val description: String? = null,
    val size: Int = 1,
    val dataType: DataType = DataType.STRING_TYPE,
    val datePattern: String?,
    val mandatory: Boolean?,
    val codeList: List<Code>?,
    val codeListSource: String? = null,
) {
    init {
        require(!(dataType == DataType.DATE_TYPE && datePattern?.isBlank() == true)) {
            "datePattern cannot be empty or blank when dataType is ${DataType.DATE_TYPE}"
        }
    }
}

fun FileDescriptionTemplate.toFileDescription(): FileDescription {
    val fieldDefinitions: List<FieldDefinition> =
        this.fields
            .map {
                val codeList: List<Code> =
                    when {
                        it.codeList?.isNotEmpty() == true ->
                            it.codeList

                        it.codeListSource?.isNotEmpty() == true ->
                            KlassApiClient().fetchCodes(
                                it.codeListSource,
                                reportingYear.toString()
                            )

                        else ->
                            emptyList()
                    }

                FieldDefinition(
                    name = it.name,
                    description = it.description,
                    from = 0,
                    size = it.size,
                    dataType = it.dataType,
                    datePattern = it.datePattern ?: "",
                    mandatory = it.mandatory ?: false,
                    codeList = codeList,
                )
            }.buildFieldDefinitions()

    return FileDescription(
        title = this.title,
        reportingYear = this.reportingYear,
        description = this.description,
        fields = fieldDefinitions,
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
data class FamvernMappingTemplate(
    val title: String = "File description",
    val reportingYear: Int = 0,
    val description: String = "Default file description",
    val regions: String = "0",
    val counties: String = "0",
    val offices: String = "0",
)

fun FamvernMappingTemplate.toFamvernHierarchyMapping() : FamilievernConstants.FamvernHierarchyMapping {
    val countiesToRegions =
        KlassApiClient().fetchCorrespondence(
        counties,
        regions,
        reportingYear.toString()
    ).toMap()

    val mappings = KlassApiClient().fetchCorrespondence(
        offices,
        counties,
        reportingYear.toString()
    )
        .toMap()
        .map { (kontor, fylke) ->
            val region = countiesToRegions.getOrDefault(
                fylke,
                Code("UNKNOWN", "Ukjent")
            )
            FamilievernConstants.FamvernHierarchyKontorFylkeRegionMapping(
                region = region.code,
                regionName = region.value,
                fylke = fylke.code,
                fylkeName = fylke.value,
                kontor = kontor.code,
                kontorName = kontor.value
            )
        }
        .sortedWith(compareBy({ it.region }, { it.fylke }, { it.kontor }))

    return FamilievernConstants.FamvernHierarchyMapping(
        title = title,
        year = reportingYear,
        description = description,
        mappings = mappings
    )
}
