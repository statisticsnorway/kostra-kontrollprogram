package no.ssb.kostra.program.extension

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import no.ssb.kostra.program.FileDescription
import no.ssb.kostra.program.util.SsnValidationUtils
import java.io.File

fun String.valueOrNull() = if (this.trim() in setOf("", "0")) null else this

fun String.municipalityIdFromRegion() = this.substring(0, 4)

// CR NOTE: Only in use in tests
fun String.districtIdFromRegion() = this.substring(4, 6)

fun String.ageInYears(reportingYear: Int): Int? =
    SsnValidationUtils.extractBirthDateFromSocialSecurityId(socialSecurityId = this)?.let { dateOfBirth ->
        reportingYear - dateOfBirth.year
    }
