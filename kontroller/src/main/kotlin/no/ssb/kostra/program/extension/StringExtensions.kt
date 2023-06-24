package no.ssb.kostra.program.extension

fun String.municipalityIdFromRegion() = this.substring(0, 4)
fun String.districtIdFromRegion() = this.substring(4, 6)