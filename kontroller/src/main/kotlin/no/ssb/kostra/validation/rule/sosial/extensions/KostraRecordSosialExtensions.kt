package no.ssb.kostra.validation.rule.sosial.extensions

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.program.util.SsnValidationUtils
import no.ssb.kostra.validation.report.StatsEntry

fun KostraRecord.hasVarighet() =
    (1..12)
        .map {
            "STMND_$it"
        }.any {
            fieldDefinitions.byColumnName(it).codeExists(this[it])
        }

fun KostraRecord.hasNotVarighet() =
    (1..12)
        .map {
            "STMND_$it"
        }.none {
            fieldDefinitions.byColumnName(it).codeExists(this[it])
        }

fun KostraRecord.ageInYears(arguments: KotlinArguments): Int =
    this[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt()) ?: -1

fun KostraRecord.hasFnr(): Boolean =
    SsnValidationUtils.isValidSocialSecurityIdOrDnr(this[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME])

fun Collection<KostraRecord>.varighetAsStatsEntries() = this
    .map { kostraRecord ->
        (1..12)
            .map {
                "STMND_$it"
            }.count { fieldName ->
                kostraRecord.fieldDefinition(fieldName).codeExists(kostraRecord[fieldName])
            }
    }
    .groupBy {
        when (it) {
            1 -> "1 måned"
            in 2..3 -> "2 - 3 måneder"
            in 4..6 -> "4 - 6 måneder"
            in 7..9 -> "7 - 9 måneder"
            in 10..11 -> "10 - 11 måneder"
            12 -> "12 måneder"
            else -> "Uoppgitt"
        }
    }
    .map {
        StatsEntry(it.key, it.value.size.toString())
    }