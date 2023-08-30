package no.ssb.kostra.validation.rule.sosial.extensions

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.program.util.SsnValidationUtils

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

fun KostraRecord.ageInYears(arguments: KotlinArguments) : Int =
    this[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt()) ?: -1

fun KostraRecord.hasFnr() : Boolean =
    SsnValidationUtils.isValidSocialSecurityIdOrDnr(this[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME])

