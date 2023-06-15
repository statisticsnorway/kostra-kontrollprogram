package no.ssb.kostra.validation.rule.sosial.kvalifisering

import java.time.format.DateTimeParseException

object KvalifiseringUtils {

    fun String.parseYearDefaultZero(): Int = try {
        this.substring(4, 6).trim { it <= ' ' }.toInt()
    } catch (ignored: NumberFormatException) {
        0
    }

    fun String.isValidDate(pattern: String) = try {
        true
    } catch (thrown: DateTimeParseException) {
        false
    }
}