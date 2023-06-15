package no.ssb.kostra.area.sosial.kvalifisering

import no.ssb.kostra.program.FieldDefinition

fun FieldDefinition.getCodes() = this.codeList.map { it.code }

fun FieldDefinition.codeListToString() = this.codeList.map { it.toString() }

fun FieldDefinition.codeIsMissing(code: String) = this.codeList.none { it.code == code }

fun Collection<FieldDefinition>.findByColumnName(columnName: String) = this.first { it.name == columnName }

