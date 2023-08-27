package no.ssb.kostra.validation.rule

import no.ssb.kostra.program.DATE_TYPE
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.INTEGER_TYPE
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry


class Rule002FileDescription(
    val fieldDefinitions: List<FieldDefinition>
) : AbstractRule<List<String>>("Kontroll 002 : Filbeskrivese", Severity.FATAL) {
    override fun validate(context: List<String>, arguments: KotlinArguments): List<ValidationReportEntry>? = context
        .withIndex()
        .map { (index, recordString) ->
            recordString.toKostraRecord(
                index = index + 1,
                fieldDefinitions = fieldDefinitions
            )
        }
        .flatMap { kostraRecord ->
            fieldDefinitions
                .flatMap { fieldDefinition ->
                    val validationReportEntries: List<ValidationReportEntry> = mutableListOf()

                    // check if there is a value
                    if (kostraRecord[fieldDefinition.name].isNotBlank()) {
                        // value exists
                        if (fieldDefinition.codeList.isNotEmpty()
                            && fieldDefinition.codeIsMissing(kostraRecord[fieldDefinition.name])
                        )
                            validationReportEntries.plus(
                                createValidationReportEntry(
                                    ruleName = "$ruleName, feltdefinisjonlengde",
                                    messageText = "Korrigér felt ${fieldDefinition.number} / '${fieldDefinition.name}', " +
                                            "posisjon fra og med ${fieldDefinition.from} til og med ${fieldDefinition.to}, " +
                                            "sin kode '${kostraRecord[fieldDefinition.name]}' fins ikke i ${fieldDefinition.codeList}.",
                                    lineNumbers = listOf(kostraRecord.lineNumber)
                                )
                            )

                        // the value is an Integer
                        if (fieldDefinition.dataType == INTEGER_TYPE
                            && kostraRecord.fieldAsInt(fieldDefinition.name) == null
                        )
                            validationReportEntries.plus(
                                createValidationReportEntry(
                                    ruleName = "$ruleName, feil i heltall-felt",
                                    messageText = "Korrigér felt ${fieldDefinition.number} / '${fieldDefinition.name}', " +
                                            "posisjon fra og med ${fieldDefinition.from} til og med ${fieldDefinition.to}, " +
                                            "er et tallfelt, men inneholder '${kostraRecord[fieldDefinition.name]}'.",
                                    lineNumbers = listOf(kostraRecord.lineNumber)
                                )
                            )

                        // The value is a Date
                        if (fieldDefinition.dataType == DATE_TYPE
                            && kostraRecord[fieldDefinition.name] != "0".repeat(fieldDefinition.datePattern.length)
                            && kostraRecord.fieldAsLocalDate(fieldDefinition.name) == null
                        )
                            validationReportEntries.plus(
                                createValidationReportEntry(
                                    ruleName = "$ruleName, feil i dato-felt",
                                    messageText = "Korrigér felt ${fieldDefinition.number} / '${fieldDefinition.name}', " +
                                            "posisjon fra og med ${fieldDefinition.from} til og med ${fieldDefinition.to}, " +
                                            "er et datofelt med datomønster ('${fieldDefinition.datePattern.uppercase()}'), men inneholder '${kostraRecord[fieldDefinition.name]}'.",
                                    lineNumbers = listOf(kostraRecord.lineNumber)
                                )
                            )

                    } else {
                        // value is missing
                        if (fieldDefinition.mandatory)
                            validationReportEntries.plus(
                                createValidationReportEntry(
                                    ruleName = "$ruleName, mangler obligatorisk verdi",
                                    messageText = "Korrigér felt ${fieldDefinition.number} / '${fieldDefinition.name}', " +
                                            "posisjon fra og med ${fieldDefinition.from} til og med ${fieldDefinition.to}, " +
                                            "er obligatorisk, men mangler verdi.",
                                    lineNumbers = listOf(kostraRecord.lineNumber)
                                )
                            )

                    }

                    validationReportEntries
                }
        }
        .ifEmpty { null }
}