package no.ssb.kostra.program.extension

import no.ssb.kostra.program.KostraRecord
import java.time.LocalDate
import kotlin.reflect.typeOf

inline fun <reified T : Any?> KostraRecord.fieldAs(field: String): T = typeOf<T>().run {
    when (classifier) {
        Int::class -> if (isMarkedNullable) fieldAsInt(field) else fieldAsIntOrDefault(field)
        LocalDate::class -> if (isMarkedNullable) fieldAsLocalDate(field) else fieldAsLocalDate(field)!!
        String::class -> (
                if (isMarkedNullable) get(field).valueOrNull()
                else get(field)
                )?.trim()

        else -> throw IllegalArgumentException("fieldAs(): Unsupported type $classifier")
    }
} as T
