package no.ssb.kostra.validation.rule.sosial.extension

fun String.municipalityIdFromRegion() = this.substring(0, 4)
fun String.districtIdFromRegion() = this.substring(4, 6)