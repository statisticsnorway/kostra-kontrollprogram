package no.ssb.kostra

object SharedConstants {

    const val OSLO_MUNICIPALITY_ID = "0301"
    val OSLO_DISTRICTS = (1..15).map { it.toString().padStart(2, '0') }
}