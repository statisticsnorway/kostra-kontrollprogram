package klass

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable
import no.ssb.kostra.program.Code

@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
data class KlassCodes(
    @JsonProperty("codes")
    val codes: List<KlassCode>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
data class KlassCode(
    val code: String = "",
    val name: String = "",
)

fun KlassCodes.toCodeList(): List<Code> =
    codes.map{ Code(code = it.code, value = it.name) }

@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
data class KlassCorrespondence(
    @JsonProperty("correspondenceItems")
    val correspondenceItems: List<KlassCorrespondenceItem>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
data class KlassCorrespondenceItem(
    val sourceCode: String,
    val sourceName: String,
    val targetCode: String,
    val targetName: String,
)

fun KlassCorrespondence.toCodePairList(): List<Pair<Code,Code>> =
    correspondenceItems.map{
        Code(code = it.sourceCode, value = it.sourceName) to Code(code = it.targetCode, value = it.targetName)
    }