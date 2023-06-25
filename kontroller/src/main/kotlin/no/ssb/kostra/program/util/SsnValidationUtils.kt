package no.ssb.kostra.program.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.regex.Pattern

object SsnValidationUtils {

    private const val CENTURY = 100L
    private const val PERSON_ID_LENGTH = 5
    private val SSN_SUFFIX_EXCEPTIONS = setOf("00100", "00200", "99999")

    private val SSN_PATTERN = Pattern.compile("^\\d{11}$")
    private val DUF_PATTERN: Pattern = Pattern.compile("^\\d{12}$")

    private val LOCAL_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy")

    private val CONTROL_SUM_DIGITS_1 = listOf(3, 7, 6, 1, 8, 9, 4, 5, 2, 1)
    private val CONTROL_SUM_DIGITS_2 = listOf(5, 4, 3, 2, 7, 6, 5, 4, 3, 2, 1)

    fun extractBirthDateFromSocialSecurityId(socialSecurityId: String): LocalDate? = try {
        when {
            !SSN_PATTERN.matcher(socialSecurityId).matches() -> null
            else -> parseDateWithAutoPivotYear(convertDNumber(socialSecurityId))
        }
    } catch (ex: DateTimeParseException) {
        null
    }

    private fun parseDateWithAutoPivotYear(socialSecurityId: String): LocalDate =
        LocalDate.parse(socialSecurityId, LOCAL_DATE_FORMATTER).let {
            /** unborn will have date of birth in the future, do not subtract a century for those */
            if (it.isAfter(LocalDate.now().plusYears(1))) it.minusYears(CENTURY)
            else it
        }


    fun isValidSocialSecurityId(personId: String) = SSN_PATTERN.matcher(personId).matches() && isModulo11Valid(personId)

    fun isValidSocialSecurityIdOrDnr(personId: String) = SSN_PATTERN.matcher(personId).matches()
            &&
            (
                    isModulo11Valid(personId)
                            ||
                            extractBirthDateFromSocialSecurityId(personId) != null
                            && SSN_SUFFIX_EXCEPTIONS.contains(personId.takeLast(PERSON_ID_LENGTH))
                    )

    fun validateDUF(duf: String): Boolean = DUF_PATTERN.matcher(duf).matches()
            &&
            duf.asSequence()
                .take(10)
                .map { it.toString().toInt() }
                .zip(sequenceOf(4, 6, 3, 2, 4, 6, 3, 2, 7, 5)) { digit, weight -> digit * weight }
                .sum()
                .mod(11)
                .toString()
                .padStart(2, '0') == duf.substring(10)


    private fun convertDNumber(socialSecurityId: String): String =
        socialSecurityId.first().toString().toInt().let {
            (if (it > 3) it - 4 else it).toString().plus(socialSecurityId.substring(1, 6))
        }

    internal fun isModulo11Valid(socialSecurityId: String): Boolean =
        modulo11(
            toCheck = socialSecurityId.substring(0, socialSecurityId.length - 1),
            controlDigits = CONTROL_SUM_DIGITS_1
        ) == 0 && modulo11(
            toCheck = socialSecurityId,
            controlDigits = CONTROL_SUM_DIGITS_2
        ) == 0

    private fun modulo11(toCheck: String, controlDigits: List<Int>): Int =
        if (toCheck.length == controlDigits.size && Pattern.compile("^\\d+$").matcher(toCheck).matches()) {
            toCheck
                .map { it.toString().toInt() }
                .zip(controlDigits) { digit, controlDigit -> digit * controlDigit }
                .sum()
                .mod(11)
        } else {
            -1
        }
}
