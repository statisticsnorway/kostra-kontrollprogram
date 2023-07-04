package no.ssb.kostra.program.extension

import no.ssb.kostra.program.FieldDefinition

fun FieldDefinition.codeListToString() = this.codeList.map { it.toString() }

fun FieldDefinition.codeIsMissing(code: String) = this.codeList.none { it.code == code }

fun FieldDefinition.codeExists(code: String) = this.codeList.any { it.code == code }

fun Collection<FieldDefinition>.findByColumnName(columnName: String) = this.first { it.name == columnName }

