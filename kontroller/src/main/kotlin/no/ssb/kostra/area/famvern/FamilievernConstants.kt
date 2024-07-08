package no.ssb.kostra.area.famvern

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.NoSuchFileException

object FamilievernConstants {
    private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
    private const val FILENAME = "mapping_familievern_region_fylke_kontor.yaml"

    data class MappingDescription(
        val title: String,
        val description: String,
        val year: Int,
        val regions: List<MappingRegion>
    )

    data class MappingRegion(val code: String, val name: String, val counties: List<MappingCounty>)

    data class MappingCounty(val code: String, val name: String, val offices: List<MappingOffice>)

    data class MappingOffice(val code: String, val name: String)

    data class KontorFylkeRegionMapping(val kontor: String, val fylke: String, val region: String)

    fun getResourceAsMappingDescription(fileName: String): List<KontorFylkeRegionMapping> =
        this::class.java.classLoader.getResourceAsStream(fileName)
            ?.let { inputStream -> InputStreamReader(inputStream) }
            ?.let { inputStreamReader -> BufferedReader(inputStreamReader) }
            ?.let { bufferedReader -> mapper.readValue(bufferedReader.readText()) as MappingDescription? }
            ?.let { mappingDescription ->
                mappingDescription.regions.flatMap { region ->
                    region.counties.flatMap { county ->
                        county.offices.map { office ->
                            KontorFylkeRegionMapping(office.code, county.code, region.code)
                        }
                    }
                }
            }
            ?: throw NoSuchFileException("Famvern mapping file not found. File name = $fileName")


    val kontorFylkeRegionMappingList =
        getResourceAsMappingDescription(FILENAME)
}