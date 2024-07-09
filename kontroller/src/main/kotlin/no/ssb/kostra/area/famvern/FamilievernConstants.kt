package no.ssb.kostra.area.famvern

import no.ssb.kostra.program.FileLoader

object FamilievernConstants {
    private const val FILENAME = "mapping_familievern_region_fylke_kontor.yaml"

    data class MappingDescription(
        val title: String,
        val description: String,
        val year: Int,
        val regions: List<MappingRegion>
    ) {
        fun toKontorFylkeRegionMapping(): List<KontorFylkeRegionMapping> =
            this.regions.flatMap { region ->
                region.counties.flatMap { county ->
                    county.offices.map { office ->
                        KontorFylkeRegionMapping(office.code, county.code, region.code)
                    }
                }
            }
    }

    data class MappingRegion(val code: String, val name: String, val counties: List<MappingCounty>)

    data class MappingCounty(val code: String, val name: String, val offices: List<MappingOffice>)

    data class MappingOffice(val code: String, val name: String)

    data class KontorFylkeRegionMapping(val kontor: String, val fylke: String, val region: String)

    val kontorFylkeRegionMappingList: List<KontorFylkeRegionMapping> =
        FileLoader.getResource<MappingDescription>(FILENAME).toKontorFylkeRegionMapping()
}