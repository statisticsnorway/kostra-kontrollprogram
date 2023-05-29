package no.ssb.kostra.area.barnevern.extension

import no.ssb.kostra.area.barnevern.ValidationUtils.extractBirthDateFromSocialSecurityId
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun String.ageInYears(reportingYear: Int): Long? =
    extractBirthDateFromSocialSecurityId(socialSecurityId = this)?.let { dateOfBirth ->
        ChronoUnit.YEARS.between(
            dateOfBirth,
            LocalDate.of(reportingYear, 12, 31)
        )
    }