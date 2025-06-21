package no.ssb.kostra.web.controller

import io.kotest.assertions.json.schema.*
import io.kotest.common.ExperimentalKotest
import io.kotest.matchers.collections.beIn
import io.kotest.matchers.longs.beGreaterThan
import io.kotest.matchers.longs.beInRange

/**
 * IMPORTANT: The schemas below are designed to safeguard the React frontend against unexpected changes
 * in the JSON format. If modifications are required here to make tests pass, ensure that the frontend
 * is updated accordingly.
 *
 * For more information, visit: https://kotest.io/docs/assertions/json/json-schema-matchers.html
 */
@OptIn(ExperimentalKotest::class)
object KoTestJsonSchemas {

    private val allowedSeverityCodes = setOf("OK", "INFO", "WARNING", "ERROR", "FATAL")

    val lineNumbersSchema = jsonSchema {
        array(minItems = 0) { number() }
    }

    val feilSchema = jsonSchema {
        obj {
            withProperty("severity", optional = false) { string { beIn(allowedSeverityCodes) } }
            withProperty("caseworker", optional = false) { string() }
            withProperty("journalId", optional = false) { string() }
            withProperty("individId", optional = false) { string() }
            withProperty("contextId", optional = false) { string() }
            withProperty("ruleName", optional = false) { string() }
            withProperty("messageText", optional = false) { string() }
            withProperty("lineNumbers", optional = false) { lineNumbersSchema() }
            additionalProperties = false
        }
    }

    val innparametereSchema = jsonSchema {
        obj {
            withProperty("aar", optional = false) { integer { beInRange(2024L..2030L) } }
            withProperty("skjema", optional = false) { string() }
            withProperty("region", optional = false) { string() }
            withProperty("navn", optional = false) { string() }
            withProperty("orgnrForetak", optional = false) { string() }
            withProperty("filnavn", optional = false) { string() }
            additionalProperties = false
        }
    }

    val rapportSchema = jsonSchema {
        obj {
            withProperty("innparametere", optional = false) { innparametereSchema() }
            withProperty("antallKontroller", optional = false) { integer { beGreaterThan(0) } }
            withProperty("severity", optional = false) { string { beIn(allowedSeverityCodes) } }
            withProperty("feil", optional = false) { array(minItems = 0) { feilSchema() } }
            additionalProperties = false
        }
    }
}