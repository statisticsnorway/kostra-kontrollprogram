package no.ssb.kostra.validation.rule

import no.ssb.kostra.program.DATE_TYPE
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.INTEGER_TYPE
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry


class Rule002FileDescription(
    val fieldDefinitions: List<FieldDefinition>
) : AbstractNoArgsRule<List<String>>("Kontroll 002 : Filbeskrivese", Severity.FATAL) {
    override fun validate(context: List<String>) = context
        .withIndex()
        .map { (index, recordString) ->
            recordString.toKostraRecord(
                index = index + 1,
                fieldDefinitions = fieldDefinitions
            )
        }
        .flatMap records@{ kostraRecord ->
            fieldDefinitions
                .map { fieldDefinition ->
                    kostraRecord to fieldDefinition
                }
        }.mapNotNull { (kostraRecord, fieldDefinition) ->
            validateValue(kostraRecord, fieldDefinition)
        }
        .ifEmpty { null }

    private fun validateValue(
        kostraRecord: KostraRecord,
        fieldDefinition: FieldDefinition
    ): ValidationReportEntry? = if (kostraRecord[fieldDefinition.name].isBlank()) {
        // value is missing / blank
        if (fieldDefinition.mandatory) {
            createValidationReportEntry(
                ruleName = "$ruleName, mangler obligatorisk verdi",
                messageText = "Korrigér felt ${fieldDefinition.number} / '${fieldDefinition.name}', " +
                        "posisjon fra og med ${fieldDefinition.from} til og med ${fieldDefinition.to}, " +
                        "mangler obligatorisk verdi.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        } else null
    } else {
        validateExistingValue(kostraRecord, fieldDefinition)
    }

    private fun validateExistingValue(
        kostraRecord: KostraRecord,
        fieldDefinition: FieldDefinition
    ): ValidationReportEntry? = when (fieldDefinition.dataType) {
        INTEGER_TYPE -> if (kostraRecord.fieldAsInt(fieldDefinition.name) == null)
            createValidationReportEntry(
                ruleName = "$ruleName, feil i heltall-felt",
                messageText = "Korrigér felt ${fieldDefinition.number} / '${fieldDefinition.name}', " +
                        "posisjon fra og med ${fieldDefinition.from} til og med ${fieldDefinition.to}, " +
                        "er et tallfelt, men inneholder '${kostraRecord[fieldDefinition.name]}'.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        else null

        DATE_TYPE -> if (kostraRecord.fieldAsLocalDate(fieldDefinition.name) == null)
            createValidationReportEntry(
                ruleName = "$ruleName, feil i dato-felt",
                messageText = "Korrigér felt ${fieldDefinition.number} / '${fieldDefinition.name}', " +
                        "posisjon fra og med ${fieldDefinition.from} til og med ${fieldDefinition.to}, " +
                        "er et datofelt med datomønster '${fieldDefinition.datePattern.uppercase()}', " +
                        "men inneholder '${kostraRecord[fieldDefinition.name]}'.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        else null

        else -> if (fieldDefinition.codeList.isNotEmpty()
            && fieldDefinition.codeIsMissing(kostraRecord[fieldDefinition.name])
        )
            createValidationReportEntry(
                ruleName = "$ruleName, feltdefinisjonlengde",
                messageText = "Korrigér felt ${fieldDefinition.number} / '${fieldDefinition.name}', " +
                        "posisjon fra og med ${fieldDefinition.from} til og med ${fieldDefinition.to}, " +
                        "sin kode '${kostraRecord[fieldDefinition.name]}' fins ikke i ${fieldDefinition.codeList}.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        else null
    }
}