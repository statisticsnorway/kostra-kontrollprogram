package no.ssb.kostra.program.extension

import no.ssb.kostra.program.util.SsnValidationUtils

fun String.valueOrNull() = if (this in setOf(" ", "0")) null else this

fun String.municipalityIdFromRegion() = this.substring(0, 4)

fun String.districtIdFromRegion() = this.substring(4, 6)

fun String.ageInYears(reportingYear: Int): Int? =
    SsnValidationUtils.extractBirthDateFromSocialSecurityId(socialSecurityId = this)?.let { dateOfBirth ->
        reportingYear - dateOfBirth.year
    }