package no.ssb.kostra.validation.rule

import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

object RandomUtils {

    fun generateRandomSsn(age: Int):String = generateRandomSSN(
        Year.now().minusYears(age.toLong()).atDay(1),
        Year.now().minusYears(age - 1L).atDay(1).minusDays(1)
    )

    fun generateRandomSSN(
        startInclusive: LocalDate,
        endExclusive: LocalDate
    ): String = (startInclusive.toEpochDay() until endExclusive.toEpochDay()).random()
        .let { randomDay ->
            LocalDate.ofEpochDay(randomDay).format(DATE_TIME_FORMATTER)
        }.let { birthDate123456 ->
            generateSequence {
                birthDate123456.plus((100 until 499).random().toString())
            }.map { nineDigitSeed ->
                buildSsnRecursive(
                    nineDigitSeed,
                    listOf(
                        controlSumDigits1.subList(
                            0, controlSumDigits1.size - 1
                        ),
                        controlSumDigits2.subList(
                            0, controlSumDigits2.size - 1
                        )
                    )
                )
            }.filter { ssnCandidate -> ssnCandidate.length == SSN_LENGTH }.first()
        }

    fun generateRandomDuf(
        startYearInclusive: Int,
        endYearInclusive: Int
    ): String {
        val year = (startYearInclusive..endYearInclusive).random()
        val sixDigits = (100000..999999).random()

        return "$year$sixDigits".let { tenDigits ->
            tenDigits.plus(tenDigits
                .map { it.toString().toInt() }
                .zip(dufControlDigits) { digit, weight -> digit * weight }
                .sum()
                .mod(11)
                .toString()
                .padStart(2, '0'))
        }
    }

    private val dufControlDigits = listOf(4, 6, 3, 2, 4, 6, 3, 2, 7, 5)

    private val controlSumDigits1 = listOf(3, 7, 6, 1, 8, 9, 4, 5, 2, 1)
    private val controlSumDigits2 = listOf(5, 4, 3, 2, 7, 6, 5, 4, 3, 2, 1)

    private val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy")

    private fun modulo11(toCheck: String, controlDigits: List<Int>): Int =
        if (toCheck.length == controlDigits.size && Pattern.compile("^\\d+$").matcher(toCheck).matches()) {
            toCheck.asSequence()
                .map { it.toString().toInt() }
                .zip(controlDigits.asSequence()) { digit, controlDigit -> digit * controlDigit }
                .sum()
                .mod(11)
        } else {
            -1
        }

    private const val SSN_LENGTH = 11
    private const val SSN_INITIAL_SEED_LENGTH = 9

    private fun buildSsnRecursive(
        seed: String,
        controlDigits: List<List<Int>>
    ): String = if (seed.length == SSN_LENGTH) {
        seed
    } else {
        modulo11(
            seed,
            controlDigits[seed.length - SSN_INITIAL_SEED_LENGTH]
        ).let { modulo11 ->
            if (modulo11 == 1 || modulo11 == 10) {
                seed
            } else {
                /** NOTE: Recursive call */
                buildSsnRecursive(
                    seed = seed.plus(getModAsString(modulo11)),
                    controlDigits = controlDigits
                )
            }
        }
    }

    private fun getModAsString(value: Int): String =
        (if (value == 0) 0 else SSN_LENGTH - value).toString()
}
