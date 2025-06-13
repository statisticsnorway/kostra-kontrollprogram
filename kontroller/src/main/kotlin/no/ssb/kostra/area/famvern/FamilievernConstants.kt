package no.ssb.kostra.area.famvern

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.micronaut.serde.annotation.Serdeable
import no.ssb.kostra.program.FileLoader

object FamilievernConstants {
    private const val FILENAME = "mapping_familievern_region_fylke_kontor_2025.yaml"

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Serdeable
    data class FamvernHierarchyMapping(
        val title: String,
        val description: String,
        val year: Int,
        val mappings: List<FamvernHierarchyKontorFylkeRegionMapping>
    ) {
        fun toKontorFylkeRegionMapping(): List<FamvernHierarchyKontorFylkeRegionMapping> = mappings

        fun toMarkdown(): String = buildString {
            appendLine("# $title (${year})")
            appendLine()
            appendLine(description)
            appendLine()
            appendLine("## Koblinger")
            appendLine()
            appendLine("| Regionnummer | Regionnavn | Fylkenummer | Fylkenavn | Kontornummer | Kontornavn |")
            appendLine("|--------------|------------|-------------|-----------|--------------|------------|")
            for (mapping in mappings) {
                appendLine("| `${mapping.region}` | ${mapping.regionName} | `${mapping.fylke}` | ${mapping.fylkeName} | `${mapping.kontor}` | ${mapping.kontorName} |")
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Serdeable
    data class FamvernHierarchyKontorFylkeRegionMapping(
        val region: String,
        val regionName: String = "",
        val fylke: String,
        val fylkeName: String = "",
        val kontor: String,
        val kontorName: String = ""
    )

    val famvernHierarchyKontorFylkeRegionMappingList: List<FamvernHierarchyKontorFylkeRegionMapping> =
        FileLoader.getResource<FamvernHierarchyMapping>(FILENAME).toKontorFylkeRegionMapping()

}