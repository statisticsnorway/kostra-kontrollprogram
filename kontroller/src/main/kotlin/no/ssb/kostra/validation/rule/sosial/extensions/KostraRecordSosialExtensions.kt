package no.ssb.kostra.validation.rule.sosial.extensions

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists

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
